package com.shq.oper.model.dto.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@ApiModel(value="结算订单页优惠券列表DTO")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqUserCouponsBusinessDto implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 来源系统
	 */
	private String fromSys;
	/**
	 * 来源系统Code
	 */
	private String fromSysCode;

	private List<BusinessParamDto> businessParam;
	
	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间

	
	
}