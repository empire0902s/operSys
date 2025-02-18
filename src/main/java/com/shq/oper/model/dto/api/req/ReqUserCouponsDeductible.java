package com.shq.oper.model.dto.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;


@ApiModel(value="抵扣券使用")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqUserCouponsDeductible implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户标识")
	private String userSeq;
	
	@ApiModelProperty(value = "结算订单号")
	private String useOrderNo;
	
	@ApiModelProperty(value = "用户优惠券标识")
	private Long couponsUserId;

	@ApiModelProperty(value = "抵扣金额")
	private BigDecimal deductibleAmount;

	private Long id;

	/**
	 * 1：使用，2：还原
	 */
	@ApiModelProperty(value = "类型")
	private Short type;

	/**
	 * 1：代金券，2：抵扣券
	 */
	private Integer couponsType;

	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间

	@ApiModelProperty(hidden=true)
	private String useDesc;	//结算描述

	
	
}