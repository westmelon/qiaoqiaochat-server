package com.neo.qiaoqiaochat.web.model.dto;

import lombok.Data;

/**
 * 搜索用户请求参数
 *
 * @author linyi
 * @date 2020 /7/27 15:25
 */
@Data
public class SearchUserDTO {


    /**
     * 用户账号
     */
    private String account;
}