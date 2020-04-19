package com.neo.qiaoqiaochat.service.impl;

import com.google.gson.Gson;
import com.neo.qiaoqiaochat.config.redis.RedisCacheManager;
import com.neo.qiaoqiaochat.exception.BusinessException;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.ResultCode;
import com.neo.qiaoqiaochat.model.vo.TokenVO;
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




    public TokenVO getTokenVO(String account){
        TokenVO vo = new TokenVO();
        String token = getToken(account);
        vo.setToken(token);
        return vo;
    }

    //验证token的有效性
    public TokenVO checkTokenValid(String token, String account) {
        Cache<String, String> cache = cacheManager.getCache(QiaoqiaoConst.RedisCacheConfig.ACCOUNT_TOKEN);
        String cachedToken = cache.get(account);
        logger.info("比较token，参数中的{}，缓存的{}",token,cachedToken);
        TokenVO tokenVo = null;
        if (!token.equals(cachedToken)) {
            logger.info("token对比不一致");
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }

        if(!tokenValid(token, account)){
            logger.info("比较token，参数中的{}，缓存的{}",token,cachedToken);
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }
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

        //生成新的token TODO 使用新的算法生成token
        String token = UUID.randomUUID().toString();
        cache.put(account, token);
        return token;
    }

    private boolean tokenValid(String token, String account){

        return true;
//        String temp = "";
//        try {
//            temp = new String(CryptManager.getIns().des3Decrypt(CryptManager.hexToBytes(token),
//                    CryptManager.hexToBytes(myAppConfig.getGdzsKey())), "UTF-8");
//        } catch (Exception e) {
//            logger.error("token[{}]解密失败",token);
//            logger.error("解密失败原因",e);
//        }
//        if (StringUtils.isBlank(temp)) {
//            return null;
//        }
//        Gson gson = new Gson();
//        TokenVo tokenVo = gson.fromJson(temp, TokenVo.class);
//        String cachedNsrsbh = tokenVo.getNsrsbh();
//        Long cachedTime = tokenVo.getTimestamp();
//        if (!nsrsbh.equals(cachedNsrsbh)) {
//            logger.error("token[{}]验证纳税人识别号{}失败",token, nsrsbh);
//            return null;
//        }
//        Long now = System.currentTimeMillis();
//        long expired = now - cachedTime;
//        if(expired > MyAppConfig.tokenExpire*1000){
//            logger.error("token[{}]已失效",token);
//            return null;
//        }
//        return tokenVo;
    }
}
