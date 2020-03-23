package com.neo.qiaoqiaochat.handler;

import com.neo.qiaoqiaochat.connector.QConnector;
import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.protobuf.QiaoQiaoHua;
import com.neo.qiaoqiaochat.proxy.MessageProxy;
import com.neo.qiaoqiaochat.session.SessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class MessageWebSocketHandler extends SimpleChannelInboundHandler<QiaoQiaoHua.Model> {
    private final static Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private MessageProxy messageProxy;
    @Autowired
    private QConnector qConnector;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QiaoQiaoHua.Model model) throws Exception {

        String sessionId = qConnector.getChannelSessionId(ctx);
        MessageWrapper messageWapper = messageProxy.createMessageWapper(sessionId, model);
        receiveMessages(ctx, messageWapper);

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        logger.info("1111");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        logger.info("1111");

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.info("1111");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("1111");

    }

    private void receiveMessages(ChannelHandlerContext hander, MessageWrapper wrapper) {
        //设置消息来源为socket
        wrapper.setSource(QiaoqiaoConst.ServerConfig.SOCKET);
        if (wrapper.isConnect()) {
            qConnector.connect(hander, wrapper);
        } else if (wrapper.isClose()) {
            qConnector.close(hander);
        } else if (wrapper.isHeartbeat()) {
            qConnector.heartbeatToClient(hander, wrapper);
        } else if (wrapper.isGroup()) {
            qConnector.pushGroupMessage(wrapper);
        } else if (wrapper.isSend()) {
            qConnector.pushMessage(wrapper.getSessionId(), wrapper);
        } else if (wrapper.isSendReply()) {
            qConnector.pushMessage(wrapper);

        }
    }
}
