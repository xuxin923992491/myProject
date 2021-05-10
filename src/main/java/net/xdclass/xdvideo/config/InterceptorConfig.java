package net.xdclass.xdvideo.config;

import net.xdclass.xdvideo.intercepter.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //定义自己的拦截器
        //注册.添加拦截器（创建自定义的拦截器对象）.添加拦截路径（“拦截路径”）
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/user/api/v1/*/**");
        //让注册器生效
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
