package com.neo.qiaoqiaochat.model.dto;


/**
 * 好友确认添加
 */
public class ConfirmFriendDTO {


    /**
     * 操作的账号.
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }
}
