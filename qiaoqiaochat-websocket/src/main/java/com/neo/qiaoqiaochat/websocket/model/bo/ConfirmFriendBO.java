package com.neo.qiaoqiaochat.websocket.model.bo;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 确认添加好友业务对象.
 * @author doudou
 */
public class ConfirmFriendBO {

    /**
     * 操作的账号.
     */
    @NotEmpty
    private String account;

    /**
     * 操作发起人账号.
     */
    private String fromAccount;

    /**
     * 备注信息
     */
    private String remark;


    /**
     * TODO 选择的操作 0通过 1拒绝 2拉黑.
     */
    @NotNull
    private Integer action;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
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
