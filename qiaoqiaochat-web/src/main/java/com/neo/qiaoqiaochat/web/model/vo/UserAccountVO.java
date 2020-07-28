package com.neo.qiaoqiaochat.web.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"id", "password"})
public class UserAccountVO {

    private Long id;

    private String account;

//    private String openid;

    private String nickName;

    private String password;

    private String headImageIndex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadImageIndex() {
        return headImageIndex;
    }

    public void setHeadImageIndex(String headImageIndex) {
        this.headImageIndex = headImageIndex;
    }
}
