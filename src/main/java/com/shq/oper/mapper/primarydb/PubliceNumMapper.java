package com.shq.oper.mapper.primarydb;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.PubliceNum;
import com.shq.oper.util.BaseMapper;

@Mapper
public interface PubliceNumMapper extends BaseMapper<PubliceNum> {

	void updateAllToDisabled();
	Page<PubliceNum>queryAllPubliceNum(PubliceNum publiceNum);
}