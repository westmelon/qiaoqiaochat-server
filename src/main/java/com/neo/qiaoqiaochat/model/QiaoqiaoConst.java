package com.neo.qiaoqiaochat.model;

import io.netty.util.AttributeKey;

public class QiaoqiaoConst {


    public static interface ServerConfig{
        public static final int SERVER_CONNET_TIMEOUT = 10;
        public static final int WRITE_IDLE_TIMEOUT = 10;
        public static final int READ_IDLE_TIMEOUT = 10;
        public static final int ALL_IDLE_TIMEOUT = 20;
        // 最大协议包长度
        public static final int MAX_FRAME_LENGTH = 1024 * 10; // 10k

        public static final int SOCKET = 1;
    }

    public static interface SessionConfig{
        public static final String SESSION_KEY = "account";
        public static final String HEARTBEAT_KEY = "heartbeat";

        public static final AttributeKey<String> SERVER_SESSION_ID = AttributeKey.valueOf(SESSION_KEY);
        public static final AttributeKey<Long> SERVER_SESSION_HEARTBEAT = AttributeKey.valueOf(HEARTBEAT_KEY);

    }

    public static interface CommandLine{
        public static final int BIND = 0;
        public static final int CLOSE = 1;
        public static final int HEARTBEAT = 2;
        public static final int ONLINE = 3;
        public static final int OFFLINE = 4;
        public static final int MESSAGE = 5;
    }

    public static interface MessageType {
        public static final int SEND = 0;
        public static final int SEND_REPLY = 1;
        public static final int NOTIFY = 2;
        public static final int NOTIFY_REPLY = 3;

    }
}
