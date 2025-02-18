package com.shq.oper.model.dto.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;


@ApiModel(value="赠送用户优惠券")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqGiftUserCoupons implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户标识")
	private String userSeq;

	@ApiModelProperty(value = "赠送目标用户标识")
	private String targetUserSeq;
	
	@ApiModelProperty(value = "赠送金额")
	private BigDecimal amount;

	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间

	@ApiModelProperty(hidden=true)
	private String useDesc;	//结算描述

	
	
}