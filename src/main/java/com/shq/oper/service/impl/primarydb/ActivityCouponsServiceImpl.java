package com.shq.oper.service.impl.primarydb;

import com.shq.oper.model.domain.primarydb.ActivityCoupons;
import com.shq.oper.service.primarydb.ActivityCouponsService;

import org.springframework.stereotype.Service;

@Service("activityCouponsService")
public class ActivityCouponsServiceImpl extends BaseServiceImpl<ActivityCoupons, Long> implements ActivityCouponsService {
}