package com.neo.qiaoqiaochat.config.redis;


import org.apache.ibatis.cache.CacheException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@ConditionalOnClass({RedisConfig.class})
public class RedisCacheManager implements CacheManager {

    private static String nameSpace = "pikachu";
    @Autowired
    @Qualifier(value = "sessionRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ShiroCacheProperties shiroCacheProperties;
    protected ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

    public RedisCacheManager() {
    }


    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        Cache c = this.caches.get(name);
        if (c == null) {
            int expire = 3600;
            if (this.shiroCacheProperties != null && this.shiroCacheProperties.getTimeout() > 0) {
                expire = this.shiroCacheProperties.getTimeout();
            }

            c = this.getCache(name, (long) expire);
        }

        return c;
    }

    public <K, V> Cache<K, V> getCache(String name, long expire) throws CacheException {
        Cache c = this.caches.get(name);
        if (c == null) {
            c = new RedisCache(name, this.redisTemplate);
            this.caches.put(name, c);
        }

        RedisCache redisCache = (RedisCache) c;
        if (expire != redisCache.getGlobExpire()) {
            redisCache.setGlobExpire(expire);
        }

        return c;
    }

    public static String makeKey(String prefix, String key) {
        StringBuilder builder = new StringBuilder();
        builder.append(nameSpace);
        if (!StringUtils.isEmpty(prefix)) {
            builder.append(":").append(prefix);
        }

        if (!StringUtils.isEmpty(key)) {
            builder.append(":").append(key);
        }

        return builder.toString();
    }

}
