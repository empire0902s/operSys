package com.shq.oper.mapperds;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Configuration
@MapperScan(basePackages = "com.shq.oper.mapper.primarydb", sqlSessionTemplateRef  = "primarySqlSessionTemplate")
@Slf4j
public class DataSourcePrimary {

    @Bean(name = "primaryDataSource",destroyMethod = "close",initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    @Primary
    public DataSource testDataSource() {
    	log.info("----------primaryDataSource--------------testDataSource---------------------");
        return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
    }

    @Bean(name = "primarySqlSessionFactory")
    @Primary
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("com.shq.oper.model.domain.primarydb");
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers/primarydb/*.xml"));
        
        org.apache.ibatis.session.Configuration mybariesCfg = new org.apache.ibatis.session.Configuration();
        mybariesCfg.setMapUnderscoreToCamelCase(true);
        mybariesCfg.setCacheEnabled(false);
        mybariesCfg.setLazyLoadingEnabled(false);
        mybariesCfg.setLogImpl(org.apache.ibatis.logging.slf4j.Slf4jImpl.class);
         
		bean.setConfiguration(mybariesCfg);
        //bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:/config/mybatis-config-primary.xml"));
        return bean.getObject();
    }

    @Bean(name = "primaryTransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "primarySqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}