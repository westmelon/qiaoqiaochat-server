package com.neo.qiaoqiaochat.model.dto;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 用户注册请求参数
 *
 * @author Neo Lin
 * @date 2020 /7/23 17:21
 */
@Data
public class UserRegisterDTO {

    /**
     * 账号
     */
    @NotEmpty(message = "账号不能为空")
    private String account;

    /**
     * 昵称
     */
    @NotEmpty(message = "昵称不能为空")
    private String nickName;

    /**
     * 简介
     */
    private String introduce;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    private String password;

}
