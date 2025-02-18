package com.shq.oper.mapper.primarydb;

import com.shq.oper.model.domain.primarydb.OrderEvaluateLikes;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderEvaluateLikesMapper extends BaseMapper<OrderEvaluateLikes> {
	
	int queryLikesCount(OrderEvaluateLikes param);
	
}