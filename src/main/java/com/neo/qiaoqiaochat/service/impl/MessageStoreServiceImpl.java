package com.neo.qiaoqiaochat.service.impl;
import java.util.Date;

import com.neo.commons.utils.SnowflakeIdWorker;
import com.neo.qiaoqiaochat.dao.MiMessageContentModelMapper;
import com.neo.qiaoqiaochat.dao.MiMessageHistoryModelMapper;
import com.neo.qiaoqiaochat.dao.MiMessageOfflineModelMapper;
import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.domain.MiMessageContentModel;
import com.neo.qiaoqiaochat.model.domain.MiMessageOfflineModel;
import com.neo.qiaoqiaochat.model.protobuf.QiaoQiaoHua;
import com.neo.qiaoqiaochat.service.MessageStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageStoreServiceImpl implements MessageStoreService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MiMessageContentModelMapper miMessageContentModelMapper;
    @Autowired
    private MiMessageOfflineModelMapper miMessageOfflineModelMapper;
    @Autowired
    private MiMessageHistoryModelMapper miMessageHistoryModelMapper;
    @Autowired
    private SnowflakeIdWorker idWorker;

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public void saveOfflineMessageToDB(MessageWrapper wrapper) {
        QiaoQiaoHua.Model model = (QiaoQiaoHua.Model) wrapper.getBody();
        String contentType = model.getContentType();
        MiMessageOfflineModel offlineModel = new MiMessageOfflineModel();
        long id = idWorker.nextId();
        Date now = new Date();
        offlineModel.setId(id);
//        offlineModel.setCode("");
        offlineModel.setType(contentType); //消息类型
        offlineModel.setSenderId(wrapper.getSenderId());
        offlineModel.setReceiverId(wrapper.getReceiverId());
        offlineModel.setState(QiaoqiaoConst.MessageState.NOT_SEND);
        offlineModel.setSendTime(now);
        offlineModel.setCreateTime(now);
        offlineModel.setUpdateTime(now);
        //生成消息
        miMessageOfflineModelMapper.insert(offlineModel);

        MiMessageContentModel contentModel = new MiMessageContentModel();
        contentModel.setMid(id);
//        contentModel.setCode("");
        contentModel.setContent(model.getTextContent());
        //TODO 处理媒体流
        contentModel.setStreamIndex("");
        contentModel.setCreateTime(now);
        contentModel.setUpdateTime(now);

        miMessageContentModelMapper.insertSelective(contentModel);
    }



    @Override
    public void saveOnlineMessageToDB(MessageWrapper wrapper) {
        //如果用户在线  直接把消息存到历史消息记录中

    }


    //在 接受到ack 报文后 将消息处理为已发送的状态
}
