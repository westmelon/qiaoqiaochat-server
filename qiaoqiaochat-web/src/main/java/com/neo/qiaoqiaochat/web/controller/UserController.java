package com.neo.qiaoqiaochat.web.controller;

import com.neo.core.entity.SimpleResult;
import com.neo.qiaoqiaochat.web.model.dto.UserRegisterDTO;
import com.neo.qiaoqiaochat.web.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
* 用户相关
* @author linyi
* @date 2020/7/23 14:42
*/
@RestController
@RequestMapping("user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserServiceImpl userService;

    private final HttpServletRequest request;

    @Autowired
    public UserController(HttpServletRequest request) {
        this.request = request;
    }


    /**
     * 用户注册
     * @author linyi
     * @date 2020/7/23 17:47
     * @param dto the dto
     * @return
     */
    @PostMapping("register")
    public SimpleResult register(@Valid @RequestBody UserRegisterDTO dto ){
        userService.userRegister(dto);
        return new SimpleResult();
    }



}
