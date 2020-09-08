package com.neo.qiaoqiaochat.web.config.redis.key;

import com.neo.qiaoqiaochat.web.redis.RedisKeyGenerator;

/**
 * @author linyi 2020-09-07
 */
public class TokenKey extends RedisKeyGenerator<String> {

    public TokenKey(String key) {
        super(key);
    }

    @Override
    public String namespace() {
        return "965";
    }

    @Override
    public String service() {
        return "app";
    }

    @Override
    public String function() {
        return "token";
    }

    @Override
    public Class<String> type() {
        return String.class;
    }

}
