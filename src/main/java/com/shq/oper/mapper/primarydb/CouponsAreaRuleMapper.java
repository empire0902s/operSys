package com.shq.oper.mapper.primarydb;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shq.oper.model.domain.primarydb.CouponsAreaRule;
import com.shq.oper.util.BaseMapper;

@Mapper
public interface CouponsAreaRuleMapper extends BaseMapper<CouponsAreaRule> {

 
	List<CouponsAreaRule> selectArea(Integer sLev);

}
