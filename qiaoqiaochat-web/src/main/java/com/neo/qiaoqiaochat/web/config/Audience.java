package com.neo.qiaoqiaochat.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "audience")
@Component
@Data
public class Audience {

    private String clientId;
    private String base64Secret;
    private String name;
    private String salt;
    private long expiresSecond;

}