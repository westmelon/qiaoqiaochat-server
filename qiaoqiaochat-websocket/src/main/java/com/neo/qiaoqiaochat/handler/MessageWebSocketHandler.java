package com.neo.qiaoqiaochat.handler;

import com.neo.qiaoqiaochat.connector.QConnector;
import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.protobuf.QiaoQiaoHua;
import com.neo.qiaoqiaochat.proxy.MessageProxy;
import com.neo.qiaoqiaochat.session.NettySessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ChannelHandler.Sharable
public class MessageWebSocketHandler extends SimpleChannelInboundHandler<QiaoQiaoHua.Model> {
    private final static Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    @Autowired
    private NettySessionManager nettySessionManager;
    @Autowired
    private MessageProxy messageProxy;
    @Autowired
    private QConnector qConnector;

    /**
     * 当客户端不再发送心跳包时触发事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //获取sessionId
        String sessionId = ctx.channel().attr(QiaoqiaoConst.SessionConfig.SERVER_SESSION_ID).get();
        //当服务端一段时间没有向客户端发送消息时 向客户端发送心跳包
        if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.WRITER_IDLE)) {
            qConnector.heartbeatToClient(ctx);
        }

        //当服务端一段时间内没有接受到客户端的反馈消息时
        if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.READER_IDLE)) {

            Long lastHeartBeat = ctx.channel().attr(QiaoqiaoConst.SessionConfig.SERVER_SESSION_HEARTBEAT).get();

            long currentTimeMillis = System.currentTimeMillis();

            if (lastHeartBeat == null || (currentTimeMillis - lastHeartBeat) / 1000 > QiaoqiaoConst.ServerConfig.SERVER_CONNET_TIMEOUT) {
                //关闭session 移除缓存
                logger.info("close session");
                qConnector.close(ctx);
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QiaoQiaoHua.Model model) throws Exception {

        String sessionId = qConnector.getChannelSessionId(ctx);
        MessageWrapper messageWrapper = messageProxy.createMessageWapper(sessionId, model);
        receiveMessages(ctx, messageWrapper);

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        logger.info("channel register!");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        logger.info("channel unregister!");

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.info("channel active!");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("channel inactive!");

    }

    private void receiveMessages(ChannelHandlerContext ctx, MessageWrapper messageWrapper) {
        //设置消息来源为socket
        Optional<MessageWrapper> optional = Optional.ofNullable(messageWrapper);

        optional.ifPresent(wrapper ->{
            //只要接收到一次消息就更新心跳事件
            ctx.channel().attr(QiaoqiaoConst.SessionConfig.SERVER_SESSION_HEARTBEAT).set(System.currentTimeMillis());
            wrapper.setSource(QiaoqiaoConst.ServerConfig.SOCKET);
            if (wrapper.isConnect()) {
                qConnector.connect(ctx, wrapper);
            } else if (wrapper.isClose()) {
                qConnector.close(ctx);
            } else if (wrapper.isHeartbeat()) {
                logger.info("heartbeat!!!");
                qConnector.heartbeatFromClient(ctx, wrapper);
            } else if (wrapper.isGroup()) {
                qConnector.pushGroupMessage(wrapper);
            } else if (wrapper.isSend()) {
                //服务器转发消息给另一个用户
                qConnector.serverPushMessage(wrapper);
                //发送该条消息的响应报文 告诉发送者消息发送成功
                qConnector.replyMessageFromClient(ctx, wrapper);
            } else if (wrapper.isSendReply()) {
                //客户端接收应答
//                qConnector.pushMessage(wrapper);
                //todo
                logger.info("接收到客户端应答消息");
            }
        });

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
    }
}
