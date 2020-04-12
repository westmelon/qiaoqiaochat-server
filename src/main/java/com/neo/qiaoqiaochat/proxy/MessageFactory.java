package com.neo.qiaoqiaochat.proxy;

import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.emun.MiCommand;
import com.neo.qiaoqiaochat.model.protobuf.QiaoQiaoHua;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class MessageFactory {


    //生成服务器应答消息 TODO
    public static MessageWrapper createServerAckMessage(MessageWrapper source){

        QiaoQiaoHua.Model body = (QiaoQiaoHua.Model) source.getBody();

        QiaoQiaoHua.Model.Builder builder = QiaoQiaoHua.Model.newBuilder();
        builder.setCmd(MiCommand.MESSAGE.getCode());
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

    public static MessageWrapper createServerMessage(ChannelHandlerContext ctx, MiCommand command){

        //TODO 从ChannelHandlerContext中获取相关信息
        String sessionId = ctx.channel().attr(QiaoqiaoConst.SessionConfig.SERVER_SESSION_ID).get();

        QiaoQiaoHua.Model.Builder builder = QiaoQiaoHua.Model.newBuilder();
        builder.setCmd(command.getCode());
        builder.setMsgType(QiaoqiaoConst.MessageType.SEND);
        builder.setTimestamp(System.currentTimeMillis());
        builder.setTextContent("");
        builder.setMediaType(0);
        builder.setGroupId("");
        builder.setSender("0");
        builder.setReceiver("0");
        builder.setAppKey("22");
        builder.setSign("11");
        builder.setClientType(0);
        builder.setClientVersion("1.0");
        builder.setEncryptionType(0);

        MessageWrapper rtn = new MessageWrapper(MessageWrapper.MessageProtocol.CONNECT, "0", sessionId, builder.build());
        return rtn;
    }

}
