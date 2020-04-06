package com.neo.qiaoqiaochat.proxy.impl;

import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.protobuf.QiaoQiaoHua;
import com.neo.qiaoqiaochat.proxy.MessageProxy;
import com.neo.qiaoqiaochat.session.NettySessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Component
public class MessageProxyImpl implements MessageProxy {

    @Autowired
    private NettySessionManager nettySessionManager;

    @Override
    public MessageWrapper createMessageWapper(String sessionId, QiaoQiaoHua.Model qiaoqiaohua) {
        int cmd = qiaoqiaohua.getCmd();
        switch (cmd) {
            case QiaoqiaoConst.CommandLine.CONNECT:
                //首次连接时候生成SessionId
                //TODO
                String sessionId0 = UUID.randomUUID().toString();
                return new MessageWrapper(MessageWrapper.MessageProtocol.CONNECT, sessionId0, "", qiaoqiaohua);
            case QiaoqiaoConst.CommandLine.CLOSE:
                return null;
            case QiaoqiaoConst.CommandLine.HEARTBEAT:
                return new MessageWrapper(MessageWrapper.MessageProtocol.HEARTBEAT, sessionId, "", qiaoqiaohua);
            case QiaoqiaoConst.CommandLine.ONLINE:
                break;
            case QiaoqiaoConst.CommandLine.OFFLINE:
                break;
            case QiaoqiaoConst.CommandLine.MESSAGE:
                //
                int msgType = qiaoqiaohua.getMsgType();
                String receiver = qiaoqiaohua.getReceiver();
                String groupId = qiaoqiaohua.getGroupId();
                if (!StringUtils.isEmpty(receiver) && QiaoqiaoConst.MessageType.SEND == msgType) {
                    //消息接受人为用户
                    //根据消息接收人找到对应的sessionId 如果么有
                    List<String> sessionIds = nettySessionManager.getSessionIdsByAccount(receiver);
                    return new MessageWrapper(MessageWrapper.MessageProtocol.SEND, sessionId, sessionIds, qiaoqiaohua);
                } else if (!StringUtils.isEmpty(groupId) && QiaoqiaoConst.MessageType.SEND == msgType) {
                    //消息接收人为群组
                    return new MessageWrapper(MessageWrapper.MessageProtocol.GROUP, sessionId, "", qiaoqiaohua);
                } else if (QiaoqiaoConst.MessageType.SEND_REPLY == msgType) {
                    //消息为应答服务器内容
                    return new MessageWrapper(MessageWrapper.MessageProtocol.SEND_REPLY, sessionId, "", qiaoqiaohua);
                } else if (QiaoqiaoConst.MessageType.NOTIFY == msgType) {
                    //消息为通知消息
                    return null;
                } else if (QiaoqiaoConst.MessageType.NOTIFY_REPLY == msgType) {
                    //消息为通知应答消息
                    return null;
                }
            default:
                return null;
        }

        return null;
    }

    @Override
    public void saveOnlineMessageToDB(MessageWrapper wapper) {
        System.out.println("save online message");
    }

    @Override
    public void saveOfflineMessageToDB(MessageWrapper wapper) {
        System.out.println("save offline message");
    }
}
