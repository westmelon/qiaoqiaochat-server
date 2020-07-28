package com.neo.qiaoqiaochat.web.dao;

import com.neo.qiaoqiaochat.web.model.domain.MiFriendModel;
import com.neo.qiaoqiaochat.web.model.vo.FriendVO;
import com.neo.qiaoqiaochat.web.util.SimpleMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MiFriendModelMapper extends SimpleMapper<MiFriendModel> {
    /**
     * 获取好友列表
     * @param uid
     * @return
     */
    List<FriendVO> getFriendList(@Param("uid") Long uid);
}