package com.neo.qiaoqiaochat.model;

import io.netty.util.AttributeKey;

public class QiaoqiaoConst {


    public static interface ServerConfig {
        public static final int SERVER_CONNET_TIMEOUT = 10;
        public static final int WRITE_IDLE_TIMEOUT = 10;
        public static final int READ_IDLE_TIMEOUT = 10;
        public static final int ALL_IDLE_TIMEOUT = 20;
        // 最大协议包长度
        public static final int MAX_FRAME_LENGTH = 1024 * 10; // 10k

        public static final int SOCKET = 1;
    }

    public static interface SessionConfig {
        public static final String SESSION_KEY = "account";
        public static final String HEARTBEAT_KEY = "heartbeat";

        public static final AttributeKey<String> SERVER_SESSION_ID = AttributeKey.valueOf(SESSION_KEY);
        public static final AttributeKey<Long> SERVER_SESSION_HEARTBEAT = AttributeKey.valueOf(HEARTBEAT_KEY);

    }


    public interface FriendAction {
        int PASSED_FRIEND_REQUIRE = 0;  //通过好友请求
        int REFUSE_FRIEND_REQUIRE = 1;  //拒绝好友请求
        int BLACK_FRIEND_REQUIRE = 2;  //拉黑好友请求
    }

    public interface FriendRelationIndex {
        int WAITING = 0;  //待添加
        int GOOD_FRIEND = 1;  //好友关系
        int DELETE = 2;  //已删除
        int BLACK_FRIEND = 2;  //已拉黑
        int REFUSE = 3;  //被拒绝
    }

    public interface MessageReceiveSetting {
        int RECEIVE_AND_REMAIND = 0;  //接收并提示消息
        int RECEIVE_NOT_REMAIND = 1;  //接受但是不提示消息
        int REFUSE = 2;  //拒收消息
    }

    public interface MessageState {
        int NOT_SEND = 0; //未发送
        int SEND = 0;     //已发送
    }

    public interface MessageContentType {
        String ADD_FRIEND = "00";
        String PASSED_FRIEND_REQUIRE = "01";
        String REFUSE_FRIEND_REQUIRE = "02";
        String TEXT = "10";
        String VOICE = "11";
        String IMAGE = "12";
        String VIDEO = "13";
    }

    public static interface ShiroConfig {
        public static final String SALT = "2020520";
        public static final String USER_INFO = "ui";
    }

    public static interface RedisCacheConfig {
        public static final String NETTY_SESSION_NAMESPACE = "netty-session";
        public static final String ACCOUNT_SESSIONIDS = "account-session-ids";
        public static final String ACCOUNT_TOKEN = "mi-token";
    }
}
