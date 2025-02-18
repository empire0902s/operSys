package com.shq.oper.mapper.primarydb;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.shq.oper.model.domain.primarydb.RoleResource;
import com.shq.oper.util.BaseMapper;

@Mapper
public interface RoleResourceMapper extends BaseMapper<RoleResource> {
	
	@Select("select resource_id from t_role_resource t where t.role_id=#{roleId}")
	public List<Long> selectByRoleId(Long roleId);
}
