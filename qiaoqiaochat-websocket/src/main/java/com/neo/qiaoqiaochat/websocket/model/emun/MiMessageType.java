package com.neo.qiaoqiaochat.websocket.model.emun;

public enum MiMessageType {

    /**
     * 发送消息
     */
    SEND(1),
    /**
     * 消息应答
     */
    SEND_REPLY(2),
    /**
     * 通知
     */
    NOTIFY(3),

    /**
     * 通知应答
     */
    NOTIFY_REPLY(4),
    /**
     * 添加好友
     */
    ADD_FRIEND(5),
    /**
     * 确认好友
     */
    CONFIRM_FRIEND(6),

    /**
     * 拒绝好友
     */
    REFUSE_FRIEND(7);

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
