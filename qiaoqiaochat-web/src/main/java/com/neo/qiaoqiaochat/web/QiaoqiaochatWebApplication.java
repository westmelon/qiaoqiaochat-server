package com.neo.qiaoqiaochat.web;

import com.neo.qiaoqiaochat.web.util.SnowflakeIdWorker;
import com.neo.qiaoqiaochat.web.util.SpringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QiaoqiaochatWebApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(QiaoqiaochatWebApplication.class, args);
        SpringUtils.setApplicationContext(context);

    }

    @Bean
    public SnowflakeIdWorker getIdworker(){
        return new SnowflakeIdWorker(1, 0);
    }


}
