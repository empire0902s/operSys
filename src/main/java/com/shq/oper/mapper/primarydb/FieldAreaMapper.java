package com.shq.oper.mapper.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.ticket.Field;
import com.shq.oper.model.domain.primarydb.ticket.FieldArea;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FieldAreaMapper extends BaseMapper<FieldArea> {

	Page<FieldArea> queryFieldAreaList (FieldArea entity);
}
