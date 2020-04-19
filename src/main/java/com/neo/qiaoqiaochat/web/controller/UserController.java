package com.neo.qiaoqiaochat.web.controller;

import com.neo.commons.utils.BaseController;
import com.neo.qiaoqiaochat.model.SimpleResult;
import com.neo.qiaoqiaochat.model.dto.AddFriendDTO;
import com.neo.qiaoqiaochat.model.dto.ConfirmFriendDTO;
import com.neo.qiaoqiaochat.model.dto.UserRegisterDTO;
import com.neo.qiaoqiaochat.service.impl.UserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserServiceImpl userService;

    private final HttpServletRequest request;

    @Autowired
    public UserController(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * showdoc
     * @catalog 接口API/用户相关
     * @title 用户注册
     * @description 用户注册的接口
     * @method post
     * @url https://xx/user/friend/add
     * @param account 必选 string 账号
     * @param nickName 必选 string 昵称
     * @param sex 非必选 string 性别
     * @param introduce 非必选 string 介绍
     * @param password 必选 string 密码
     *
     * @remark 这里是备注信息
     * @number 1
     */
    @RequestMapping("register")
    public SimpleResult register(@Valid @RequestBody UserRegisterDTO dto ){
        userService.userRegister(dto);
        return new SimpleResult();
    }



    /**
     * showdoc
     * @catalog 接口API/用户相关
     * @title 添加好友
     * @description 添加好友的接口
     * @method post
     * @url https://xx/user/friend/add
     * @param account 必选 string 添加的账号
     * @param secretSignal 必选 string 验证消息
     *
     * @remark 这里是备注信息
     * @number 2
     */
    @RequestMapping(value = "friend/add")
    public SimpleResult addFriend(@Valid @RequestBody AddFriendDTO dto)  {
        userService.addFriend(dto);
        SimpleResult result = new SimpleResult();
        result.setData("反對反對");
        return result;
    }

    /**
     * showdoc
     * @catalog 接口API/用户相关
     * @title 添加好友确认
     * @description 添加好友确认的接口
     * @method post
     * @url https://xx/user/friend/confirm
     * @param account 必选 string 账号
     * @param remark 非必选 string 备注
     *
     * @remark 这里是备注信息
     * @number 3
     */
    @RequestMapping("friend/confirm")
    public SimpleResult confirmFriend(@Valid @RequestBody ConfirmFriendDTO dto) {
        userService.confirmAddFriend(dto);
        return new SimpleResult();
    }

    /**
     * showdoc
     * @catalog 接口API/用户相关
     * @title 获取好友列表
     * @description 获取好友列表的接口
     * @method post
     * @url https://xx/user/friend/confirm
     * @return_param  account string 账号
     * @return_param  nickName string 昵称
     * @return_param  remark  string 好友备注
     * @return_param  headImageIndex  string 头像地址
     * @return_param  relationIndex  int 好友关系
     * @return_param  msgReceiveSetting  int 消息接收策略
     *
     * @remark 这里是备注信息
     * @number 4
     */
    @RequestMapping("friend/list")
    public SimpleResult getFriendList(){
        SimpleResult<List> result = new SimpleResult<>();
        result.setData(userService.getFriendList());
        return result;
    }


}
