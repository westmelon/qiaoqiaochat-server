package com.neo.qiaoqiaochat.proxy;

import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.emun.MiCommand;
import com.neo.qiaoqiaochat.model.emun.MiMessageType;
import com.neo.qiaoqiaochat.model.protobuf.QiaoQiaoHua;

import java.util.List;

/**
 * The interface Message proxy.
 */
public interface MessageProxy {

    /**
     * 包装客户端请求消息
     *
     * @param sessionId   the session id
     * @param qiaoqiaohua the qiaoqiaohua
     * @return the message wrapper
     */
    MessageWrapper createMessageWapper(String sessionId, QiaoQiaoHua.Model qiaoqiaohua);

    MessageWrapper createServerMessage(String reSession, QiaoQiaoHua.Model.Builder qiaoqiaohuaBuilder, MiCommand command, MiMessageType messageType);

    /**
     * 生成服务端消息  场景1服务器接收到消息后生成应答消息
     * 场景2 服务器生成消息通知用户
     **/
    MessageWrapper createServerMessage(List<String> reSessions, QiaoQiaoHua.Model.Builder qiaoqiaohuaBuilder, MiCommand command, MiMessageType messageType);

    /**
     * 保存在线消息
     *
     * @param wapper the wapper
     */
    void saveOnlineMessageToDB(MessageWrapper wapper);

    /**
     * 保存离线消息
     *
     * @param wapper the wapper
     */
    void saveOfflineMessageToDB(MessageWrapper wapper);

    //获取上线状态消息  将上线成功的消息返回给客户端


    //获取重连状态消息


    //获取下线状态消息
}
