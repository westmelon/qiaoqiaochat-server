package com.neo.qiaoqiaochat.web.model.dto;

import javax.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "账号不能为空")
    private String account;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    private String password;
}
