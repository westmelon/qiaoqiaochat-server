package com.neo.qiaoqiaochat.session;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

/*
 * @Description: session代理对象
 * @Author Neo Lin
 * @Date  2020/3/22 14:32
 */
public class SessionProxy {

    private List<Session> sessions;

    public SessionProxy(Session session) {
        this.sessions = new ArrayList<>();
        sessions.add(session);
    }

    public SessionProxy(List<Session> sessions) {
        this.sessions = new ArrayList<>();
        this.sessions.addAll(sessions);
    }


    public boolean write(Object msg) {
        boolean hasSuccess = false;
        for(Session session : sessions){
            if (isConnect(session)) {
                Channel channel = session.getChannel();
                boolean b = channel.writeAndFlush(msg).awaitUninterruptibly(5000);
                hasSuccess |= b;
            }
        }
        return hasSuccess;
    }

    //判断channel是否在线
    public boolean isConnect(Session session) {
        if (session != null && session.getChannel() != null) {
            return session.getChannel().isActive();
        }
        return false;
    }

}
