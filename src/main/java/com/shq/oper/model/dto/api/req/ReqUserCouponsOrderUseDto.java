package com.shq.oper.model.dto.api.req;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



@ApiModel(value="使用优惠券结算")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqUserCouponsOrderUseDto  implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "商品Code标识List")
	private String goodsCodeList;
	
	@ApiModelProperty(value = "用户标识")
	private String userSeq;
	
	@ApiModelProperty(value = "结算订单号")
	private String useOrderNo;
	
	@ApiModelProperty(value = "用户优惠券标识")
	private Long couponsUserId;	

	@ApiModelProperty(value = "订单金额")
	private BigDecimal useOrderMoney;

	@ApiModelProperty(value = "订单优惠金额")
	private BigDecimal useSpendMoney;
	
	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间

	@ApiModelProperty(hidden=true)
	private String useDesc;	//结算描述

	
	
}