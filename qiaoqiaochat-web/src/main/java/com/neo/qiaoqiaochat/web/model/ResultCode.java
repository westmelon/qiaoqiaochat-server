package com.neo.qiaoqiaochat.web.model;

public enum ResultCode {
    //SUCCESS
    SUCCESS(0, "success"),

    //资源类
    NO_LOGIN(401, "未登录"),
    ACCESS_DENIED(403, "禁止访问"),
    PERMISSION_DENIED(405, "无访问权限"),
    //系统错误代码
    SERVICE_ERROR(500, "系统错误"),
    //参数类
    REQ_DATA_ERROR(601, "请求参数错误"),
    REQ_FORMAT_ERROR(602, "请求参数格式错误"),

    ACCOUNT_NOT_FOUND(700,"用户账号不存在"),
    PASSWORD_NOT_MATCH(701,"账号或密码错误"),
    PASSWORD_TRY_TOO_MUCH_TIME(702,"密码错误错误次数过多，请稍后再试"),
    REGIST_ERROR_ACCOUNT_EXISTS(750,"该账号已存在"),
    YOU_ARE_ALREADY_FRIEND(751,"你们已经是好友啦！")

    ;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
