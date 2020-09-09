package com.neo.qiaoqiaochat.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import com.neo.core.util.SnowflakeIdWorker;

@SpringBootApplication
@ComponentScan({"com.neo.**"})
public class QiaoqiaochatWebsocketApplication {

    public static void main(String[] args) {
		
        SpringApplication.run(QiaoqiaochatWebsocketApplication.class, args);

    }

    @Bean
    public SnowflakeIdWorker getIdworker(){
        return new SnowflakeIdWorker(0, 0);
    }


}
