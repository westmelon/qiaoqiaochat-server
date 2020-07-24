package com.neo.qiaoqiaochat.connector;

import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.bo.AddFriendBO;
import com.neo.qiaoqiaochat.model.bo.ConfirmFriendBO;
import com.neo.qiaoqiaochat.model.emun.MiCommand;
import com.neo.qiaoqiaochat.model.emun.MiMessageType;
import com.neo.qiaoqiaochat.model.protobuf.QiaoQiaoHua;
import com.neo.qiaoqiaochat.model.vo.TokenVO;
import com.neo.qiaoqiaochat.proxy.MessageProxy;
import com.neo.qiaoqiaochat.service.impl.AuthService;
import com.neo.qiaoqiaochat.session.Session;
import com.neo.qiaoqiaochat.session.NettySessionManager;
import com.neo.qiaoqiaochat.session.SessionProxy;
import com.neo.qiaoqiaochat.util.SnowflakeIdWorker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class QiaoConnector implements QConnector {

    private final static Logger logger = LoggerFactory.getLogger(QConnector.class);

    @Autowired
    private NettySessionManager nettySessionManager;
    @Autowired
    private MessageProxy messageProxy;
    @Autowired
    private AuthService authService;
    @Autowired
    private SnowflakeIdWorker idWorker;

    @Override
    public void heartbeatFromClient(ChannelHandlerContext ctx, MessageWrapper wrapper) {
        //接受到客户端消息后 刷新心跳最新时间
//        ctx.channel().attr(AttributeKey.valueOf(QiaoqiaoConst.SessionConfig.HEARTBEAT_KEY)).set(System.currentTimeMillis());
    }


    @Override
    public void heartbeatToClient(ChannelHandlerContext ctx) {
        String sessionId = getChannelSessionId(ctx);
        QiaoQiaoHua.Model.Builder builder = QiaoQiaoHua.Model.newBuilder();
        MessageWrapper serverMessage = messageProxy.createServerMessage(sessionId, builder, MiCommand.HEARTBEAT, MiMessageType.SEND);

        Session session = nettySessionManager.getSession(sessionId);
        SessionProxy proxy = new SessionProxy(session);
        proxy.write(serverMessage.getBody());
    }


    private boolean pushMessage(MessageWrapper wrapper) {
        List<String> reSessionIds = wrapper.getReSessionIds();
        List<Session> sessions = nettySessionManager.getSessions(reSessionIds);
        SessionProxy proxy = new SessionProxy(sessions);
        return proxy.write(wrapper.getBody());
    }

//    /**
//     * 服务端转发用户消息
//     * @param wrapper
//     */
//    @Override
//    public void forwardMessage(MessageWrapper wrapper){
//
//    }

    /**
     * 服务端直接推送消息 通用方法
     *
     * @param wrapper 消息包装对象
     */
    @Override
    public void serverPushMessage(MessageWrapper wrapper) {
        boolean writeSuccess = pushMessage(wrapper);
        if (writeSuccess) {
            messageProxy.saveOnlineMessageToDB(wrapper);
        } else {
            messageProxy.saveOfflineMessageToDB(wrapper);
        }
    }

    /**
     * 推送添加好友消息
     *
     * @param bo 添加好友业务对象
     */
    @Override
    @Deprecated
    public void pushAddFriendMessage(AddFriendBO bo) {
        String account = bo.getAccount();
        List<String> sessionIds = nettySessionManager.getSessionIdsByAccount(account);

        //服务端生成添加好友的消息
        QiaoQiaoHua.Model.Builder builder = QiaoQiaoHua.Model.newBuilder();
        builder.setReceiver(bo.getAccount());
        builder.setSender(bo.getFromAccount());
        builder.setTextContent(bo.getSecretSignal());
        builder.setContentType(QiaoqiaoConst.MessageContentType.ADD_FRIEND);
        MessageWrapper serverMessage = messageProxy.createServerMessage(sessionIds, builder, MiCommand.MESSAGE, MiMessageType.ADD_FRIEND);

        serverPushMessage(serverMessage);

    }

    /**
     * 推送添加好友反馈消息
     *
     * @param bo 反馈添加好友业务对象
     */
    @Override
    @Deprecated
    public void pushConfirmFriendMessage(ConfirmFriendBO bo) {
        String account = bo.getAccount();
        List<String> sessionIds = nettySessionManager.getSessionIdsByAccount(account);

        MiMessageType messageType = null;
        String contentType = null;
        if (QiaoqiaoConst.FriendAction.PASSED_FRIEND_REQUIRE == bo.getAction()) {
            messageType = MiMessageType.ADD_FRIEND;
            contentType = QiaoqiaoConst.MessageContentType.PASSED_FRIEND_REQUIRE;
        } else if (QiaoqiaoConst.FriendAction.REFUSE_FRIEND_REQUIRE == bo.getAction() ||
                QiaoqiaoConst.FriendAction.BLACK_FRIEND_REQUIRE == bo.getAction()) {
            messageType = MiMessageType.REFUSE_FRIEND;
            contentType = QiaoqiaoConst.MessageContentType.REFUSE_FRIEND_REQUIRE;
        } else {
            return;
        }
//服务端生成添加好友的消息
        QiaoQiaoHua.Model.Builder builder = QiaoQiaoHua.Model.newBuilder();
        builder.setReceiver(bo.getAccount());
        builder.setSender(bo.getFromAccount());
        builder.setTextContent(bo.getRemark());
        builder.setContentType(contentType);
        MessageWrapper serverMessage = messageProxy.createServerMessage(sessionIds, builder, MiCommand.MESSAGE, messageType);
        serverPushMessage(serverMessage);
    }

    @Override
    public void pushGroupMessage(MessageWrapper wrapper) {
        // TODO: 2020/3/22 发送群消息
    }

    @Override
    public void close(String sessionId) {
        nettySessionManager.removeSession(sessionId);
    }

    @Override
    public void close(ChannelHandlerContext handler) {
        String sessionId = getChannelSessionId(handler);
        try {
            if (!StringUtils.isEmpty(sessionId)) {
                close(sessionId);
            }
        } finally {
            //关闭通道
            handler.channel().close();
        }
    }

    @Override
    public void connect(ChannelHandlerContext ctx, MessageWrapper wrapper) {
        //验证token TODO 是否可以放入统一的权限验证处理器中去做
        QiaoQiaoHua.Model body = wrapper.getBody();
        String token = body.getToken();
        TokenVO tokenVO = authService.checkTokenValid(token);

        //当用户登录后首次连接
        String sessionId = wrapper.getSessionId();
        String sessionId0 = getChannelSessionId(ctx);
        //当sessionID存在或者相等  视为同一用户重新连接  TODO 重连？
        if (!StringUtils.isEmpty(sessionId0) || sessionId.equals(sessionId0)) {
            logger.info("connector reconnect sessionId -> " + sessionId + ", ctx -> " + ctx.toString());
//            pushMessage(messageProxy.getReConnectionStateMsg(sessionId0));
        } else {
            logger.info("connector connect sessionId -> " + sessionId + ", sessionId0 -> " + sessionId0 + ", ctx -> " + ctx.toString());
            //session 中保存用户信息
            Session session = nettySessionManager.createSession(wrapper, ctx);

            nettySessionManager.addSession(session.getSessionId(), session);
            nettySessionManager.bindSessionIdsToAccount(tokenVO.getAccount(), session.getSessionId());

            setChannelSessionId(ctx, session.getSessionId());
            logger.info("create channel attr sessionId " + sessionId + " successful, ctx -> " + ctx.toString());
        }
        sendEchoToClient(ctx, wrapper);
    }


    /**
     * 接收到用户发送的消息 响应客户端的报文
     *
     * @param ctx     netty通道上下文
     * @param wrapper 接收到的消息
     */
    @Override
    public void replyMessageFromClient(ChannelHandlerContext ctx, MessageWrapper wrapper) {
        //生成一个唯一的消息id
        String sessionId = getChannelSessionId(ctx);
        //接收到的消息
        QiaoQiaoHua.Model received = wrapper.getBody();
        QiaoQiaoHua.Model.Builder builder = QiaoQiaoHua.Model.newBuilder();
        builder.setResponseId(received.getRequestId());
        builder.setUniqueId(wrapper.getBody().getUniqueId());
        builder.setIdentifier(received.getIdentifier());
        builder.setReceiver(received.getReceiver());
        builder.setTextContent(received.getTextContent());
        MessageWrapper serverMessage = messageProxy.createServerMessage(sessionId, builder, MiCommand.MESSAGE, MiMessageType.SEND_REPLY);
        pushMessage(serverMessage);

    }

    /**
     * 响应除了类型为消息的报文
     *
     * @param ctx
     * @param wrapper
     */
    public void sendEchoToClient(ChannelHandlerContext ctx, MessageWrapper wrapper) {
        String sessionId = getChannelSessionId(ctx);
        //接收到的消息
        QiaoQiaoHua.Model received = wrapper.getBody();
        QiaoQiaoHua.Model.Builder builder = QiaoQiaoHua.Model.newBuilder();
        builder.setResponseId(received.getRequestId());
        MessageWrapper serverMessage = messageProxy.createServerMessage(sessionId, builder, MiCommand.ECHO, MiMessageType.SEND_REPLY);
        pushMessage(serverMessage);
    }


    @Override
    public String getChannelSessionId(ChannelHandlerContext ctx) {
        return ctx.channel().attr(QiaoqiaoConst.SessionConfig.SERVER_SESSION_ID).get();
    }

    private void setChannelSessionId(ChannelHandlerContext ctx, String sessionId) {
        ctx.channel().attr(QiaoqiaoConst.SessionConfig.SERVER_SESSION_ID).set(sessionId);
    }
}
