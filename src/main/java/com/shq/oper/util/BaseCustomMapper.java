package com.shq.oper.util;

import org.apache.ibatis.annotations.UpdateProvider;

/**
 * 自定义通用 Mapper
 *
 * @param <T>
 * @author tjun
 */
public interface BaseCustomMapper<T> {
	
	@UpdateProvider(type=SQLProvider.class, method="disabledById")
	int disabledById(T record);
	@UpdateProvider(type=SQLProvider.class, method="enableById")
	int enableById(T record);
	
}