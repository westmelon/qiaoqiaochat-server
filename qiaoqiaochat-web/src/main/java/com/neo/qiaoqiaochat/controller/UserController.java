package com.neo.qiaoqiaochat.controller;

import com.neo.qiaoqiaochat.model.SimpleResult;
import com.neo.qiaoqiaochat.model.dto.AddFriendDTO;
import com.neo.qiaoqiaochat.model.dto.ConfirmFriendDTO;
import com.neo.qiaoqiaochat.model.dto.UserRegisterDTO;
import com.neo.qiaoqiaochat.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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


    /**
     * 添加好友
     * @author linyi
     * @date 2020/7/23 17:49
     * @param dto the dto
     * @return
     */
    @PostMapping(value = "friend/add")
    public SimpleResult addFriend(@Valid @RequestBody AddFriendDTO dto)  {
        userService.addFriend(dto);
        SimpleResult result = new SimpleResult();
        result.setData("反對反對");
        return result;
    }

    /**
     * 添加好友确认
     * @author linyi
     * @date 2020/7/23 17:48
     * @param dto the dto
     * @return
     */
    @PostMapping("friend/confirm")
    public SimpleResult confirmFriend(@Valid @RequestBody ConfirmFriendDTO dto) {
        userService.confirmAddFriend(dto);
        return new SimpleResult();
    }

   /**
    * 获取好友列表
    * @author linyi
    * @date 2020/7/23 17:49
    * @return
    */
    @PostMapping("friend/list")
    public SimpleResult getFriendList(){
        SimpleResult<List> result = new SimpleResult<>();
        result.setData(userService.getFriendList());
        return result;
    }


}
