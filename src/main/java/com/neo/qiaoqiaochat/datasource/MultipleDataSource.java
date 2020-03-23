package com.neo.qiaoqiaochat.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;



public class MultipleDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        logger.debug("datasource:"+MultipleDataSourceHolder.getDBType());
        return MultipleDataSourceHolder.getDBType();
    }

    @Override
    public void setTargetDataSources(Map targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }
}
