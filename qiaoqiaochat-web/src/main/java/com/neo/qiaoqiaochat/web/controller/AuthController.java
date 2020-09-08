package com.neo.qiaoqiaochat.web.controller;


import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.neo.qiaoqiaochat.web.model.SimpleResult;
import com.neo.qiaoqiaochat.web.model.dto.LoginDTO;
import com.neo.qiaoqiaochat.web.model.vo.TokenVO;
import com.neo.qiaoqiaochat.web.model.vo.UserAccountVO;
import com.neo.qiaoqiaochat.web.service.impl.AuthService;
import com.neo.qiaoqiaochat.web.service.impl.LoginService;
import com.neo.qiaoqiaochat.web.util.UserUtils;

/**
 * 权限相关
 *
 * @author linyi
 * @date 2020/7/23 14:39
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoginService loginService;

    @Autowired
    private AuthService authService;


    /**
     * 登录
     *
     * @param dto
     * @return SimpleResult
     */
    @PostMapping("login")
    public SimpleResult<TokenVO> login(@RequestBody @Valid LoginDTO dto) {
        //验证登录成功以后 把用户账号 和token放入缓存中
        loginService.login(dto);
        TokenVO tokenVO = loginService.doAfterLogin(dto.getAccount());
        SimpleResult<TokenVO> simpleResult = new SimpleResult<>();
        simpleResult.setData(tokenVO);
        return simpleResult;
    }

    /**
     * 获取用户token
     *
     * @return SimpleResult
     */
    @PostMapping(value = "token")
    public SimpleResult<TokenVO> getToken() {
        //验证登录成功以后 把用户账号 和token放入缓存中
        //key-account  value-token
        SimpleResult<TokenVO> result = new SimpleResult<>();
        UserAccountVO user = UserUtils.getAccount();
        TokenVO tokenVO = authService.getTokenVO(user.getAccount());
        result.setData(tokenVO);
        return result;
    }


    /**
     * 登出
     *
     * @return SimpleResult
     */
    @PostMapping("logout")
    public SimpleResult logout() {
        //todo
//        Subject currentUser = SecurityUtils.getSubject();
//        currentUser.logout();

        return new SimpleResult();
    }

}
