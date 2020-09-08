package com.neo.qiaoqiaochat.web.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.google.gson.Gson;

/**
 * @author linyi 2020-09-07
 */
@Component
public class RedisService {

    @Autowired
    private RedisKVService redisKVService;


    public <T> T get(RedisKeyGenerator<T> generator) {
        Class<T> type = generator.type();

        String value = redisKVService.get(generator.get());
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return new Gson().fromJson(value, type);
    }

    public <T> void set(RedisKeyGenerator<T> generator, T value) {
        String json = new Gson().toJson(value);
        redisKVService.set(generator.get(), json);
    }

}
