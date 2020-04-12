package com.neo.qiaoqiaochat.web.controller;


import com.neo.commons.utils.BaseController;
import com.neo.qiaoqiaochat.model.SimpleResult;
import com.neo.qiaoqiaochat.model.dto.LoginDTO;
import com.neo.qiaoqiaochat.model.vo.TokenVO;
import com.neo.qiaoqiaochat.model.vo.UserAccountVO;
import com.neo.qiaoqiaochat.service.AuthService;
import com.neo.qiaoqiaochat.service.LoginService;
import com.neo.qiaoqiaochat.service.UserService;
import com.neo.qiaoqiaochat.util.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("auth")
public class AuthController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @Autowired
    private HttpServletRequest request;
        //注册


    /**
     * 登录
     * @return SimpleResult
     */
    @RequestMapping("login")
    public SimpleResult login() throws IOException {
            //验证登录成功以后 把用户账号 和token放入缓存中
        LoginDTO dto = getAndCheckParam(request.getInputStream(), LoginDTO.class);
        loginService.login(dto);
        loginService.doAfterLogin(dto.getAccount());
        return new SimpleResult();
    }

    /**
     * 获取用户token
     * @return SimpleResult
     */
    @RequestMapping("token")
    public SimpleResult getToken() throws IOException {
        //验证登录成功以后 把用户账号 和token放入缓存中
        //key-account  value-token
        SimpleResult<TokenVO> result = new SimpleResult<>();
        UserAccountVO user = ShiroUtils.getCurrentUser();
        TokenVO tokenVO = authService.getTokenVO(user.getAccount());
        result.setData(tokenVO);
        return result;
    }


    /**
     * 登出
     * @return SimpleResult
     * @throws IOException
     */
    @RequestMapping("logout")
    public SimpleResult logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return new SimpleResult();
    }

    }
