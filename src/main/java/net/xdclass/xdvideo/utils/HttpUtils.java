package net.xdclass.xdvideo.utils;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装http get和post请求
 * 步骤：创建client，设置请求，设置参数，发送，响应，返回结果，关闭client
 */
public class HttpUtils {
    private static final Gson gson = new Gson();
    /**
     * get方法
     * @param url
     * @return
     */
    public static Map<String,Object> doGet(String url){
        Map<String,Object> map = new HashMap<>();

        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)//连接超时
                .setConnectionRequestTimeout(5000)//请求超时
                .setSocketTimeout(5000)//
                .setRedirectsEnabled(true)//允许自动重定向
                .build();
        HttpGet httpGet = new HttpGet(url);//拿到http的get方法
        httpGet.setConfig(requestConfig);//设置配置信息

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);//将http的get请求执行，返回了一个http响应
            if (httpResponse.getStatusLine().getStatusCode()==200){
                //如果成功，拿到响应的结果，转换成字符串形式(json)
                String jsonResult = EntityUtils.toString(httpResponse.getEntity());
                //将json转为gson,也就是将String转成map
                map = gson.fromJson(jsonResult,map.getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                //最后客户端得关闭
                httpClient.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return map;
    }

    public static String doPost(String url,String data,int timeout){

        CloseableHttpClient httpClient = HttpClients.createDefault();

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout)//连接超时
                .setConnectionRequestTimeout(timeout)//请求超时
                .setSocketTimeout(timeout)//
                .setRedirectsEnabled(true)//允许自动重定向
                .build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("Content-Type","text/html;charset=UTF-8");

        if (data!=null && data instanceof String){//使用字符串传参
            StringEntity stringEntity = new StringEntity(data,"UTF-8");
            httpPost.setEntity(stringEntity);
        }
        //发送请求
        try{
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode()==200){
                //如果成功，拿到响应的结果，转换成字符串形式(json)
                String result = EntityUtils.toString(httpEntity);
                return result;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
