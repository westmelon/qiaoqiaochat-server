package com.neo.qiaoqiaochat.web.redis;

import javax.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author linyi 2020-09-07
 */
public class RedisCache<K, V> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private RedisCache<K, V> cache;




}
