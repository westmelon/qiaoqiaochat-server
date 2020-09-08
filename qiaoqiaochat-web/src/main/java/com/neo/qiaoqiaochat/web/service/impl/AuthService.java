package com.neo.qiaoqiaochat.web.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.neo.qiaoqiaochat.web.config.Audience;
import com.neo.qiaoqiaochat.web.config.redis.key.TokenKey;
import com.neo.qiaoqiaochat.web.exception.BusinessException;
import com.neo.qiaoqiaochat.web.model.ResultCode;
import com.neo.qiaoqiaochat.web.model.vo.TokenVO;
import com.neo.qiaoqiaochat.web.model.vo.UserAccountVO;
import com.neo.qiaoqiaochat.web.redis.RedisService;
import com.neo.qiaoqiaochat.web.util.JwtUtils;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private RedisService redisService;

    @Autowired
    private Audience audience;


    //todo
    public TokenVO getTokenVO(String account) {
        TokenVO vo = new TokenVO();
        String token = getToken(account);
        vo.setToken(token);
        return vo;
    }

    //验证token的有效性
    public TokenVO checkTokenValid(String token) {


        UserAccountVO vo = JwtUtils.parseJWT(token, audience.getBase64Secret());
        String account = vo.getAccount();
        String tokenCached = redisService.get(new TokenKey(account));
        logger.info("比较token，参数中的{}，缓存的{}", token, tokenCached);

        TokenVO tokenVo = new TokenVO();
        if (!token.equals(tokenCached)) {
            logger.info("token对比不一致");
            throw new BusinessException(ResultCode.ACCESS_DENIED);
        }

        tokenVo.setToken(token);
        tokenVo.setAccount(account);
        return tokenVo;
    }

    private synchronized String getToken(String account) {
        boolean needFresh = false;
        //先从缓存中获取token 然后校验token的有效性 如果有效则返回token 无效则重新获取并放入缓存

        String tokenCached = redisService.get(new TokenKey(account));

        if (StringUtils.isNotBlank(tokenCached)) {
            if (!tokenValid(tokenCached, account)) {
                needFresh = true;
            }
        } else {
            needFresh = true;
        }
        if (!needFresh) {
            return tokenCached;
        }
        // TODO: 2020/9/7  重新登陆？
        //生成新的token
//        String token = JwtUtils.buildJWT(account, audience);
//        redisService.set(new TokenKey(account), token);
        return "";
    }


    private boolean tokenValid(String cachedToken, String account) {
        try {
            UserAccountVO vo = JwtUtils.parseJWT(cachedToken, audience.getBase64Secret());

            if (!account.equals(vo.getAccount())) {
                return false;
            }
        } catch (Exception e) {
            logger.error("token解密失敗", e);
            return false;
        }
        return true;
    }

}
