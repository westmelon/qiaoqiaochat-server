package com.neo.qiaoqiaochat.web.model;

/**
 * restful api 返回对象
 * @author Neo Lin
 * @date  2020/4/5
 */
public class SimpleResult<T> {

    private int code;

    private String message;

    private T data;

    public SimpleResult() {
        this.code = ResultCode.SUCCESS.getCode();
        this.message = ResultCode.SUCCESS.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setResultCode(ResultCode resultCode){
        this.code = resultCode.getCode();
        this.message = resultCode.getMsg();
    }
}
