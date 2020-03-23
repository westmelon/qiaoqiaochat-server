package com.neo.qiaoqiaochat.proxy;

import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.protobuf.QiaoQiaoHua;

public interface MessageProxy {

    //包装消息
    MessageWrapper createMessageWapper(String sessionId, QiaoQiaoHua.Model qiaoqiaohua);

    //保存在线消息
    void saveOnlineMessageToDB(MessageWrapper wapper);

    //保存离线消息
    void saveOfflineMessageToDB(MessageWrapper wapper);

    //获取上线状态消息  将上线成功的消息返回给客户端


    //获取重连状态消息


    //获取下线状态消息
}
