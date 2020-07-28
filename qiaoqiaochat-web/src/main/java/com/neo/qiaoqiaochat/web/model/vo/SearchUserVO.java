package com.neo.qiaoqiaochat.web.model.vo;

import javax.persistence.Column;

/**
 * 搜索用户结果
 *
 * @author linyi
 * @date 2020 /7/27 15:44
 */
public class SearchUserVO {

    /**
     * 账号
     */
    private String account;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 简介
     */
    private String introduce;

    /**
     * 头像
     */
    private String headImageIndex;
}
