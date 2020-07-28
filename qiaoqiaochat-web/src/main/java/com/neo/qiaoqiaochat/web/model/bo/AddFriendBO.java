package com.neo.qiaoqiaochat.web.model.bo;


import javax.validation.constraints.NotEmpty;

public class AddFriendBO {

    /**
     * 添加账号.
     */
    @NotEmpty
    private String account;

    /**
     * 暗号.
     */
    private String secretSignal;

    /**
     * 添加者账号.
     */
    private String fromAccount;

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

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }
}
