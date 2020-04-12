package com.neo.qiaoqiaochat.web.controller;

import com.neo.commons.utils.BaseController;
import com.neo.qiaoqiaochat.model.SimpleResult;
import com.neo.qiaoqiaochat.model.dto.UserRegisterDTO;
import com.neo.qiaoqiaochat.service.LoginService;
import com.neo.qiaoqiaochat.service.UserService;
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
@RequestMapping("user")
public class UserController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    private final HttpServletRequest request;

    @Autowired
    public UserController(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 注册
     * @return SimpleResult
     */
    @RequestMapping("register")
    public SimpleResult register() throws IOException {
        UserRegisterDTO dto = getAndCheckParam(request.getInputStream(), UserRegisterDTO.class);
        userService.userRegister(dto);
        return new SimpleResult();
    }

    //添加好友

    /**
     * test
     * @return SimpleResult
     */
    @RequestMapping("test")
    public SimpleResult test() throws IOException {
        String label = (String) SecurityUtils.getSubject().getSession().getAttribute("label");
        SimpleResult simpleResult = new SimpleResult();
        simpleResult.setData(label);
        return simpleResult;
    }
}
