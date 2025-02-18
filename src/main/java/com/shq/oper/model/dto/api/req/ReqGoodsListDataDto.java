package com.shq.oper.model.dto.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


@ApiModel(value="请求优惠券商品范围列表接口DTO")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqGoodsListDataDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "优惠券id")
	private Long id;	//正常

	@ApiModelProperty(value = "来源系统")
	private String fromSys;

	@ApiModelProperty(value = "来源系统code")
	private String fromSysCode;
	
	@ApiModelProperty(value = "页码")
	private Integer page;	
	
	@ApiModelProperty(value = "页码大小")
	private Integer limit;

	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间
	
}