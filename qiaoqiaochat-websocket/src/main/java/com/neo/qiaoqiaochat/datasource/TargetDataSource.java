package com.neo.qiaoqiaochat.datasource;


import java.lang.annotation.*;

/**
 * @Description:在方法上使用，用于指定使用哪个数据源
 * @Author Neo Lin
 * @Date  2017/11/24 15:52
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String name();
}
