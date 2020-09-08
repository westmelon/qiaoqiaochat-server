package com.neo.qiaoqiaochat.web.controller;

import com.neo.core.entity.SimpleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manage")
public class MngController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取netty session列表
     * @return SimpleResult
     */
    @RequestMapping("session/list")
    public SimpleResult register(){
        SimpleResult result = new SimpleResult();
        return result;
    }
}
