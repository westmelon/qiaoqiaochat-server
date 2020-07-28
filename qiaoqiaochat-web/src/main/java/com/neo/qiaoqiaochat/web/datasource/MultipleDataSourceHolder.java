package com.neo.qiaoqiaochat.web.datasource;


import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：${DESCRIPTION}
 * 作者：王兆阳
 * 日期：2017-08-28
 **/

public class MultipleDataSourceHolder {

    private MultipleDataSourceHolder() {
    }

    private static final ThreadLocal<String> dataSourceKey = new ThreadLocal<>();
    public static List<String> dataSourceIds = new ArrayList<>();
    public static final String MAIN = "main";



    public static void setDBType(String dbType) {
        dataSourceKey.set(dbType);
    }

    public static String getDBType() {
        if (!StringUtils.isEmpty(dataSourceKey.get())){
            return dataSourceKey.get();
        }else{
            return MAIN;
        }

    }

    public static void clearDBType() {
        dataSourceKey.remove();
    }

    /**
     * @Description: 判断指定数据源是否存在
     * @Author Neo Lin
     * @param  [dataSourceId]
     * @return  boolean
     * @Date  2017/11/24
     */
    public static boolean containsDataSource(String dataSourceId){
        return dataSourceIds.contains(dataSourceId);
    }

}
