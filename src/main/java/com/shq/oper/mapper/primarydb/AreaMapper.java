package com.shq.oper.mapper.primarydb;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Area;
import com.shq.oper.util.BaseMapper;

@Mapper
public interface AreaMapper extends BaseMapper<Area> {

	/**
	 * 查询区域父节点信息
	 * 
	 * @param entity
	 * @param rowBounds
	 *            不分页 使用 RowBounds.DEFAULT
	 * @return
	 */
	Page<Area> selectTOAreaAll(Area area);

	/**
	 * 查询上级区域下的所有区域
	 * 
	 * @param
	 * @return
	 */
	Area queryWhinChilds(Area area);

	/**
	 * 根据区域级别查询区域
	 * 
	 * @param sLev
	 * @return
	 */
	List<Area> selectArea(Area areas);

}
