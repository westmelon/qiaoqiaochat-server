package com.neo.qiaoqiaochat.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.neo.qiaoqiaochat.datasource.MultipleDataSource;
import com.neo.qiaoqiaochat.datasource.MultipleDataSourceHolder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = "com.neo.qiaoqiaochat.dao")
@EnableTransactionManagement
public class MyBatisConfig {

    @Autowired
    Environment env;
    /**
     * 生成数据源
     * @return
     */
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource dataSource(){
        return DruidDataSourceBuilder.create().build();
    }


    @Bean(name = "multds")
    MultipleDataSource multipleDataSource()  throws Exception{
        MultipleDataSource multipleDataSource = new MultipleDataSource();
        Map<String,DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put(MultipleDataSourceHolder.MAIN,dataSource());
        MultipleDataSourceHolder.dataSourceIds.add(MultipleDataSourceHolder.MAIN);
        multipleDataSource.setTargetDataSources(dataSourceMap);
        return multipleDataSource;
    }

    /**
     * spring和MyBatis完美整合 会话工程类 省去配置映射文件
     * @return
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource multds) throws Exception{
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(multds);
        bean.setTypeAliasesPackage(env.getProperty("mybatis.type-aliases-package"));
        String mapper = env.getProperty("mybatis.mapper-locations");
        if (!StringUtils.isEmpty(mapper)){
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapper));
        }
        return bean.getObject();
    }

}
