package com.shq.oper.model.dto.api.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



@ApiModel(value="锁定状态订单操作")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqUserCouponsOrderLock  implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户标识")
	private String userSeq;

	@ApiModelProperty(value = "结算订单号")
	private String useOrderNo;

	@ApiModelProperty(value = "用户优惠券标识")
	private Long couponsUserId;

	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间

	@ApiModelProperty(hidden=true)
	private String useDesc;	//结算描述

	
	
}