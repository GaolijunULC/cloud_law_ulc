package com.clyl.cloudlaw.config;

import com.clyl.cloudlaw.filter.JWTInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Iced
 * @version 1.0
 * 2022/12/9
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new JWTInterceptor());
        registration.addPathPatterns("");
        registration.excludePathPatterns(
                "/api/user/login",
                "/api/talk/newTalk",
                "/api/talk/getTalks",
                "/**/*.html",
                "/**/*.js",
                "/**/*.css"
        );
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许跨域请求的地址
                .allowedOriginPatterns("*")
                // 允许跨域请求的域名
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATH")
                // 允许请求的方式
                .allowCredentials(true)
                // 是否允许证书（cookies）
                .maxAge(3000);
        // 跨域允许时间
    }

}
