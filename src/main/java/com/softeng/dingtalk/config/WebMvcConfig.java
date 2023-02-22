package com.softeng.dingtalk.config;

import com.softeng.dingtalk.component.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author zhanyeye
 * @description
 * @create 12/11/2019 2:20 PM
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login")
                .excludePathPatterns("/api/login_test/**")
                .excludePathPatterns("/api/v2/login")
                .excludePathPatterns("/api/v2/login_test/**")
                .excludePathPatterns("/wsapi");
    }

    /**
     * webSocket
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}