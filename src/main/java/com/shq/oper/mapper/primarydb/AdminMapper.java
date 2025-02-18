package com.shq.oper.mapper.primarydb;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Admin;
import com.shq.oper.util.BaseMapper;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

	Page<Admin> queryByExcludeAdmin(Admin entity);
}