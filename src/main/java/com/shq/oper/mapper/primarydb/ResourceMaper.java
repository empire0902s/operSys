package com.shq.oper.mapper.primarydb;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Resource;
import com.shq.oper.util.BaseMapper;

/**
 *@author ltg
 *@date 2018/4/18 11:41
 */
@Mapper
public interface ResourceMaper extends BaseMapper<Resource> {

	/**
	 * 查询菜单资源及父节点信息
	 * @param entity
	 * @param rowBounds 不分页 使用 RowBounds.DEFAULT
	 * @return
	 */
	Page<Resource> queryWithParent(Resource entity);

	/**
	 *查询菜单父类下的所有菜单备注列表
	 * @param
	 * @return
	 */
	Page<String> queryWhinChilds(Resource entity);

	Page<Resource> queryResourceeByAdmin(Resource resource);

	Resource queryResourceDetailById(long id);
}
