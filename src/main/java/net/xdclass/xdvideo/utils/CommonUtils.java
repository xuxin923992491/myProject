package net.xdclass.xdvideo.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 常用工具类的封装，md5，uuid等
 */
public class CommonUtils {

    /**
     * 生成 uuid， 即用来标识一笔单，也用做 nonce_str，也就是流水号
     * @return
     */
    public static String generateUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-","").substring(0,32);
        return uuid;
    }

    /**
     * md5常用工具类
     * @param data
     * @return
     */
    public static String MD5(String data){
        try {
            //获取MD5加密算法的摘要对象，就是相当于创建一个算法对象
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //算法对象对数据进行摘要计算（相当于进行加密的过程），返回成byte数组类型
            byte[] array = md5.digest(data.getBytes("UTF-8"));//原来的数据进行utf-8编码
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {//遍历经过计算后的数据，进行按位操作，转成16进制，作为加密后的结果
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();//全部转为大写

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
