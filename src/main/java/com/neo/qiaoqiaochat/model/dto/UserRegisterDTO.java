package com.neo.qiaoqiaochat.model.dto;


import javax.validation.constraints.NotEmpty;

public class UserRegisterDTO {

    @NotEmpty(message = "账号不能为空")
    private String account;

    @NotEmpty(message = "昵称不能为空")
    private String nickName;

    private String sex;

    private String introduce;

    @NotEmpty(message = "密码不能为空")
    private String password;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
