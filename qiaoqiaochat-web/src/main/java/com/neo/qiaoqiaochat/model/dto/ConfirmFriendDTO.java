package com.neo.qiaoqiaochat.model.dto;


import lombok.Data;


/**
 * 好友确认添加
 * @author Neo Lin
 * @date 2020/7/23 17:25
 */
@Data
public class ConfirmFriendDTO {

    /**
     * 操作的账号
     */
    private String account;

    /**
     * 备注信息
     */
    private String remark;


    /**
     * TODO 选择的操作 0通过 1拒绝 2拉黑.
     */
    private Integer action;
}
