package com.shq.oper.service.primarydb;

import java.util.List;

import com.shq.oper.model.domain.primarydb.CouponsAreaRule;

public interface CouponsAreaRuleService extends BaseService<CouponsAreaRule, Long> {
	
	List<CouponsAreaRule> selectArea(Integer sLev);
}
