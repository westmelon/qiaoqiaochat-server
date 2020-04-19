package com.neo.qiaoqiaochat.session;


import com.neo.qiaoqiaochat.config.InitApp;
import com.neo.qiaoqiaochat.config.redis.RedisCache;
import com.neo.qiaoqiaochat.config.redis.RedisCacheManager;
import com.neo.qiaoqiaochat.model.MessageWrapper;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.proxy.MessageProxy;
import io.netty.channel.ChannelHandlerContext;
import com.neo.qiaoqiaochat.model.Message;
import org.apache.shiro.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnBean(InitApp.class)
public class NettySessionManager {

    /**
     * sessionId与netty session 的缓存
     */
    private final Cache<String, Session> sessionCache;

    /**
     * 用户账号与netty session列表对应关系
     */
    private final Cache<String, Set<String>> accountSessionIdsCache;

    @Autowired
    public NettySessionManager(RedisCacheManager cacheManager) {
        sessionCache = cacheManager.getCache(QiaoqiaoConst.RedisCacheConfig.NETTY_SESSION_NAMESPACE);
        accountSessionIdsCache = cacheManager.getCache(QiaoqiaoConst.RedisCacheConfig.ACCOUNT_SESSIONIDS);

    }

    /**
     * 添加netty session
     *
     * @param sessionId the session id
     * @param session   the session
     */
    public synchronized void addSession(String sessionId, Session session) {
        sessionCache.put(sessionId, session);
    }


    /**
     * 查找netty session
     *
     * @param sessionId the session id
     * @return the session
     */
    public Session getSession(String sessionId) {
        return sessionCache.get(sessionId);
    }

    /**
     * 移除netty session
     *
     * @param sessionId the session id
     */
    public void removeSession(String sessionId) {
        Session session = getSession(sessionId);

        if (session != null) {
            SessionProxy proxy = new SessionProxy(session);

            proxy.close(); //关闭通道
            sessionCache.remove(sessionId);

            //更新用户在线设备列表
            String account = session.getAccount();
            synchronized (this) {
                Set<String> userSessions = accountSessionIdsCache.get(account);
                if (!CollectionUtils.isEmpty(userSessions)) {
                    userSessions.remove(sessionId);
                }
                accountSessionIdsCache.put(account, userSessions);
            }
        }
    }


    /**
     * 根据sessionId列表查找netty session列表
     *
     * @param sessionIds the session ids
     * @return the sessions
     */
    public List<Session> getSessions(List<String> sessionIds) {
        List<Session> sessions = new ArrayList<>();
        sessionIds.forEach(sessionId -> {
            Session session = getSession(sessionId);
            if (session != null) {
                sessions.add(session);
            }
        });
        return sessions;
    }

    /**
     * 生成netty session
     */
    public Session createSession(MessageWrapper wrapper, ChannelHandlerContext ctx) {
        //TODO 生成一个会话id
//        String sender = message.getSender();
        Session session = new Session();
        session.setSessionId(UUID.randomUUID().toString());
        session.setChannel(ctx.channel());
        session.setCreateSessionTime(System.currentTimeMillis());
        return session;
    }


    /**
     * 根据账号查找用户netty sessionIds
     *
     * @param account 账号
     * @return 账号的session列表
     */
    public List<String> getSessionIdsByAccount(String account) {
        Set<String> userSessions = accountSessionIdsCache.get(account);
        return CollectionUtils.isEmpty(userSessions) ? null : new ArrayList<>(userSessions);
    }

    /**
     * 绑定账号和session列表 同步处理
     *
     * @param account   账号
     * @param sessionId the session id
     */
    public synchronized void bindSessionIdsToAccount(String account, String sessionId) {
        Set<String> sessionIds = accountSessionIdsCache.get(account);
        if (CollectionUtils.isEmpty(sessionIds)) {
            sessionIds = new HashSet<>();
        }
        sessionIds.add(sessionId);
        accountSessionIdsCache.put(account, sessionIds);
    }

    public Map<String, Set<String>> getUserSessionMapping() {
        return ((RedisCache)accountSessionIdsCache).mapValues();
    }
}
