package com.neo.qiaoqiaochat.web.controller;

import com.neo.qiaoqiaochat.web.auth.UserAuth;
import com.neo.qiaoqiaochat.web.model.SimpleResult;
import com.neo.qiaoqiaochat.web.model.dto.AddFriendDTO;
import com.neo.qiaoqiaochat.web.model.dto.ConfirmFriendDTO;
import com.neo.qiaoqiaochat.web.model.dto.SearchUserDTO;
import com.neo.qiaoqiaochat.web.model.vo.ContactVO;
import com.neo.qiaoqiaochat.web.model.vo.SearchUserVO;
import com.neo.qiaoqiaochat.web.service.FriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 好友相关接口
 *
 * @author linyi
 * @date 2020/7/27 15:52
 */
@RestController
@RequestMapping("friend")
public class FriendController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FriendService friendService;

    /**
     * 根据账号查询用户信息
     *
     * @param dto the dto
     * @return
     * @author linyi
     * @date 2020/7/27 15:52
     * @since 0.0.1
     */
    @PostMapping("search")
    public SimpleResult<SearchUserVO> login(@RequestBody @Valid SearchUserDTO dto) {
        SimpleResult<SearchUserVO> simpleResult = new SimpleResult<>();
        SearchUserVO vo = friendService.searchUserForWeb(dto);
        simpleResult.setData(vo);
        return simpleResult;
    }


    /**
     * 添加好友
     *
     * @param dto the dto
     * @return
     * @author linyi
     * @date 2020/7/23 17:49
     */
    @PostMapping(value = "add")
    public SimpleResult<Void> addFriend(@Valid @RequestBody AddFriendDTO dto) {

        SimpleResult<Void> result = new SimpleResult<>();
        friendService.addFriend(dto);
        return result;
    }

    /**
     * 处理好友添加请求
     *
     * @param dto the dto
     * @return
     * @author linyi
     * @date 2020/7/27 16:15
     * @since 0.0.1
     */
    @PostMapping("confirm")
    public SimpleResult<Void> confirmFriend(@Valid @RequestBody ConfirmFriendDTO dto) {
        friendService.confirmAddFriend(dto);
        return new SimpleResult<Void>();
    }

    /**
     * 获取好友列表
     *
     * @return
     * @author linyi
     * @date 2020/7/27 16:14
     * @since 0.0.1
     */
    @PostMapping("list")
    @UserAuth
    public SimpleResult<ContactVO> getFriendList() {
        SimpleResult<ContactVO> result = new SimpleResult<>();
        result.setData(friendService.getFriendList());
        return result;
    }

    //添加好友请求处理 （通过好友请求后推送一条打招呼信息）
}