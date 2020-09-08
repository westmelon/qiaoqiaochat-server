package com.neo.qiaoqiaochat.web.redis;

/**
 * @author linyi 2020-09-07
 */
public abstract class RedisKeyGenerator<T> {

    public RedisKeyGenerator(String key) {
        this.key = key;
    }

    private final String key;

    public abstract String namespace();

    public abstract String service();

    public abstract String function();


    public abstract Class<T> type();

    public String get() {
        return String.format("%s_%s_%s_%s", namespace(), service(), function(), key);
    }

}
