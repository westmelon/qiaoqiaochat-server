package com.neo.qiaoqiaochat.service;

import com.neo.qiaoqiaochat.model.MessageWrapper;

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
