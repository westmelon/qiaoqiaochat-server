package com.neo.qiaoqiaochat.service.impl;

import com.google.gson.Gson;
import com.neo.qiaoqiaochat.config.redis.RedisCacheManager;
import com.neo.qiaoqiaochat.exception.BusinessException;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.ResultCode;
import com.neo.qiaoqiaochat.model.dto.LoginDTO;
import com.neo.qiaoqiaochat.model.vo.UserAccountVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoginService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisCacheManager cacheManager;

    @Autowired
    private UserServiceImpl userService;

    @Deprecated
    public String login(String content){
        LoginDTO loginDTO = new Gson().fromJson(content, LoginDTO.class);

        String username = loginDTO.getAccount();
        String password = loginDTO.getPassword();
        logger.info("username-{},password-{}",username, password);

        //todo 生成token  丢进缓存 token为key 账号主体为value？
        String token = UUID.randomUUID().toString();
        final Cache cache = cacheManager.getCache("token",1800);
        cache.put(token, username);

        return token;

    }

    public void login(LoginDTO dto){

        String account = dto.getAccount();
        String password = dto.getPassword();
        logger.info("account-{},password-{}",account, password);

        UsernamePasswordToken token = new UsernamePasswordToken(account, password);
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(token);
        }catch (ExcessiveAttemptsException e){
            throw new BusinessException(ResultCode.PASSWORD_TRY_TOO_MUCH_TIME);
        }catch (UnknownAccountException e){
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }catch (AuthenticationException e) {
            logger.error(e.toString(),e);
                throw new BusinessException(ResultCode.PASSWORD_NOT_MATCH);
        }




    }

    public void doAfterLogin(String account){
        UserAccountVO user = userService.findUserByAccount(account);
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        //向session 存入用户信息
        session.setAttribute(QiaoqiaoConst.ShiroConfig.USER_INFO, new Gson().toJson(user));



    }




}
