package com.neo.qiaoqiaochat.websocket.service;

import com.neo.qiaoqiaochat.websocket.model.MessageWrapper;

/**
 * 消息存储相关服务
 *
 * @author Neo Lin
 * @date  2020/4/19
 */
public interface MessageStoreService {


    void saveOfflineMessageToDB(MessageWrapper wrapper);

    void saveOnlineMessageToDB(MessageWrapper wrapper);
}
