package com.shq.oper.service.impl.primarydb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shq.oper.mapper.primarydb.CouponsAreaRuleMapper;
import com.shq.oper.model.domain.primarydb.CouponsAreaRule;
import com.shq.oper.service.primarydb.CouponsAreaRuleService;

@Service("couponsAreaRuleService")
public class CouponsAreaRuleServiceImpl extends BaseServiceImpl<CouponsAreaRule, Long>
		implements CouponsAreaRuleService {

	@Autowired
	private CouponsAreaRuleMapper cAreaRuleMapper;

	public List<CouponsAreaRule> selectArea(Integer sLev) {
		return cAreaRuleMapper.selectArea(sLev);
	}

}
