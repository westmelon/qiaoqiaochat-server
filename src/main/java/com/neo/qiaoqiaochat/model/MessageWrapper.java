package com.neo.qiaoqiaochat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * @Description: 消息包装类
 * @Author Neo Lin
 * @Date  2020/3/19 20:54
 */
public class MessageWrapper implements Serializable {

    private static final long serialVersionUID = 2399518522645918499L;
    private String sessionId;

    private List<String> reSessionIds;

    private int source;

    private Object body;

    private MessageProtocol protocol;

    public MessageWrapper(MessageProtocol protocol, String sessionId, String reSessionId, Object body) {
        this.reSessionIds = new ArrayList<>();
        this.protocol = protocol;
        this.sessionId = sessionId;
        this.reSessionIds.add(reSessionId);
        this.body = body;
    }

    public MessageWrapper(MessageProtocol protocol, String sessionId, List<String> reSessionIds, Object body) {
        this.reSessionIds = new ArrayList<>();
        this.protocol = protocol;
        this.sessionId = sessionId;
        if(reSessionIds != null && reSessionIds.size() > 0){
            this.reSessionIds.addAll(reSessionIds);
        }
        this.body = body;
    }

    public enum MessageProtocol {
        CONNECT, CLOSE, HEARTBEAT, SEND,SEND_REPLY, GROUP, NOTIFY, NOTIFY_REPLY, ON_LINE, OFF_LINE;
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<String> getReSessionIds() {
        return reSessionIds;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public boolean isConnect() {
        return MessageProtocol.CONNECT.equals(protocol);
    }

    public boolean isClose() {
        return MessageProtocol.CLOSE.equals(protocol);
    }

    public boolean isHeartbeat() {
        return MessageProtocol.HEARTBEAT.equals(protocol);
    }

    public boolean isSend() {
        return MessageProtocol.SEND.equals(protocol);
    }

    public boolean isSendReply(){
        return MessageProtocol.SEND_REPLY.equals(protocol);
    }

    public boolean isGroup() {
        return MessageProtocol.GROUP.equals(protocol);
    }

    public boolean isNotify() {
        return MessageProtocol.NOTIFY.equals(protocol);
    }

    public boolean isNotifyReply() {
        return MessageProtocol.NOTIFY_REPLY.equals(protocol);
    }

    public boolean isOnLine() {
        return MessageProtocol.ON_LINE.equals(protocol);
    }

    public boolean isOffLine() {
        return MessageProtocol.OFF_LINE.equals(protocol);
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}


