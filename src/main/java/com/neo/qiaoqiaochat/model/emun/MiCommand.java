package com.neo.qiaoqiaochat.model.emun;

/**
 * 消息命令类型
 * @author Neo Lin
 * @date  2020/4/11
 */
public enum  MiCommand {

    /**
     * 连接
     */
    CONNECT(0),
    /**
     * 关闭连接
     */
    CLOSE(1),
    /**
     * 心跳包
     */
    HEARTBEAT(2),
    /**
     * 上线
     */
    ONLINE(3),
    /**
     * 下线
     */
    OFFLINE(4),
    /**
     * 消息
     */
    MESSAGE(5);

    private final int code;

    MiCommand(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MiCommand getEnumByCode(int code) {
        for (MiCommand cmd : values()) {
            if (cmd.code == code) {
                return cmd;
            }
        }
        return null;
    }
}
