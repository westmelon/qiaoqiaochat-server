package com.neo.qiaoqiaochat.web.shiro;

import com.neo.qiaoqiaochat.web.config.redis.RedisCacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.*;

//@Configuration
public class ShiroConfiguration {


    //将自己的验证方式加入容器
    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        //将自定义的令牌set到了Realm
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置SecuritManager
        shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager());
        //使用自定义的过滤器
        Map<String, Filter> filters = new HashMap<>();
        filters.put("authc", new SimpleAuthFilter());
        shiroFilterFactoryBean.setFilters(filters);
        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置退出过滤器,其中的具体代码Shiro已经替我们实现了
        //filterChainDefinitionMap.put("/logout", "logout");

        //过滤链定义，从上向下顺序执行，一般将 /**放在最为下边
        //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问

        filterChainDefinitionMap.put("/auth/login", "anon");       //不拦截登录请求
        filterChainDefinitionMap.put("/user/register", "anon");

        filterChainDefinitionMap.put("/**", "anon");


        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;

    }


    //配置securityManager安全管理器.
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setAuthenticator(authenticator());
        //设置多Realm，用于获取认证凭证
        Collection<Realm> realms = new ArrayList<>();
        realms.add(myShiroRealm());
        securityManager.setRealms(realms);
        //注入缓存管理器
        securityManager.setCacheManager(cacheManager());
        //注入会话管理器
        securityManager.setSessionManager(sessionManager());
        //注入Cookie(记住我)管理器(remenberMeManager)
        securityManager.setRememberMeManager(null);
        return securityManager;
    }


    //cookie 管理器
    @Bean
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
//        simpleCookie.setMaxAge(2592000);
//        simpleCookie.setHttpOnly(true);
        simpleCookie.setName("q-session");
        return simpleCookie;
    }

    //记住我的功能
//    @Bean
//    public CookieRememberMeManager rememberMeManager() {
//        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
//        byte[] cipherKey = Base64.decode("4AvVhmFLUs0KTA3Kprsdag==");
//        cookieRememberMeManager.setCookie(simpleCookie());
//        cookieRememberMeManager.setCipherKey(cipherKey);
//        return cookieRememberMeManager;
//    }


    @Bean
    public SimpleSessionListener sessionListener() {
        return new SimpleSessionListener();
    }

    @Bean
    public RedisSessionDAO sessionDao() {
        return new RedisSessionDAO();
    }

    //配置cacheManager todo conditiononmissbean
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        return redisCacheManager;
    }

    @Bean
    public SimpleSessionFactory sessionFactory() {
        return new SimpleSessionFactory();
    }

    //session管理
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置session dao
        sessionManager.setSessionDAO(sessionDao());
        //设置cookie中session的id
        sessionManager.setSessionIdCookie(simpleCookie());
        //设置session工厂
        sessionManager.setSessionFactory(sessionFactory());
        List<SessionListener> list = new ArrayList();
        //设置监听器
        list.add(sessionListener());
        sessionManager.setSessionListeners(list);
        int expire = -1000;
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(false);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(false);
        //全局session过期时间
        sessionManager.setGlobalSessionTimeout((long) expire);
        sessionManager.setCacheManager(cacheManager());
        //取消url 后面的 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }


    //密码匹配凭证管理器
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //采用SHA-512方式加密
        hashedCredentialsMatcher.setHashAlgorithmName("SHA-512");
        //设置加密次数
        hashedCredentialsMatcher.setHashIterations(5);
        //true加密用的hex编码，false用的base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }


    //保证实现了Shiro内部lifecycle函数的bean执行.
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    //开启Shiro的注解(如@RequiresRoles,@RequiresPermissions等等),
    // 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证,
    // 配置以下两个bean(DefaultAdvisorAutoProxyCreatorAuthorizationAttributeSourceAdvisor)即可实现此功能.
    //
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
                new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(getDefaultWebSecurityManager());
        return authorizationAttributeSourceAdvisor;
    }

}
