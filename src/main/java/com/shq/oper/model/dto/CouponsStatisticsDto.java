package com.shq.oper.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Transient;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "返回优惠券统计对象Dto", description = "返回优惠券统计对象Dto")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class CouponsStatisticsDto implements Serializable {
	private Long id;

	private LocalDateTime statisticsTime;

	/**
	 * 新增数量
	 */
	private Integer newAddCount;

	/**
	 * 新增订单总金额
	 */
	private Integer newAddTotalOrderMoney;
	/**
	 * 新增优惠总金额
	 */
	private Integer newAddTotalSpendMoney;

	/**
	 * 发放数量
	 */
	private Integer totalSendNums;

	/**
	 * 使用数量
	 */
	private Integer totalUseCounts;
	/**
	 * 订单总金额
	 */
	private Integer totalOrderMoney;
	/**
	 * 总优惠金额
	 */
	private Integer totalSpendMoney;

	private Integer numberOfUsingcoupons;

	//已使用代金券
	private Integer totalAmount;
//
	//抵扣券总额
	private Integer totalDeductible;

	//抵扣券余额
	private Integer totalBalance;

	//已抵扣总额
	private Integer totalUsedBalance;


	private static final long serialVersionUID = 1L;

}