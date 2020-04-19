package com.neo.qiaoqiaochat.model.emun;

public enum MiMessageType {

    /**
     * 发送消息
     */
    SEND(0),
    /**
     * 消息应答
     */
    SEND_REPLY(1),
    /**
     * 通知
     */
    NOTIFY(2),

    /**
     * 通知应答
     */
    NOTIFY_REPLY(3),
    /**
     * 添加好友
     */
    ADD_FRIEND(4),
    /**
     * 确认好友
     */
    CONFIRM_FRIEND(5),

    /**
     * 拒绝好友
     */
    REFUSE_FRIEND(6);

    private final int code;

    MiMessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MiMessageType getEnumByCode(int code) {
        for (MiMessageType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
