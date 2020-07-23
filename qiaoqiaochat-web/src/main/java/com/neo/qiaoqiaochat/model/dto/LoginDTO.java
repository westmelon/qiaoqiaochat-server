package com.neo.qiaoqiaochat.model.dto;

import lombok.Data;

/**
 * 用户登录请求参数
 * @author Neo Lin
 * @date 2020 /7/23 17:24
 */
@Data
public class LoginDTO {

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;
}
