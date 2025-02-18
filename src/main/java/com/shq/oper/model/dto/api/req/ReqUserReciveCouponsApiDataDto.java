package com.shq.oper.model.dto.api.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



@ApiModel(value="请求用户领取优惠券DTO")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqUserReciveCouponsApiDataDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "活动批次号标识")
	private String actBatch;	//正常
	
	@ApiModelProperty(value = "用户序列号")
	private String userSeq;	
	
	@ApiModelProperty(value = "用户手机号")
	private String userPhone;	
	
	@ApiModelProperty(value = "优惠券标识")
	private Long couponsId;	
	
	
}