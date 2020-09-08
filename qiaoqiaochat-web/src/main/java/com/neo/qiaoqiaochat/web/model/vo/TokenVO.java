package com.neo.qiaoqiaochat.web.model.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * token封装对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenVO {

    /**
     * 密令
     */
    private String token;

    /**
     * 账号
     */
    private String account;

}
