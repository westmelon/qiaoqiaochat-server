package com.neo.qiaoqiaochat.session;


import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.proxy.MessageProxy;
import io.netty.channel.ChannelHandlerContext;
import com.neo.qiaoqiaochat.model.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    protected Map<String, Session> sessions = new ConcurrentHashMap<>();  //todo 使用redis实现
    
    protected Map<String, List<String>> userSessionMapping = new ConcurrentHashMap<>();  //用户账号与session列表对应关系

    //添加session
    public synchronized void addSession(String sessionId, Session session){
        sessions.put(sessionId, session);
    }

    //移除session
    public void removeSession(String sessionId){
        Session session = getSession(sessionId);
        if(session != null){

            session.getChannel().close(); //关闭通道
            sessions.remove(sessionId);
            String account = session.getAccount();
            List<String> userSessions = userSessionMapping.get(account);
            if(!CollectionUtils.isEmpty(userSessions)){
                userSessions.remove(sessionId);
            }
        }
    }

    //查找session
    public Session getSession(String sessionId){
        return sessions.get(sessionId);
    }

    public Session createSession(MessageWrapper wrapper, ChannelHandlerContext ctx){
        //TODO 生成一个会话id
//        String sender = message.getSender();
        Session session = new Session();
        session.setSessionId(UUID.randomUUID().toString());
        session.setChannel(ctx.channel());
        return session;
    }
    
    //根据账号查找用户sessions
    public List<String> getSessionIdsByAccount(String account){
        return userSessionMapping.get(account);
    }
}
