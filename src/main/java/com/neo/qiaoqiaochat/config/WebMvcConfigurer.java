package com.neo.qiaoqiaochat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * 说明：注册过滤器
 * 作者：王兆阳
 * 日期：2017-05-22
 **/
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

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
