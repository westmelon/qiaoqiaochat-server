package com.neo.qiaoqiaochat.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.neo.qiaoqiaochat.datasource.MultipleDataSource;
import com.neo.qiaoqiaochat.datasource.MultipleDataSourceHolder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.neo.qiaoqiaochat.dao")
@EnableTransactionManagement
@ConfigurationProperties
public class MyBatisConfig {

    @Autowired
    Environment env;

    /**
     * 生成数据源
     * @return
     */
    public DataSource getDataSource() throws Exception{

        Properties properties = new Properties();
        properties.setProperty(DruidDataSourceFactory.PROP_DRIVERCLASSNAME,env.getProperty("jdbc.driverClassName"));
        properties.setProperty(DruidDataSourceFactory.PROP_URL,env.getProperty("jdbc.url"));
        properties.setProperty(DruidDataSourceFactory.PROP_USERNAME,env.getProperty("jdbc.username"));
        properties.setProperty(DruidDataSourceFactory.PROP_PASSWORD,env.getProperty("jdbc.password"));
        properties.setProperty(DruidDataSourceFactory.PROP_FILTERS,env.getProperty("jdbc.filters"));

        properties.setProperty(DruidDataSourceFactory.PROP_MAXWAIT,env.getProperty("jdbc.maxWait","60000"));
        properties.setProperty(DruidDataSourceFactory.PROP_MAXACTIVE,env.getProperty("jdbc.maxActive","15"));
        properties.setProperty(DruidDataSourceFactory.PROP_INITIALSIZE,env.getProperty("jdbc.initialSize","3"));
        properties.setProperty(DruidDataSourceFactory.PROP_MINIDLE,env.getProperty("jdbc.minIdle","5"));
        //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        properties.setProperty(DruidDataSourceFactory.PROP_TIMEBETWEENEVICTIONRUNSMILLIS,env.getProperty("jdbc.timeBetweenEvictionRunsMillis","60000"));
        properties.setProperty(DruidDataSourceFactory.PROP_MINEVICTABLEIDLETIMEMILLIS,env.getProperty("jdbc.minEvictableIdleTimeMillis","300000"));

            /*
            Oracle	select 1 from dual
            MySql	select 1
            postgresql	select version()
            */
        properties.setProperty(DruidDataSourceFactory.PROP_VALIDATIONQUERY,env.getProperty("jdbc.validationQuery","SELECT 1"));
        properties.setProperty(DruidDataSourceFactory.PROP_TESTWHILEIDLE,env.getProperty("jdbc.testWhileIdle","true"));
        properties.setProperty(DruidDataSourceFactory.PROP_TESTONBORROW,env.getProperty("jdbc.testOnBorrow","false"));
        properties.setProperty(DruidDataSourceFactory.PROP_TESTONRETURN,env.getProperty("jdbc.name","false"));
        properties.setProperty(DruidDataSourceFactory.PROP_TESTWHILEIDLE,env.getProperty("jdbc.testWhileIdle","true"));
            /*
            打开PSCache，并且指定每个连接上PSCache的大小
            Oracle，则把poolPreparedStatements配置为true，mysql可以配置为false
             */
        properties.setProperty(DruidDataSourceFactory.PROP_POOLPREPAREDSTATEMENTS,env.getProperty("jdbc.poolPreparedStatements","false"));
        properties.setProperty(DruidDataSourceFactory.PROP_MAXOPENPREPAREDSTATEMENTS,env.getProperty("jdbc.maxPoolPreparedStatementPerConnectionSize","20"));

        return DruidDataSourceFactory.createDataSource(properties);
    }


    @Bean(name = "multds")
    MultipleDataSource multipleDataSource()  throws Exception{
        MultipleDataSource multipleDataSource = new MultipleDataSource();
        Map<String,DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put(MultipleDataSourceHolder.MAIN,getDataSource());
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
