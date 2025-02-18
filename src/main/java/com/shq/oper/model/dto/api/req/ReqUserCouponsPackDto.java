package com.shq.oper.model.dto.api.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



@ApiModel(value="请求查询用户优惠券列表DTO")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqUserCouponsPackDto  implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户标识")
	private String userSeq;
	
	@ApiModelProperty(value = "优惠券状态(1未使用,2锁定中,3已使用,4已过期)")
	private Integer couponsStatus;	

	@ApiModelProperty(value = "页码")
	private Integer page;	
	
	@ApiModelProperty(value = "页码大小")
	private Integer limit;

	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间

	
}