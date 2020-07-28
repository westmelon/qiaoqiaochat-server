package com.neo.qiaoqiaochat.web.shiro;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@ConfigurationProperties(
        prefix = "shiro.session"
)
public class SessionProperties {
    private Integer timeout;
    private String prefix;

    public SessionProperties() {
    }

    public Integer getTimeout() {
        if (this.timeout == null || this.timeout < 0) {
            this.timeout = 3600;
        }

        return this.timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getPrefix() {
        return StringUtils.isEmpty(this.prefix) ? "q-session" : this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}