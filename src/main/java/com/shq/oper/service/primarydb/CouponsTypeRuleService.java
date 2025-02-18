package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.CouponsTypeRule;
import com.shq.oper.model.dto.Msg;

public interface CouponsTypeRuleService extends BaseService<CouponsTypeRule, Long> {

	Msg<String> disabled(CouponsTypeRule couponsType);
}