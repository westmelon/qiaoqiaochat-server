package com.neo.qiaoqiaochat.service.impl;

import com.google.gson.Gson;
import com.neo.qiaoqiaochat.config.Audience;
import com.neo.qiaoqiaochat.config.redis.RedisCacheManager;
import com.neo.qiaoqiaochat.exception.BusinessException;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.ResultCode;
import com.neo.qiaoqiaochat.model.vo.TokenVO;
import com.neo.qiaoqiaochat.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisCacheManager cacheManager;

    @Autowired
    private Audience audience;


    public TokenVO getTokenVO(String account){
        TokenVO vo = new TokenVO();
        String token = getToken(account);
        vo.setToken(token);
        return vo;
    }

    //验证token的有效性
    public TokenVO checkTokenValid(String token) {
        Cache<String, String> cache = cacheManager.getCache(QiaoqiaoConst.RedisCacheConfig.ACCOUNT_TOKEN);

        String account = JwtUtils.parseJWT(token, audience.getBase64Secret());
        String cachedToken = cache.get(account);
        logger.info("比较token，参数中的{}，缓存的{}",token,cachedToken);
        TokenVO tokenVo = new TokenVO();
        if (!token.equals(cachedToken)) {
            logger.info("token对比不一致");
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }

        if(!tokenValid(token, account)){
            logger.info("比较token，参数中的{}，缓存的{}",token,cachedToken);
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
        tokenVo.setToken(token);
        tokenVo.setAccount(account);
        return tokenVo;
    }

    private synchronized String getToken(String account){
        boolean needFresh = false;
        //先从缓存中获取token 然后校验token的有效性 如果有效则返回token 无效则重新获取并放入缓存
        Cache<String, String> cache = cacheManager.getCache(QiaoqiaoConst.RedisCacheConfig.ACCOUNT_TOKEN);
        String cachedToken = cache.get(account);
        if(StringUtils.isNotBlank(cachedToken)){
            if(!tokenValid(cachedToken, account)){
                needFresh = true;
            }
        }else{
            needFresh = true;
        }
        if(!needFresh){
            return cachedToken;
        }

        //生成新的token
        String token = JwtUtils.buildJWT(account, audience );
        cache.put(account, token);
        return token;
    }

    private boolean tokenValid(String token, String account){
        String decode = JwtUtils.parseJWT(token, audience.getBase64Secret());

        return decode.equals(account);

    }
}
