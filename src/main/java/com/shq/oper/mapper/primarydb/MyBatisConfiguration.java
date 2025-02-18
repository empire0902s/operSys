package com.shq.oper.mapper.primarydb;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.pagehelper.PageHelper;

/**
 * MyBatis 配置
 *
 */
//@Configuration
public class MyBatisConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisConfiguration.class);

//    @Bean
    public PageHelper pageHelper() {
        logger.info("注册MyBatis分页插件PageHelper");
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
//        p.setProperty("dialect", "com.github.pagehelper.dialect.helper.SqlServer2012Dialect");
        //p.setProperty("offsetAsPageNum", "true");
        p.setProperty("helperDialect", "sqlserver2012");
        p.setProperty("autoRuntimeDialect", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

}
