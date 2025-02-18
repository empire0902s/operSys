package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.ActivityGoodsRule;
import com.shq.oper.model.dto.Msg;

public interface ActivityGoodsRuleService extends BaseService<ActivityGoodsRule, Long> {

    Msg<String> disabled(ActivityGoodsRule activityGoodsRule);


}