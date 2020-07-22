package com.neo.qiaoqiaochat;

import com.neo.qiaoqiaochat.util.SnowflakeIdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QiaoqiaochatWebApplication {

    public static void main(String[] args) {
		
        SpringApplication.run(QiaoqiaochatWebApplication.class, args);

    }

    @Bean
    public SnowflakeIdWorker getIdworker(){
        return new SnowflakeIdWorker(1, 0);
    }


}
