package com.neo.qiaoqiaochat.web.service.impl;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.google.gson.Gson;
import com.neo.qiaoqiaochat.web.config.Audience;
import com.neo.qiaoqiaochat.web.config.redis.key.TokenKey;
import com.neo.qiaoqiaochat.web.config.redis.key.UserKey;
import com.neo.qiaoqiaochat.web.exception.BusinessException;
import com.neo.qiaoqiaochat.web.model.ResultCode;
import com.neo.qiaoqiaochat.web.model.domain.MiUserModel;
import com.neo.qiaoqiaochat.web.model.dto.LoginDTO;
import com.neo.qiaoqiaochat.web.model.vo.TokenVO;
import com.neo.qiaoqiaochat.web.model.vo.UserAccountVO;
import com.neo.qiaoqiaochat.web.redis.RedisService;
import com.neo.qiaoqiaochat.web.util.JwtUtils;
import com.neo.qiaoqiaochat.web.util.PasswordUtils;

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

        return;

    }

    public TokenVO doAfterLogin(String account) {

        // TODO: 2020/9/7
        UserAccountVO user = userService.findUserByAccount(account);
        String token = JwtUtils.buildJWT(user, audience);

        //用户信息放入缓存
        redisService.set(new UserKey(account), user);
        //token放入缓存
        redisService.set(new TokenKey(account), token);

        return TokenVO.builder().account(account).token(token).build();


    }

}
