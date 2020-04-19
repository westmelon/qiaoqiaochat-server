package com.neo.qiaoqiaochat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.List;



@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns     用于添加拦截规则
        // excludePathPatterns 用于排除拦截

        super.addInterceptors(registry);
    }


    /**
     * 初始化应用
     * @return
     */
    @Bean(initMethod = "init")
    public InitApp initializeService(){
        return new InitApp();
    }


}
