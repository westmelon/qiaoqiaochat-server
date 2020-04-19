package com.neo.qiaoqiaochat.model.dto;


import com.neo.commons.annotation.NotEmpty;

/**
 * 添加好友的实体类对象
 * @author doudou
 */
public class AddFriendDTO {

    /**
     * 添加账号.
     */
    @NotEmpty
    private String account;

    /**
     * 暗号.
     */
    private String secretSignal;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSecretSignal() {
        return secretSignal;
    }

    public void setSecretSignal(String secretSignal) {
        this.secretSignal = secretSignal;
    }
}
