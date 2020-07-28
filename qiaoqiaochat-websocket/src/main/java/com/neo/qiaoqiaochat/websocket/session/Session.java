package com.neo.qiaoqiaochat.websocket.session;

import io.netty.channel.Channel;

import java.io.Serializable;

public class Session implements Serializable {

    private static final long serialVersionUID = 3399518522675918499L;

    private String account;

    private String sessionId;

    private ClientInfo clientInfo;

    private transient Channel channel;

    private Long createSessionTime;


    public static class ClientInfo{
        private String type; //客户端类型
        private String version; //客户端版本

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public Long getCreateSessionTime() {
        return createSessionTime;
    }

    public void setCreateSessionTime(Long createSessionTime) {
        this.createSessionTime = createSessionTime;
    }
}
