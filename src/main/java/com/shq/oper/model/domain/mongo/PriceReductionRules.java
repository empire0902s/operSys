package com.shq.oper.model.domain.mongo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** 砍价规则 **/
@lombok.Data
public class PriceReductionRules implements Serializable {
	private static final long serialVersionUID = 1L;
	/****/
	@Id
	private String id;

	private String updateTime;
	
	private String updateAdmin;
	

	/**
	 * 砍价活动有效周期 单位[小时] 默认24小时
	 */
	private Integer effectivePeriodHour = 24;
	
	
	/**
	 * 用户每日砍价次数限制 默认 3次
	 */
	private Integer userDayReductionTimes = 3;
	
	
	/**
	 * 同一件商品 最大重复砍价次数 默认【1】 次
	 */
	private Integer goodsRepeatedReductionTimes = 1;
	
	/**
	 * 同一件商品 可重复砍价周期 单位[小时] 默认24小时
	 */
	private Integer goodsRepeatedPeriodHour = 24;
	
	
}