package com.neo.qiaoqiaochat.connector;

import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.bo.AddFriendBO;
import com.neo.qiaoqiaochat.model.bo.ConfirmFriendBO;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * The interface Q connector.
 */
public interface QConnector {


    /**
     * 接受客户端的心跳包 刷新时间
     *
     * @param hander  the hander
     * @param wrapper the wrapper
     */
    void heartbeatFromClient(ChannelHandlerContext hander, MessageWrapper wrapper);


    /**
     * 向连接的客户端发送心跳包
     *
     * @param hander the hander
     */
    void heartbeatToClient(ChannelHandlerContext hander);

    /**
     * 发送消息（消息回传）.
     *
     * @param wrapper the wrapper
     */
    void pushMessage(MessageWrapper wrapper);



    /**
     * 服务端发送消息
     *
     * @param wrapper   the wrapper
     */
    void serverPushMessage(MessageWrapper wrapper);

    /**
     * 发送请求添加好友的消息
     *
     * @param bo 添加好友业务对象
     * @return 消息是否推送成功
     */
    void pushAddFriendMessage(AddFriendBO bo);

    /**
     * 发送添加好友处理结果
     *
     * @param bo 添加好友反馈业务对象
     * @return 消息是否推送成功
     */
    void pushConfirmFriendMessage(ConfirmFriendBO bo);

    /**
     * 发送组消息
     *
     * @param wrapper the wrappe
     */
    void pushGroupMessage(MessageWrapper wrapper);

    //关闭连接

    /**
     * Close.
     *
     * @param sessionId the session id
     */
    void close(String sessionId);

    /**
     * Close.
     *
     * @param hander the hander
     */
    void close(ChannelHandlerContext hander);


    /**
     * 创建连接.
     *
     * @param ctx     the ctx
     * @param wrapper the wrapper
     */
    void connect(ChannelHandlerContext ctx, MessageWrapper wrapper);

    /**
     * 获取channel的sessionid
     *
     * @param ctx the ctx
     * @return the channel session id
     */
    String getChannelSessionId(ChannelHandlerContext ctx);


}
