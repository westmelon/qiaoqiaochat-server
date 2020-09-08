package com.neo.qiaoqiaochat.web.redis;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 底层redis 操作服务
 * @author: copy
 */
@Component
public class RedisKVService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 指定缓存经过多久以后失效
     *
     * @param key 键
     * @param time 经过此时间后，这个key将会失效（单位：秒）
     * @return true成功 false失败
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            logger.error("指定缓存失效时间失败，key{},time{}",key,time,e);
            return false;
        }
    }

    /**
     * 指定缓存失效时间
     *
     * @param key 键
     * @param expireAt 失效时间
     * @return true成功 false失败
     */
    public boolean expireAt(String key, Date expireAt) {
        try {
            stringRedisTemplate.expireAt(key, expireAt);
            return true;
        } catch (Exception e) {
            logger.error("指定缓存失效时间失败，key{},expireAt{}", key, expireAt, e);
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                stringRedisTemplate.delete(key[0]);
            } else {
                stringRedisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 根据key获取
     * @param key 键
     * @return 值
     */
    public String get(String key) {

        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("缓存放入，key{},value{}",key,value,e);
            return false;
        }
    }

    /**
     * 缓存放入
     * @param key 键
     * @param value 值
     * @param time 失效时间  单位毫秒
     * @return true成功 false失败
     */
    public boolean set(String key, String value,long time) {
        try {
            stringRedisTemplate.opsForValue().set(key, value,time,TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception e) {
            logger.error("缓存放入，key{},value{},time{}",key,value,time,e);
            return false;
        }
    }

    /**
     * 对应Redis的INCRBY {key} {increment}命令
     *
     * @retrun true成功 false失败
     */
    public long incrby(String key, long increment) {
        try {
            Long result = stringRedisTemplate.opsForValue().increment(key, increment);
            if (result == null) {
                return 0;
            }
            return result;
        } catch (Exception e) {
            logger.error("incrby，key{},increment{}", key, increment, e);
            return 0;
        }
    }

}
