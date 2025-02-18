package com.shq.oper.model.dto.api.req;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



@ApiModel(value="结算订单页优惠券列表DTO")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqUserCouponsOrderPrevDto  implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "商品Code标识List")
	private String goodsCodeList;

	@ApiModelProperty(value = "订单金额")
	private BigDecimal orderMoney;

	@ApiModelProperty(value = "用户标识")
	private String userSeq;

	@ApiModelProperty(value = "页码")
	private Integer page;	
	
	@ApiModelProperty(value = "页码大小")
	private Integer limit;

	
	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间

	
	
}