package com.neo.qiaoqiaochat;

import com.neo.qiaoqiaochat.util.SnowflakeIdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QiaoqiaochatWebsocketApplication {

    public static void main(String[] args) {
		
        SpringApplication.run(QiaoqiaochatWebsocketApplication.class, args);

    }

    @Bean
    public SnowflakeIdWorker getIdworker(){
        return new SnowflakeIdWorker(0, 0);
    }


}
