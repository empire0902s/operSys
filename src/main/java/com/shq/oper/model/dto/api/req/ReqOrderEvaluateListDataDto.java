package com.shq.oper.model.dto.api.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



@ApiModel(value="请求添加订单评价信息DTO")
@lombok.Data
public class ReqOrderEvaluateListDataDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "订单编号")
	private String orderNo;
	
	@ApiModelProperty(value = "商品code")
	private String goodsCode;
	
	@ApiModelProperty(value = "审核类型")
	private String auditType;
	
	@ApiModelProperty(value = "审核状态")
	private String auditStats;
	
	@ApiModelProperty(value = "评价用户seq")
	private String userSeq;
	
	@ApiModelProperty(value = "页码")
	private Integer page;	
	
	@ApiModelProperty(value = "页码大小")
	private Integer limit;


}