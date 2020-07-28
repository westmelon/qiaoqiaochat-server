package com.neo.qiaoqiaochat.web.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SimpleSessionListener implements SessionListener {

    private static final Logger logger = LoggerFactory.getLogger(SimpleSessionListener.class);

    @Autowired
    private RedisSessionDAO redisSessionDAO;

    public SimpleSessionListener() {
    }

    @Override
    public void onStart(Session session) {
        //session创建
        logger.debug("session {} has bean created!", session.getId());
    }

    @Override
    public void onStop(Session session) {
        //session销毁
        logger.debug("session {} has bean destroyed!", session.getId());
        redisSessionDAO.delete(session);
    }

    @Override
    public void onExpiration(Session session) {
        //session过期
        logger.debug("session {} is expired!", session.getId());
        redisSessionDAO.delete(session);
    }
}
