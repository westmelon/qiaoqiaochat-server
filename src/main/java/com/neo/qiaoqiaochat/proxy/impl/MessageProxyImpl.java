package com.neo.qiaoqiaochat.proxy.impl;

import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.emun.MiCommand;
import com.neo.qiaoqiaochat.model.emun.MiMessageType;
import com.neo.qiaoqiaochat.model.protobuf.QiaoQiaoHua;
import com.neo.qiaoqiaochat.proxy.MessageProxy;
import com.neo.qiaoqiaochat.service.MessageStoreService;
import com.neo.qiaoqiaochat.service.UserService;
import com.neo.qiaoqiaochat.service.impl.UserServiceImpl;
import com.neo.qiaoqiaochat.session.NettySessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MessageProxyImpl implements MessageProxy {

    @Autowired
    private NettySessionManager nettySessionManager;
    @Autowired
    private MessageStoreService messageStoreService;
    @Autowired
    private UserService userService;

    @Override
    public MessageWrapper createMessageWapper(String sessionId, QiaoQiaoHua.Model qiaoqiaohua) {
        //todo 消息解密
        int cmd = qiaoqiaohua.getCmd();
        MiCommand miCommand = MiCommand.getEnumByCode(cmd);
        if (miCommand == null) {
            return null;
        }
        MessageWrapper wrapper = null;
        switch (miCommand) {
            case CONNECT:
                //首次连接时候生成SessionId
                //TODO
//                String sessionId0 = UUID.randomUUID().toString();
                wrapper = new MessageWrapper(MessageWrapper.MessageProtocol.CONNECT, "", "", qiaoqiaohua);
                break;
            case CLOSE:
                wrapper = null;
                break;
            case HEARTBEAT:
                wrapper = new MessageWrapper(MessageWrapper.MessageProtocol.HEARTBEAT, sessionId, "", qiaoqiaohua);
                break;
            case ONLINE:
                break;
            case OFFLINE:
                break;
            case MESSAGE:
                //
                int msgType = qiaoqiaohua.getMsgType();
                String receiver = qiaoqiaohua.getReceiver();
                String sender = qiaoqiaohua.getSender();
                String groupId = qiaoqiaohua.getGroupId();
                if (!StringUtils.isEmpty(receiver) && MiMessageType.SEND.getCode() == msgType) {
                    //消息接受人为用户
                    //根据消息接收人找到对应的sessionId 如果么有
                    List<String> sessionIds = nettySessionManager.getSessionIdsByAccount(receiver);
                    wrapper = new MessageWrapper(MessageWrapper.MessageProtocol.SEND, sessionId, sessionIds, qiaoqiaohua);
                } else if (!StringUtils.isEmpty(groupId) && MiMessageType.SEND.getCode() == msgType) {
                    //消息接收人为群组
                    wrapper = new MessageWrapper(MessageWrapper.MessageProtocol.GROUP, sessionId, "", qiaoqiaohua);
                } else if (MiMessageType.SEND_REPLY.getCode() == msgType) {
                    //消息为应答服务器内容
                    wrapper = new MessageWrapper(MessageWrapper.MessageProtocol.SEND_REPLY, sessionId, "", qiaoqiaohua);
                } else if (MiMessageType.NOTIFY.getCode() == msgType) {
                    //消息为通知消息
                    wrapper = null;
                } else if (MiMessageType.NOTIFY_REPLY.getCode() == msgType) {
                    //消息为通知应答消息
                    wrapper = null;
                }
                if(wrapper != null){
                    //设置消息接收人和发送人的id
                    Long receiverId = userService.getUserIdByAccount(receiver);
                    Long senderId = userService.getUserIdByAccount(sender);
                    wrapper.setSenderId(senderId);
                    wrapper.setReceiverId(receiverId);
                }
                break;
            default:
                wrapper = null;
                break;
        }

        return wrapper;
    }


    @Override
    public MessageWrapper createServerMessage(String reSession, QiaoQiaoHua.Model.Builder qiaoqiaohuaBuilder, MiCommand command, MiMessageType messageType) {

        List<String> reSessions = new ArrayList<>();
        reSessions.add(reSession);
        return createServerMessage(reSessions, qiaoqiaohuaBuilder, command, messageType);
    }


    @Override
    public MessageWrapper createServerMessage(List<String> reSessions, QiaoQiaoHua.Model.Builder builder, MiCommand command, MiMessageType messageType) {
        builder.setCmd(command.getCode());
        builder.setMsgType(messageType.getCode());
        String sender = builder.getSender();
        String receiver = builder.getReceiver();
        Long receiverId = userService.getUserIdByAccount(receiver);
        Long senderId = userService.getUserIdByAccount(sender);

        MessageWrapper wrapper = new MessageWrapper(MessageWrapper.MessageProtocol.SEND, "", reSessions, builder.build());
        wrapper.setSenderId(senderId);
        wrapper.setReceiverId(receiverId);
        return wrapper;
    }

    @Override
    public void saveOnlineMessageToDB(MessageWrapper wapper) {
        System.out.println("save online message");
        messageStoreService.saveOnlineMessageToDB(wapper);
    }

    @Override
    public void saveOfflineMessageToDB(MessageWrapper wapper) {
        System.out.println("save offline message");
        messageStoreService.saveOfflineMessageToDB(wapper);
    }
}
