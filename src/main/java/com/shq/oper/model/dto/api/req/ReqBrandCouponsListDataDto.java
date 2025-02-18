package com.shq.oper.model.dto.api.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



@ApiModel(value="1010品牌店铺可领取优惠券列表DTO")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqBrandCouponsListDataDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "来源系统")
	private String fromSys;	//来源系统
	
	@ApiModelProperty(value = "来源系统Code")
	private String fromSysCode;	//来源系统Code

	@ApiModelProperty(value = "活动批次号标识")
	private String actBatch;	//活动批次号标识
	
	
	@ApiModelProperty(value = "用户标识")
	private String userSeq;
	
	@ApiModelProperty(value = "页码")
	private Integer page;	
	
	@ApiModelProperty(value = "页码大小")
	private Integer limit;

	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间
	
}