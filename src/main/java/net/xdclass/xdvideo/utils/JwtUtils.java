package net.xdclass.xdvideo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.xdclass.xdvideo.domain.User;

import java.util.Date;

/**
 * JWT工具类
 */
public class JwtUtils {
    //定义一个发行者(也叫面向的用户)
    public static final String SUBJECT = "xdclass";
    //定义过期时间
    public static final long EXPIRE = 1000*60*60*24*7;
    //定义一个密钥
    public static final String APPSECRET = "xd666";

    /**
     * 生成jwt过程，也就相当于是加密的过程
     * @param user 用户对象，里面包含用户信息
     * @return
     */
    public static String geneJsonWebToken(User user){
        if (user==null||user.getId()==null||user.getName()==null||user.getHeadImg()==null){
            return null;
        }
        //由于要生成token，下面就要将token内的元素进行拼接，把需要的内容加入进去，也就是定义token三部分
        //下面方法的连续调用实际上就是字符串的拼接
        String token = Jwts.builder().setSubject(SUBJECT)//builder()：构建一个token，需要指定目标对象，setSubject(),也就是发行者
                .claim("id",user.getId())//claim()，该方法可以自定义属性，形式为key-value形式
                .claim("name",user.getName())//claim部分也就是token的负载部分
                .claim("img",user.getHeadImg())
                .setIssuedAt(new Date())//setIssuedAt()设置token发行的时间
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE))//setExpiration()设置token的一个过期时间
                .signWith(SignatureAlgorithm.HS256,APPSECRET).compact();//signWith()设置签名，参数为签名(加密)算法和密钥，密钥不能泄露，后期根据密钥来解密
                //compact()使得整个字符串紧凑压缩一下，返回了一个一连串的字符串
        return token;
    }

    /**
     * 校验token
     * @param token
     * @return
     */
    public static Claims checkJWT(String token){
        //这个Claims相当于一个map，返回值就是解析的结果，校验得到的token中的哪部分信息，是通过下面方法来进行设定的
        try {
            final Claims claims = Jwts.parser().setSigningKey(APPSECRET). //parser()生成一个解析类，
                    //setSigningKey()设置解密的密钥，也就是和加密的时候达成的统一协定，参数就是之前定义的密钥
                    parseClaimsJws(token).getBody();//负载，也就是对象信息
                    //parseClaimsJws()该方法就是要解析token，参数就是待解析的token
                    //getBody()该方法就是在解析成功后获取token中的负载，当然还有其他方法可以获取头部或者签名等
            return claims;
        }catch (Exception e){ }
        return null;
    }
}
