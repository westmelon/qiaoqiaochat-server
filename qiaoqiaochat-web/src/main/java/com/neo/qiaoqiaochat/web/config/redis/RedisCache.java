package com.neo.qiaoqiaochat.web.config.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisCache<K, V> implements Cache<K, V> {

    private String cacheKey;
    private RedisTemplate<K, V> redisTemplate;
    private long globExpire = 1800L;

    public RedisCache(String name, RedisTemplate client) {
        this.cacheKey = RedisCacheManager.makeKey(name, "");
        this.redisTemplate = client;
    }

    public V get(K key) throws CacheException {
        this.redisTemplate.boundValueOps(this.getCacheKey(key)).expire(this.globExpire, TimeUnit.SECONDS);
        return this.redisTemplate.boundValueOps(this.getCacheKey(key)).get();
    }

    public V put(K key, V value) throws CacheException {
        V old = this.get(key);
        this.redisTemplate.boundValueOps(this.getCacheKey(key)).set(value);
        this.redisTemplate.boundValueOps(this.getCacheKey(key)).expire(this.globExpire, TimeUnit.SECONDS);
        return old;
    }

    public V remove(K key) throws CacheException {
        V old = this.get(key);
        this.redisTemplate.delete(this.getCacheKey(key));
        return old;
    }

    public void clear() throws CacheException {
        this.redisTemplate.delete(this.keys());
    }

    public int size() {
        return this.keys().size();
    }

    public Set<K> keys() {
        return this.redisTemplate.keys(this.getCacheKey("*"));
    }

    public Collection<V> values() {
        Set<K> set = this.keys();
        List<V> list = new ArrayList();

        for (K s : set) {
            list.add(this.get(s));
        }
        return list;
    }

    public Map<K, V> mapValues() {
        Set<K> set = this.keys();
        Map<K, V> map  = new HashMap<>();

        for (K s : set) {
            map.put(s, this.get(s));
        }
        return map;
    }

    private K getCacheKey(Object k) {
        return (K)(this.cacheKey + ":" + k.toString());
    }

    public long getGlobExpire() {
        return this.globExpire;
    }

    public void setGlobExpire(long globExpire) {
        this.globExpire = globExpire;
    }
}
