package com.shq.oper.mapper.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.deductible.DeductibleIntroduce;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeductibleIntroduceMapper extends BaseMapper<DeductibleIntroduce> {

	
	void updateAllToDisabled();
	Page<DeductibleIntroduce>queryAllDeductibleIntroduce(DeductibleIntroduce deductibleIntroduce);
	
}