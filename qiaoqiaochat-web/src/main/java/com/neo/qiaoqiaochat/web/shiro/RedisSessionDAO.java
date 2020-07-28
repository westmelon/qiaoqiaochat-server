package com.neo.qiaoqiaochat.web.shiro;


import com.neo.qiaoqiaochat.web.config.redis.RedisCacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisSessionDAO extends AbstractSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
    @Autowired
    @Qualifier(value = "sessionRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SessionProperties sessionProperties;
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        logger.debug("create session:{}", session.getId());
        this.redisTemplate.opsForValue().set(RedisCacheManager.makeKey(this.sessionProperties.getPrefix(), sessionId.toString()), session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        logger.debug("get session:{}", sessionId);
        Session session = null;
        if (sessionId != null) {
            session = (Session)this.redisTemplate.opsForValue().get(RedisCacheManager.makeKey(this.sessionProperties.getPrefix(), sessionId.toString()));
        }

        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        try {
            if (session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
                return;
            }
        } catch (Exception e) {
            logger.error("session is expire");
        }

        String key = RedisCacheManager.makeKey(this.sessionProperties.getPrefix(), session.getId().toString());
        if (session instanceof NewSimpleSession && ((NewSimpleSession)session).isChanged()) {
            //sesssion 发生变化 刷新缓存
            logger.debug("get session:{}", session.getId());
            ((NewSimpleSession)session).setChanged(false);
            this.redisTemplate.opsForValue().set(key, session);
        }

        this.redisTemplate.expire(key, (long)this.sessionProperties.getTimeout(), TimeUnit.SECONDS);
    }

    @Override
    public void delete(Session session) {
        if (session != null && session.getId() != null) {
            logger.debug("delete session:{}", session.getId());
            this.redisTemplate.delete(RedisCacheManager.makeKey(this.sessionProperties.getPrefix(), session.getId().toString()));
        } else {
            logger.error("session or session id is null");
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet();
        Set<String> keys = this.redisTemplate.keys(RedisCacheManager.makeKey(this.sessionProperties.getPrefix(), ""));
        if (keys != null && keys.size() > 0) {
            Iterator iterator = keys.iterator();

            while(iterator.hasNext()) {
                String key = (String)iterator.next();
                Session s = (Session)this.redisTemplate.opsForValue().get(key);
                sessions.add(s);
            }
        }

        return sessions;
    }
}
