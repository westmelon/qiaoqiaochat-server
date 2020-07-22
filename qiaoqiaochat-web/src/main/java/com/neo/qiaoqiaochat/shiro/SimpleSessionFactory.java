package com.neo.qiaoqiaochat.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;

public class SimpleSessionFactory implements SessionFactory {

    public SimpleSessionFactory() {
    }

    @Override
    public Session createSession(SessionContext sessionContext) {
        if (sessionContext != null) {
            String host = sessionContext.getHost();
            if (host != null) {
                return new NewSimpleSession(host);
            }
        }

        return new NewSimpleSession();
    }
}
