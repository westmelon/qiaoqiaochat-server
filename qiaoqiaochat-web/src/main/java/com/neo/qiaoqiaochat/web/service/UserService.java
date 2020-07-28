package com.neo.qiaoqiaochat.web.service;

import com.neo.qiaoqiaochat.web.model.domain.MiUserModel;
import com.neo.qiaoqiaochat.web.model.dto.UserRegisterDTO;
import com.neo.qiaoqiaochat.web.model.vo.UserAccountVO;

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

    MiUserModel getUserByAccount(String account);

    /**
     * 用户注册
     *
     * @param dto the dto
     */
    void userRegister(UserRegisterDTO dto);


}
