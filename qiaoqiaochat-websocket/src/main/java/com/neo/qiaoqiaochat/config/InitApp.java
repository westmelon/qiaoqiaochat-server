package com.neo.qiaoqiaochat.config;

import com.neo.qiaoqiaochat.config.redis.RedisCacheManager;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean({RedisCacheManager.class})
public class InitApp {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisCacheManager cacheManager;

    public void init() {
        //初始化缓存
        cacheManager.getCache(QiaoqiaoConst.RedisCacheConfig.NETTY_SESSION_NAMESPACE, 3600);
        cacheManager.getCache(QiaoqiaoConst.RedisCacheConfig.ACCOUNT_SESSIONIDS, 3600);
        cacheManager.getCache(QiaoqiaoConst.RedisCacheConfig.ACCOUNT_TOKEN, 3600);


    }
}
