package com.neo.qiaoqiaochat.websocket.config.redis;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(
        prefix = "shiro.cache"
)
public class ShiroCacheProperties {
    private Integer timeout;

    public ShiroCacheProperties() {
    }

    public Integer getTimeout() {
        if (this.timeout == null || this.timeout < 0) {
            this.timeout = 1800;
        }

        return this.timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}