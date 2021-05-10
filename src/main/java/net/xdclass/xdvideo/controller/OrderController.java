package net.xdclass.xdvideo.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.xdclass.xdvideo.domain.JsonData;
import net.xdclass.xdvideo.dto.VideoOrderDto;
import net.xdclass.xdvideo.service.VideoOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * 订单接口
 */
@RestController
@RequestMapping("/api/v1/order")//不会被拦截器拦截，用于测试
//@RequestMapping("/user/api/v1/order")//这个路径会被拦截器拦截，只有登录了才能放行
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Logger dataLogger = LoggerFactory.getLogger("dataLogger");

    @Autowired
    private VideoOrderService videoOrderService;

    @GetMapping("add")
    public JsonData saveOrder(@RequestParam(value = "video_id",required = true)int videoId,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception{

        //String ip = IpUtils.getIpAddr(request);//获取用户的ip
        String ip = "192.168.253.1";//临时
        //int userId = request.getAttribute("user_id");
        int userId = 1;
        VideoOrderDto videoOrderDto = new VideoOrderDto();
        videoOrderDto.setUserId(userId);
        videoOrderDto.setVideoId(videoId);
        videoOrderDto.setIp(ip);

        String codeUrl = videoOrderService.save(videoOrderDto);
        if (codeUrl==null){
            throw new NullPointerException();
        }

        //生成二维码
        try{
            //生成二维码配置
            Map<EncodeHintType,Object> hints =  new HashMap<>();

            //设置纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //编码类型
            hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
            //生成二维码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE,400,400,hints);
            //以流的形式将二维码传到页面
            OutputStream out =  response.getOutputStream();

            MatrixToImageWriter.writeToStream(bitMatrix,"png",out);

        }catch (Exception e){
            e.printStackTrace();
        }

        //生成二维码之后，用户扫码之后，如果扫码成功了，微信支付系统会回调给服务端，此时就是回调，然后第三方
        //进行更新订单，因此下面要进行回调+更新订单
        //回调主要是通知第三方支付结果

        return JsonData.buildSuccess("下单成功");
    }
}
