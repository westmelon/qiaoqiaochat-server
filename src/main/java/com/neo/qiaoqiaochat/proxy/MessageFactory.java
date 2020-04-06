package com.neo.qiaoqiaochat.proxy;

import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.protobuf.QiaoQiaoHua;

public class MessageFactory {


    //生成服务器应答消息 TODO
    public static MessageWrapper createServerAckMessage(MessageWrapper source){

        QiaoQiaoHua.Model body = (QiaoQiaoHua.Model) source.getBody();

        QiaoQiaoHua.Model.Builder builder = QiaoQiaoHua.Model.newBuilder();
        builder.setCmd(QiaoqiaoConst.CommandLine.MESSAGE);
        builder.setMsgType(QiaoqiaoConst.MessageType.SEND_REPLY);
        builder.setTimestamp(System.currentTimeMillis());
        builder.setTextContent("");
        builder.setMediaType(0);
        builder.setGroupId("");
        builder.setSender("0");
        builder.setReceiver(body.getSender());
        builder.setAppKey("22");
        builder.setSign("11");
        builder.setClientType(0);
        builder.setClientVersion("1.0");
        builder.setEncryptionType(0);


        MessageWrapper rtn = new MessageWrapper(MessageWrapper.MessageProtocol.CONNECT, source.getSessionId(), "", builder.build());
        return rtn;
    }

}
