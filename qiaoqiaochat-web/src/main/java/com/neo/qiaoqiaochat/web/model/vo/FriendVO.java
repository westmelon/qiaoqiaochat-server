package com.neo.qiaoqiaochat.web.model.vo;

import lombok.Data;

/**
 * 好友返回参数
 * @author linyi
 * @date 2020/7/28 9:00
 */
@Data
public class FriendVO {

    /**
     * 账号
     */
    private String account;

    /**
     * 昵称
     */
    private String nickName;


    /**
     * 好友备注
     */
    private String remarkName;

    /**
     * 头像地址
     */
    private String headImageIndex;

    /**
     * 好友关系 0好友 1拉黑
     */
    private Integer relationIndex;

    /**
     * 消息接收策略 0接收并提示 1接收但不提示 2拒收
     */
    private Integer msgReceiveSetting;



}
