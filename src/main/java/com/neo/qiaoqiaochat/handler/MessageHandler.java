package com.neo.qiaoqiaochat.handler;

import com.neo.qiaoqiaochat.connector.QConnector;
import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.protobuf.QiaoQiaoHua;
import com.neo.qiaoqiaochat.proxy.MessageProxy;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.session.NettySessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class MessageHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    @Autowired
    private NettySessionManager nettySessionManager;
    @Autowired
    private MessageProxy messageProxy;
    @Autowired
    private QConnector qConnector;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof QiaoQiaoHua.Model){
            String sessionId = qConnector.getChannelSessionId(ctx);
            QiaoQiaoHua.Model qiaoqiaohua = (QiaoQiaoHua.Model) msg;
            MessageWrapper messageWapper = messageProxy.createMessageWapper(sessionId, qiaoqiaohua);
            receiveMessages(ctx, messageWapper);
        }


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {   //心跳包相关
        super.userEventTriggered(ctx, evt);
        logger.info("nonono");
        String sessionId = ctx.channel().attr(QiaoqiaoConst.SessionConfig.SERVER_SESSION_ID).get();

        if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.WRITER_IDLE)) {  //send heartBEAT
            ctx.channel().writeAndFlush("dididadida");
        }

        if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.READER_IDLE)) {

            Long lastHeartBeat = ctx.channel().attr(QiaoqiaoConst.SessionConfig.SERVER_SESSION_HEARTBEAT).get();

            long currentTimeMillis = System.currentTimeMillis();

            if (lastHeartBeat == null || (currentTimeMillis - lastHeartBeat) / 1000 > QiaoqiaoConst.ServerConfig.SERVER_CONNET_TIMEOUT) { //关闭通道
                logger.info("close session");
//                ctx.channel().close(); //todo session相关操作
            }
        }

    }

    private void receiveMessages(ChannelHandlerContext hander, MessageWrapper wrapper) {
        //设置消息来源为socket
        wrapper.setSource(QiaoqiaoConst.ServerConfig.SOCKET);
        if (wrapper.isConnect()) {
            qConnector.connect(hander, wrapper);
        } else if (wrapper.isClose()) {
            qConnector.close(hander);
        } else if (wrapper.isHeartbeat()) {
            qConnector.heartbeatToClient(hander,wrapper);
        }else if (wrapper.isGroup()) {
            qConnector.pushGroupMessage(wrapper);
        }else if (wrapper.isSend()) {
            qConnector.pushMessage(wrapper.getSessionId(),wrapper);
        } else if (wrapper.isSendReply()) {
            qConnector.pushMessage(wrapper);

        }
    }
}
