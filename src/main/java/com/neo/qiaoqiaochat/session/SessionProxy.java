package com.neo.qiaoqiaochat.session;

import io.netty.channel.Channel;

/*
 * @Description: session代理对象
 * @Author Neo Lin
 * @Date  2020/3/22 14:32
 */
public class SessionProxy {

    private Session session;

    public SessionProxy(Session session) {
        this.session = session;
    }


    public boolean write(Object msg) {
        if (isConnect()){
            Channel channel = session.getChannel();
            return channel.writeAndFlush(msg).awaitUninterruptibly(5000);
        }
        return false;
    }

    //判断channel是否在线
    public boolean isConnect() {
        if (session != null && session.getChannel() != null)
            return session.getChannel().isActive();
        return false;
    }

}
