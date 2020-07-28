package com.neo.qiaoqiaochat.websocket.service;

/**
 * The interface User service.
 */
public interface UserService {

    /**
     * 根据账号查询用户id
     *
     * @param account the account
     * @return the user id by account
     */
    Long getUserIdByAccount(String account);

}
