package com.neo.qiaoqiaochat.session;


import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.proxy.MessageProxy;
import io.netty.channel.ChannelHandlerContext;
import com.neo.qiaoqiaochat.model.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NettySessionManager {

    protected Map<String, Session> sessions = new ConcurrentHashMap<>();  //todo 使用redis实现

    //用户账号与session列表对应关系
    protected Map<String, Set<String>> userSessionMapping = new ConcurrentHashMap<>();

    //添加session
    public synchronized void addSession(String sessionId, Session session) {
        sessions.put(sessionId, session);
    }

    //移除session
    public void removeSession(String sessionId) {
        Session session = getSession(sessionId);
        if (session != null) {

            session.getChannel().close(); //关闭通道
            sessions.remove(sessionId);
            String account = session.getAccount();
            Set<String> userSessions = userSessionMapping.get(account);
            if (!CollectionUtils.isEmpty(userSessions)) {
                userSessions.remove(sessionId);
            }
        }
    }

    //查找session
    public Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public List<Session> getSessions(List<String> sessionIds) {
        List<Session> sessions = new ArrayList<>();
        sessionIds.forEach(sessionId -> {
            Session session = getSession(sessionId);
            if(session != null){
                sessions.add(session);
            }
        });
        return sessions;
    }

    public Session createSession(MessageWrapper wrapper, ChannelHandlerContext ctx) {
        //TODO 生成一个会话id
//        String sender = message.getSender();
        Session session = new Session();
        session.setSessionId(UUID.randomUUID().toString());
        session.setChannel(ctx.channel());
        session.setCreateSessionTime(System.currentTimeMillis());
        return session;
    }

    //根据账号查找用户sessions
    public List<String> getSessionIdsByAccount(String account) {
        Set<String> userSessions = userSessionMapping.get(account);
        return CollectionUtils.isEmpty(userSessions) ? null : new ArrayList<>(userSessions);
    }

    //绑定账号和session列表 同步处理
    public synchronized void bindSessionIdsToAccount(String account, String sessionId) {
        Set<String> sessionIds = userSessionMapping.get(account);
        if (CollectionUtils.isEmpty(sessionIds)) {
            sessionIds = new HashSet<>();
        }
        sessionIds.add(sessionId);
        userSessionMapping.put(account, sessionIds);
    }

    public Map<String, Set<String>> getUserSessionMapping() {
        return userSessionMapping;
    }
}
