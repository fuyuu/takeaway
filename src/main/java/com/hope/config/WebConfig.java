package com.hope.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/user/login","/user/register",
                        "/user/send-code","/user/forget","/user/refreshToken",
                        "/merchant/list","/merchant/{id}/spu","/merchant/list-with-dishes","/cart/list",
                        "/voucher/all/{id}","/user","/banner/photos","/category/list-merchant"
                );
    }
}
