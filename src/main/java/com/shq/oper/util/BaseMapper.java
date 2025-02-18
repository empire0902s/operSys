package com.shq.oper.util;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.rowbounds.SelectRowBoundsMapper;

/**
 * 自定义通用 Mapper
 *
 * @param <T>
 * @author tjun
 */
public abstract interface BaseMapper<T> extends
		BaseCustomMapper<T>,
        tk.mybatis.mapper.common.BaseMapper<T>,
        MySqlMapper<T>,
        IdsMapper<T>,
        SelectRowBoundsMapper<T> {
	
}