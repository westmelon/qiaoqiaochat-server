package com.neo.qiaoqiaochat.service;

import com.neo.qiaoqiaochat.model.dto.AddFriendDTO;
import com.neo.qiaoqiaochat.model.dto.ConfirmFriendDTO;
import com.neo.qiaoqiaochat.model.dto.UserRegisterDTO;
import com.neo.qiaoqiaochat.model.vo.FriendVO;
import com.neo.qiaoqiaochat.model.vo.UserAccountVO;

import java.util.List;

/**
 * The interface User service.
 */
public interface UserService {
    /**
     * 根据账号查找用户信息
     *
     * @param account the account
     * @return the user account vo
     */
    UserAccountVO findUserByAccount(String account);

    /**
     * Find user by account for api user account vo.
     *
     * @param account the account
     * @return the user account vo
     */
    UserAccountVO findUserByAccountForApi(String account);

    /**
     * 根据账号查询用户id
     *
     * @param account the account
     * @return the user id by account
     */
    Long getUserIdByAccount(String account);

    /**
     * 用户注册
     *
     * @param dto the dto
     */
    void userRegister(UserRegisterDTO dto);

    /**
     * 添加好友
     *
     * @param dto the dto
     */
    void addFriend(AddFriendDTO dto);

    /**
     * 确认添加好友
     *
     * @param dto the dto
     */
    void confirmAddFriend(ConfirmFriendDTO dto);

    List<FriendVO> getFriendList();
}
