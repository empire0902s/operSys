package com.shq.oper.mapperds;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.github.pagehelper.PageInterceptor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@MapperScan(basePackages = "com.shq.oper.mapper.shq520new", sqlSessionTemplateRef  = "shq520newSqlSessionTemplate")
@Slf4j
public class DataSource_SqlServer_shq520new {

    @Bean(name = "shq520newDataSource",destroyMethod = "close",initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource.shq520new")
    public DataSource testDataSource() {
    	log.info("----------shq520newDataSource--------------testDataSource---------------------");
    	return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
    }

    @Bean(name = "shq520newSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("shq520newDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setTypeAliasesPackage("com.shq.oper.model.domain.shq520new");
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers/shq520new/*.xml"));
        bean.setDataSource(dataSource);
        //bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:/config/mybatis-config-mssql.xml"));
        //分页插件	https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md
        Interceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "com.shq.oper.mapperds.CustSqlServer2012Dialect");
        //properties.setProperty("helperDialect", "sqlserver2012");
        //properties.setProperty("autoRuntimeDialect", "true");	//自动识别方言。默认Sqlserver2005
        properties.setProperty("reasonable", "false");//不启用智能分页，按照
        properties.setProperty("supportMethodsArguments","true");//
        properties.setProperty("params","pageNum=pageNumKey;pageSize=pageSizeKey;count=countSql;reasonable=reasonable;pageSizeZero=pageSizeZero");
        interceptor.setProperties(properties);
        bean.setPlugins(new Interceptor[] {interceptor});
        
        org.apache.ibatis.session.Configuration conf = new org.apache.ibatis.session.Configuration();
        conf.setMapUnderscoreToCamelCase(true);
		bean.setConfiguration(conf);
        
        return bean.getObject();
    }

    @Bean(name = "shq520newTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("shq520newDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "shq520newSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("shq520newSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}