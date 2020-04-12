package com.neo.qiaoqiaochat.connector;

import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.emun.MiCommand;
import com.neo.qiaoqiaochat.model.protobuf.QiaoQiaoHua;
import com.neo.qiaoqiaochat.proxy.MessageFactory;
import com.neo.qiaoqiaochat.proxy.MessageProxy;
import com.neo.qiaoqiaochat.service.AuthService;
import com.neo.qiaoqiaochat.service.LoginService;
import com.neo.qiaoqiaochat.session.Session;
import com.neo.qiaoqiaochat.session.NettySessionManager;
import com.neo.qiaoqiaochat.session.SessionProxy;
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

    @Override
    public void heartbeatFromClient(ChannelHandlerContext ctx, MessageWrapper wrapper) {
        //接受到客户端消息后 刷新心跳最新时间
        ctx.channel().attr(AttributeKey.valueOf(QiaoqiaoConst.SessionConfig.HEARTBEAT_KEY)).set(System.currentTimeMillis());
    }

    @Override
    public void heartbeatToClient(ChannelHandlerContext ctx) {
        MessageWrapper serverMessage = MessageFactory.createServerMessage(ctx, MiCommand.HEARTBEAT);
        //TODO 去除冗余代码
        List<String> reSessionIds = serverMessage.getReSessionIds();
        List<Session> sessions = nettySessionManager.getSessions(reSessionIds);
        SessionProxy proxy = new SessionProxy(sessions);
        boolean writeSuccess = proxy.write(serverMessage.getBody());
    }


    @Override
    public void pushMessage(MessageWrapper wrapper) throws RuntimeException {
        //服务器接收到消息的回执信息
        String sessionId = wrapper.getSessionId();
        Session session = nettySessionManager.getSession(sessionId);
        if(session != null){
            //todo
            SessionProxy proxy = new SessionProxy(session);
            proxy.write(wrapper.getBody());
        }
    }

    /**
     * 服务端接收到用户间的消息进行转发
     * @param sessionId
     * @param wrapper
     * @throws RuntimeException
     */
    @Override
    public void pushMessage(String sessionId, MessageWrapper wrapper) throws RuntimeException {
        Session session = nettySessionManager.getSession(sessionId);
        if(session != null){
            //发送消息
            List<String> reSessionIds = wrapper.getReSessionIds();
            List<Session> sessions = nettySessionManager.getSessions(reSessionIds);
            SessionProxy proxy = new SessionProxy(sessions);
            boolean writeSuccess = proxy.write(wrapper.getBody());

            if(writeSuccess){
                messageProxy.saveOnlineMessageToDB(wrapper);
            }else{
                messageProxy.saveOfflineMessageToDB(wrapper);
            }


        }else{ //用户当前不在线
            messageProxy.saveOfflineMessageToDB(wrapper);
        }
    }

    @Override
    public void pushGroupMessage(MessageWrapper wrapper) throws RuntimeException {
        // TODO: 2020/3/22 发送群消息
    }

    @Override
    public void close(String sessionId) {
        nettySessionManager.removeSession(sessionId);
    }

    @Override
    public void close(ChannelHandlerContext hander) {
        String sessionId = getChannelSessionId(hander);
        if(!StringUtils.isEmpty(sessionId)) {
            close(sessionId);
        }
    }

    @Override
    public void connect(ChannelHandlerContext ctx, MessageWrapper wrapper) {
        //验证token TODO 是否可以放入统一的权限验证处理器中去做
        QiaoQiaoHua.Model body = (QiaoQiaoHua.Model) wrapper.getBody();
        String token = body.getToken();
        String sender = body.getSender();
        authService.checkTokenValid(token, sender);

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

            nettySessionManager.addSession(sessionId, session);
            nettySessionManager.bindSessionIdsToAccount(sender, sessionId);

            setChannelSessionId(ctx, sessionId);
            logger.info("create channel attr sessionId " + sessionId + " successful, ctx -> " + ctx.toString());
        }
        MessageWrapper serverAckMessage = MessageFactory.createServerAckMessage(wrapper);
        pushMessage(serverAckMessage);
    }


    @Override
    public String getChannelSessionId(ChannelHandlerContext ctx) {
        return ctx.channel().attr(QiaoqiaoConst.SessionConfig.SERVER_SESSION_ID).get();
    }

    private void setChannelSessionId(ChannelHandlerContext ctx, String sessionId) {
        ctx.channel().attr(QiaoqiaoConst.SessionConfig.SERVER_SESSION_ID).set(sessionId);
    }
}
