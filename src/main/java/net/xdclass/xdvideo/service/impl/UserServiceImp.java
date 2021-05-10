package net.xdclass.xdvideo.service.impl;

import net.xdclass.xdvideo.config.WeChatConfig;
import net.xdclass.xdvideo.domain.User;
import net.xdclass.xdvideo.mapper.UserMapper;
import net.xdclass.xdvideo.service.UserService;
import net.xdclass.xdvideo.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User saveWechatUser(String code) {
        //向微信那边请求token的地址
        String accessTokenUrl = String.format(WeChatConfig.getOpenAccessTokenUrl(),weChatConfig.getOpenAppid(),weChatConfig.getOpenAppsecret(),code);
        //调用httpClient工具类，以http客户端的形式同微信进行交互，也就是向微信发送请求
        //请求结果中就已经包含了access_token了
        Map<String,Object> baseMap = HttpUtils.doGet(accessTokenUrl);
        if (baseMap==null||baseMap.isEmpty()){
            return null;
        }
        //从官方文档中知道，请求结果中会包含token，因此我们可以获取到token
        String accessToken = (String) baseMap.get("access_token");
        //从请求结果中获取授权用户的唯一标识
        String openId = (String) baseMap.get("openid");

        User dbUser = userMapper.findByOpenId(openId);
        if (dbUser!=null){//说明查到了，直接返回
            return dbUser;
        }

        //有了token之后就可以向微信获取用户的基本信息了
        //获取用户基本信息除了token之外还需要openid，上面已经获得了
        String userInfoUrl = String.format(WeChatConfig.getOpenUserInfoUrl(),accessToken,openId);
        Map<String,Object> baseUserMap = HttpUtils.doGet(accessTokenUrl);
        if (baseUserMap==null||baseMap.isEmpty()){
            return null;
        }
        //从官方文档中知道，请求结果中会包含许多用户的基本信息，我们可以从中提取出需要的
        String nickname = (String) baseMap.get("nickname");
        Double sexTemp = (Double) baseMap.get("sex");
        int sex = sexTemp.intValue();//转成int类型
        String province = (String) baseMap.get("province");
        String city = (String) baseMap.get("city");
        String country = (String) baseMap.get("country");
        String headimgurl = (String) baseMap.get("headimgurl");
        StringBuilder sb = new StringBuilder(country).append("||").append(province).append("||").append(city);
        String finalAddress = sb.toString();
        try {
            //字符串中文的统一进行转码,解决乱码
            nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
            finalAddress = new String(finalAddress.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //下面开始，封装user对象
        User user = new User();
        user.setName(nickname);
        user.setHeadImg(headimgurl);
        user.setCity(finalAddress);
        user.setOpenid(openId);
        user.setSex(sex);
        user.setCreateTime(new Date());

        userMapper.save(user);

        return user;
    }
}
