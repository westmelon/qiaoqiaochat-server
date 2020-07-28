package com.neo.qiaoqiaochat.web.datasource;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Description:切换数据源aop
 * @Author Neo Lin
 * @Date  2017/11/24 15:58
 */
@Aspect
@Order(-1)// 保证该AOP在@Transactional之前执行
@Component
public class MultipleDataSourceAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleDataSourceAspect.class);


    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, TargetDataSource ds) throws Throwable {
        String dsId = ds.name();
        if (!MultipleDataSourceHolder.containsDataSource(dsId)) {
            LOGGER.error("数据源[{}]不存在，使用默认数据源 > {}", ds.name(), point.getSignature());
        } else {
            LOGGER.debug("Use DataSource : {} > {}", ds.name(), point.getSignature());
            MultipleDataSourceHolder.setDBType(ds.name());
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
        LOGGER.debug("Revert DataSource : {} > {}", ds.name(), point.getSignature());
        MultipleDataSourceHolder.clearDBType();
    }

}
