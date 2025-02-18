package com.shq.oper.model.dto.api.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



@ApiModel(value="1007请求活动列表接口DTO")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqActivityListDataDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	@ApiModelProperty(value = "来源系统")
	private String fromSys;	//来源系统
	
	@ApiModelProperty(value = "来源系统Code")
	private String fromSysCode;	//来源系统Code
	
	@ApiModelProperty(value = "活动批次号标识")
	private String actBatch;	//正常
	
	@ApiModelProperty(value = "页码")
	private Integer page;	
	
	@ApiModelProperty(value = "页码大小")
	private Integer limit;
	
	@ApiModelProperty(value = "角色用户规则")
	private Integer userRoleRule;

	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间
	
}