package com.neo.qiaoqiaochat.controller;

import com.neo.qiaoqiaochat.model.SimpleResult;
import com.neo.qiaoqiaochat.session.NettySessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manage")
public class MngController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NettySessionManager nettySessionManager;
    /**
     * 获取netty session列表
     * @return SimpleResult
     */
    @RequestMapping("session/list")
    public SimpleResult register(){
        SimpleResult result = new SimpleResult();
        result.setData(nettySessionManager.getUserSessionMapping());
        return result;
    }
}
