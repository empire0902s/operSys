package com.shq.oper.service.impl.primarydb;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.shq.oper.mapper.primarydb.CouponsMapper;
import com.shq.oper.mapper.primarydb.CouponsTypeRuleMapper;
import com.shq.oper.model.domain.primarydb.Coupons;
import com.shq.oper.model.domain.primarydb.CouponsTypeRule;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.primarydb.CouponsTypeRuleService;
import com.shq.oper.util.CacheKeyConstant;

@Service("couponsTypeRuleService")
public class CouponsTypeRuleServiceImpl extends BaseServiceImpl<CouponsTypeRule, Long>
		implements CouponsTypeRuleService {

	
	@Autowired
	private CouponsTypeRuleMapper couponsTypeRuleMapper;
	@Autowired
	private CouponsMapper couponsMapper;

	@Transactional
	@Override
	public Msg<String> disabled(CouponsTypeRule couponsType){
		couponsType.setUpdateTime(LocalDateTime.now());
		couponsType.setIsDisabled(true);
		couponsTypeRuleMapper.disabledById(couponsType);
		Coupons coupons=new Coupons();
		coupons.setCouponsRuleId(couponsType.getId().toString());
		List<Coupons> couponsList=couponsMapper.select(coupons);
		if(couponsList.size()<=0) {
			return Msg.ok("禁用成功");
		}
		for (Coupons coupons2 : couponsList) {
			coupons2.setIsDisabled(true);
		}
		couponsMapper.updateBath(couponsList);
		//清除Coupons-query- 的缓存
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONS_QUERY);
		return Msg.ok("禁用成功");
	}

}