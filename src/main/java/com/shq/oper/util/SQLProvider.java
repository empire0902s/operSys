package com.shq.oper.util;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;


public class SQLProvider extends MapperTemplate {

	public SQLProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

    /**
     * 通过主键 禁用
     * @param ms
     */
    public String disabledById(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append("<set>");
        sql.append("is_disabled= true,");
        sql.append("update_admin=#{updateAdmin},");
        sql.append("update_time=#{updateTime}");
        sql.append("</set>");
        sql.append(SqlHelper.wherePKColumns(entityClass));
        return sql.toString();
    }
    /**
     * 通过主键 启用
     * @param ms
     */
    public String enableById(MappedStatement ms) {
    	Class<?> entityClass = getEntityClass(ms);
    	StringBuilder sql = new StringBuilder();
    	sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
    	sql.append("<set>");
    	sql.append("is_disabled=false,");
    	sql.append("update_admin=#{updateAdmin},");
    	sql.append("update_time=#{updateTime}");
    	sql.append("</set>");
    	sql.append(SqlHelper.wherePKColumns(entityClass));
    	return sql.toString();
    }
	

}
