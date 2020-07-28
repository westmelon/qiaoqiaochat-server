package com.neo.qiaoqiaochat.websocket.handler;

import com.neo.qiaoqiaochat.websocket.connector.QConnector;
import com.neo.qiaoqiaochat.websocket.proxy.MessageProxy;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.neo.qiaoqiaochat.websocket.session.NettySessionManager;
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



    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {   //心跳包相关
//        super.userEventTriggered(ctx, evt);
//        logger.info("nonono");
//        String sessionId = ctx.channel().attr(QiaoqiaoConst.SessionConfig.SERVER_SESSION_ID).get();
//
//        if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.WRITER_IDLE)) {  //send heartBEAT
//            ctx.channel().writeAndFlush("dididadida");
//        }
//
//        if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.READER_IDLE)) {
//
//            Long lastHeartBeat = ctx.channel().attr(QiaoqiaoConst.SessionConfig.SERVER_SESSION_HEARTBEAT).get();
//
//            long currentTimeMillis = System.currentTimeMillis();
//
//            if (lastHeartBeat == null || (currentTimeMillis - lastHeartBeat) / 1000 > QiaoqiaoConst.ServerConfig.SERVER_CONNET_TIMEOUT) { //关闭通道
//                logger.info("close session");
////                ctx.channel().close(); //todo session相关操作
//            }
//        }
//
//    }

}
