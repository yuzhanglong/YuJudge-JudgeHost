package com.yzl.judgehost.core.handlers;

import com.yzl.judgehost.core.authorization.AuthorizationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yuzhanglong
 * @description 请求拦截器
 * @date 2020-7-2 8:47
 */

@Configuration
public class HttpRequestHandler implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public AuthorizationInterceptor authenticationInterceptor() {
        return new AuthorizationInterceptor();
    }
}