package com.neo.qiaoqiaochat.websocket.model.key;

import java.util.Set;
import com.neo.core.redis.RedisKeyGenerator;

/**
 * @author linyi 2020-09-09
 */
public class NettySessionIdKey extends RedisKeyGenerator<Set> {

    public NettySessionIdKey(String key) {
        super(key);
    }

    @Override
    public String namespace() {
        return "965";
    }

    @Override
    public String service() {
        return "websocket";
    }

    @Override
    public String function() {
        return "netty-session";
    }

    @Override
    public Class<Set> type() {
        return Set.class;
    }

}
