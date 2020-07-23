package com.neo.qiaoqiaochat.model.dto;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 添加好友的实体类对象
 * @author Neo Lin
 * @date 2020/7/23 17:25
 */
@Data
public class AddFriendDTO {

    /**
     * 添加账号
     */
    @NotEmpty
    private String account;

    /**
     * 暗号
     */
    private String secretSignal;


}
