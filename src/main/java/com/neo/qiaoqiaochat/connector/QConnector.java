package com.neo.qiaoqiaochat.connector;

import com.neo.qiaoqiaochat.model.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

public interface QConnector {


    /**
     * 接受客户端的心跳包 刷新时间
     * @param hander
     * @param wrapper
     */
    void heartbeatFromClient(ChannelHandlerContext hander, MessageWrapper wrapper);


    /**
     * 向连接的客户端发送心跳包
     * @param hander
     */
    void heartbeatToClient(ChannelHandlerContext hander);

    //发送消息（消息回传）
    void pushMessage(MessageWrapper wrapper) throws RuntimeException;

    //给指定用户发送消息
    void pushMessage(String sessionId, MessageWrapper wrapper) throws RuntimeException;

    //发送组消息
    void pushGroupMessage(MessageWrapper wrapper) throws RuntimeException;

    //关闭连接

    void close(String sessionId);

    void close(ChannelHandlerContext hander);


    //    创建连接
    void connect(ChannelHandlerContext ctx, MessageWrapper wrapper);

    //获取channel的sessionid
    String getChannelSessionId(ChannelHandlerContext ctx);


}
