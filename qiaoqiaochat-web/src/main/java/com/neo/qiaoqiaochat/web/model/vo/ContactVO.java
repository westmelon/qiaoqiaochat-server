package com.neo.qiaoqiaochat.web.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 通讯录列表
 *
 * @author linyi
 * @date 2020/7/27 16:27
 */
@Data
public class ContactVO {


    /**
     * 好友列表
     */
    private List<FriendVO> friends;
}
