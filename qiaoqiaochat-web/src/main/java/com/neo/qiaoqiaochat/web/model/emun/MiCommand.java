package com.neo.qiaoqiaochat.web.model.emun;

/**
 * 消息命令类型
 * @author Neo Lin
 * @date  2020/4/11
 */
public enum  MiCommand {

    /**
     * 连接
     */
    CONNECT(1),
    /**
     * 关闭连接
     */
    CLOSE(2),
    /**
     * 心跳包
     */
    HEARTBEAT(3),
    /**
     * 上线
     */
    ONLINE(4),
    /**
     * 下线
     */
    OFFLINE(5),
    /**
     * 消息
     */
    MESSAGE(6),
    /**
     * 回应
     */
    ECHO(7);

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
