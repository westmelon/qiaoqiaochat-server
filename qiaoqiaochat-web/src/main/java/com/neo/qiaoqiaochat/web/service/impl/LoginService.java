package com.neo.qiaoqiaochat.web.service.impl;

import com.google.gson.Gson;
import com.neo.core.entity.ResultCode;
import com.neo.core.entity.TokenVO;
import com.neo.core.entity.UserParam;
import com.neo.core.exception.BusinessException;
import com.neo.core.jwt.Audience;
import com.neo.core.jwt.JwtUtils;
import com.neo.core.redis.RedisService;
import com.neo.core.redis.key.TokenKey;
import com.neo.core.redis.key.UserKey;
import com.neo.core.util.PasswordUtils;
import com.neo.qiaoqiaochat.web.model.domain.MiUserModel;
import com.neo.qiaoqiaochat.web.model.dto.LoginDTO;
import com.neo.qiaoqiaochat.web.model.vo.UserAccountVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class LoginService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private Audience audience;


    @Deprecated
    public String login(String content) {
        LoginDTO loginDTO = new Gson().fromJson(content, LoginDTO.class);

        String account = loginDTO.getAccount();
        String password = loginDTO.getPassword();
        logger.info("account-{},password-{}", account, password);

        String token = UUID.randomUUID().toString();
        redisService.set(new TokenKey(account), token);

        return token;

    }

    public void login(LoginDTO dto) {

        String account = dto.getAccount();
        String password = dto.getPassword();
        logger.info("account-{},password-{}", account, password);
        // TODO: 2020/9/7
        MiUserModel savedAccount = userService.getUserByAccount(account);
        if (savedAccount == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }
        String savedPassword = savedAccount.getPassword();
        String sin = PasswordUtils.sin(password);
        if (StringUtils.isEmpty(sin) || StringUtils.isEmpty(savedPassword) || !sin.equals(savedPassword)) {
            throw new BusinessException(ResultCode.PASSWORD_NOT_MATCH);
        }

    }

    public TokenVO doAfterLogin(String account) {

        // TODO: 2020/9/7
        UserAccountVO user = userService.findUserByAccount(account);
        UserParam param = new UserParam();
        BeanUtils.copyProperties(user, param);
        String token = JwtUtils.buildJWT(param, audience);

        //用户信息放入缓存
        redisService.set(new UserKey(account), param);
        //token放入缓存
        redisService.set(new TokenKey(account), token);

        return TokenVO.builder().account(account).token(token).build();


    }

}
