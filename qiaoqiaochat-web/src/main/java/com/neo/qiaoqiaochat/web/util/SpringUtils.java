package com.neo.qiaoqiaochat.web.util;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationContext;

/**
 * spring工具类
 *
 * @author copy
 */
public class SpringUtils {


    private static ApplicationContext applicationContext;

    private SpringUtils() {

    }


    public static <T> T createBean(Class<T> clazz) {
        return applicationContext.getAutowireCapableBeanFactory().createBean(clazz);
    }

    /**
     * 初始化上下文容器
     */
    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    /**
     * 获取上下文容器
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取容器内对象Set列表
     */
    public static <T> Set<T> getBeans(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz).values().stream().collect(Collectors.toSet());
    }

    /**
     * 获取容器内对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

}
