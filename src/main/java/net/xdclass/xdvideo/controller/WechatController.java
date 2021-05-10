package net.xdclass.xdvideo.controller;

import net.xdclass.xdvideo.config.WeChatConfig;
import net.xdclass.xdvideo.domain.JsonData;
import net.xdclass.xdvideo.domain.User;
import net.xdclass.xdvideo.domain.VideoOrder;
import net.xdclass.xdvideo.mapper.VideoOrderMapper;
import net.xdclass.xdvideo.service.UserService;
import net.xdclass.xdvideo.service.VideoOrderService;
import net.xdclass.xdvideo.utils.JwtUtils;
import net.xdclass.xdvideo.utils.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

@Controller
@RequestMapping("/api/v1/wechat")
//@CrossOrigin
public class WechatController {
    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoOrderService videoOrderService;


    /**
     * 拼装扫一扫登录url
     * @return
     */
    @GetMapping("login_url")
    @ResponseBody
    public JsonData loginUrl(@RequestParam(value = "access_page",required = true)String accessPage) throws UnsupportedEncodingException {
        //access_page当前页面的地址
        //个人理解：向重定向地址的接口送code，但是页面应该会回到我们传入的参数，也就是当前页面的地址
        String redirectUrl = weChatConfig.getOpenRedirectUrl();//获取开放平台重定向地址，也就是扫一扫之后回调接口的地址

        String callbackUrl = URLEncoder.encode(redirectUrl,"GBK");//进行编码

        String qrcodeUrl = String.format(weChatConfig.getOpenQrcodeUrl(),weChatConfig.getOpenAppid(),callbackUrl,accessPage);

        return JsonData.buildSuccess(qrcodeUrl);
    }

    /**
     * 微信扫码登录回调地址
     * @param code
     * @param state
     * @param response
     * @throws IOException
     */
    @GetMapping("/user/callback")
    public void wechatUserCallback(@RequestParam(value = "code",required = true) String code, String state, HttpServletResponse response) throws IOException {
        //第三方需要通过code来向微信获取token，因此，现在需要看看能不能获取code
        //由于我们是在本机，不能够访问真实回调的服务器，并且我们也不能够登录到“狼途”账号，因此也不能设置自己的
        //回调地址，所以我们也是只能够进行代码的练习，此处我们就当成已经获取到了code和state了
        /*System.out.println("code=" + code);
        System.out.println("state" + state);*/
        //那么现在就要拿回调回来的code和state再向微信那边去获取access_token了，此时需要封装一个api来向微信请求
        //也就是同样的操作，从文档中拿到请求链接，并给链接赋值参数即可
        User user = userService.saveWechatUser(code);//执行完之后，应该从微信那边获取到用户对象
        //截止到目前，第三方和微信的交互结束，下面则是用户和第三方之间的交互
        //也就是用户扫码之后并确认授权之后的过程，在这个过程期间，第三方拿到了用户的相关信息，也就是用户扫码并
        //授权后，到了该控制器页面，也就是此时的回调页面
        //此时开始就是用户和第三方交互
        if (user!=null){
            //生成jwt
            //此时拿到了token
            String token = JwtUtils.geneJsonWebToken(user);
            //请求转发到state页面
            //state当前用户的页面地址需要拼接http://  这样才不会站内跳转
            response.sendRedirect(state+"?token="+token+"&head_img"+user.getHeadImg()+"&name="+URLEncoder.encode(user.getName(),"UTF-8"));

        }
    }

    /**
     * 微信支付回调，以流的形式传输
     * @param request
     * @param response
     */
    @RequestMapping("/order/callback")
    public void orderCallback(HttpServletRequest request,HttpServletResponse response) throws Exception{
        InputStream inputStream = request.getInputStream();
        //BufferedReader是包装设计模式，性能更高
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine())!=null){
            sb.append(line);
        }
        in.close();
        inputStream.close();
        //流结束，说明微信那边将xml信息回调给了第三方，第三方需要转为map来进行读取
        Map<String,String> callbackMap = WXPayUtil.xmlToMap(sb.toString());
        System.out.println(callbackMap.toString());
        //此时拿到了微信那边的回调信息，我们第三方需要响应给微信表示第三方收到了
        //第三方这边收到了微信那边的回调信息，其中会包含是否支付成功，成功了那么会更新数据库的订单信息，因此
        //第三方这边需要校验签名，防止没支付成功也更新订单了，也就是防止伪造回调

        //首先进行校验，但是校验的前提得是sortedmap，因此先将上面的map转为sortedmap
        SortedMap<String,String> sortedMap = WXPayUtil.getSortedMap(callbackMap);
        //判断签名是否正确
        if (WXPayUtil.isCorrectSign(sortedMap,weChatConfig.getKey())){
            //下面需要判断支付成功，成功才能更新订单状态
            if ("SUCCESS".equals(sortedMap.get("result_code"))){
                //此时需要更新订单状态
                //为什么是更新，因为之前已经下单了，只不过是处于未支付状态，当支付成功后，再修改订单状态
                //表示整个一次下单操作结束

                //先查找订单
                String outTradeNo = sortedMap.get("out_trade_no");
                VideoOrder dbVideoOrder = videoOrderService.findByOutTradeNo(outTradeNo);
                if (dbVideoOrder!=null && dbVideoOrder.getState()==0){  //判断逻辑看业务场景
                    //下面开始更新
                    VideoOrder videoOrder = new VideoOrder();
                    videoOrder.setOpenid(sortedMap.get("openid"));
                    videoOrder.setOutTradeNo(outTradeNo);
                    videoOrder.setNotifyTime(new Date());
                    videoOrder.setState(1);
                    int rows = videoOrderService.updateVideoOrderByOutTradeNo(videoOrder);
                    if (rows==1){//需要通知微信订单处理成功
                        response.setContentType("text/xml");
                        response.getWriter().println("success");
                        return;
                    }
                }
            }
        }
        //没进入上面，就说明订单处理失败
        response.setContentType("text/xml");
        response.getWriter().println("fail");
        return;
    }


}
