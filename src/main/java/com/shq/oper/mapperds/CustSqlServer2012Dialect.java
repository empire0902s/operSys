package com.shq.oper.mapperds;


import com.github.pagehelper.Page;
import com.github.pagehelper.cache.Cache;
import com.github.pagehelper.cache.CacheFactory;
import com.github.pagehelper.dialect.AbstractHelperDialect;
import com.github.pagehelper.parser.SqlServerParser;
import com.github.pagehelper.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * @author tjun
 */
@Slf4j
public class CustSqlServer2012Dialect extends AbstractHelperDialect {
    protected SqlServerParser pageSql = new SqlServerParser();
    protected Cache<String, String> CACHE_COUNTSQL;
    protected Cache<String, String> CACHE_PAGESQL;

    //with(nolock)
    protected String WITHNOLOCK = ", PAGEWITHNOLOCK";

    @Override
    public String getCountSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey countKey) {
        String sql = boundSql.getSql();
        String cacheSql = CACHE_COUNTSQL.get(sql);
        if (cacheSql != null) {
            return cacheSql;
        } else {
            cacheSql = sql;
        }
        cacheSql = cacheSql.replaceAll("((?i)with\\s*\\(nolock\\))", WITHNOLOCK);
        cacheSql = countSqlParser.getSmartCountSql(cacheSql);
        cacheSql = cacheSql.replaceAll(WITHNOLOCK, " with(nolock)");
        CACHE_COUNTSQL.put(sql, cacheSql);
        log.debug("----------build-----countSql---");
        return cacheSql;
    }

    @Override
    public String getPageSql(String sql, Page page, CacheKey pageKey) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
        sqlBuilder.append(sql);
        sqlBuilder.append(" OFFSET ");
        sqlBuilder.append(page.getStartRow());
        sqlBuilder.append(" ROWS ");
        pageKey.update(page.getStartRow());
        sqlBuilder.append(" FETCH NEXT ");
        sqlBuilder.append(page.getPageSize());
        sqlBuilder.append(" ROWS ONLY");
        pageKey.update(page.getPageSize());
        log.debug("----------build-----getPageSql----");
        return sqlBuilder.toString();
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String sqlCacheClass = properties.getProperty("sqlCacheClass");
        if (StringUtil.isNotEmpty(sqlCacheClass) && !sqlCacheClass.equalsIgnoreCase("false")) {
            CACHE_COUNTSQL = CacheFactory.createCache(sqlCacheClass, "count", properties);
            CACHE_PAGESQL = CacheFactory.createCache(sqlCacheClass, "page", properties);
        } else {
            CACHE_COUNTSQL = CacheFactory.createCache(null, "count", properties);
            CACHE_PAGESQL = CacheFactory.createCache(null, "page", properties);
        }
    }
}
