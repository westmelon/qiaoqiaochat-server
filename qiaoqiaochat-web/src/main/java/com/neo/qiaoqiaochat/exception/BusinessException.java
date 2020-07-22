package com.neo.qiaoqiaochat.exception;

import com.neo.qiaoqiaochat.model.ResultCode;

public class BusinessException extends RuntimeException {

    private final int code;

    private final String msg;

    private final String subMsg;


    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.subMsg = "";
    }

    public BusinessException(int code, String msg, String subMsg) {
        super(msg + " " + subMsg);
        this.code = code;
        this.msg = msg;
        this.subMsg = subMsg;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.subMsg = "";
    }
    public BusinessException(ResultCode resultCode, String subMsg) {
        super(resultCode.getMsg() + " " + subMsg);
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.subMsg = subMsg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getSubMsg() {
        return subMsg;
    }
}
