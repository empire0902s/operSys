package com.shq.oper.model.dto.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.io.Serializable;


@ApiModel(value="请求发圈数据接口DTO")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqCirCleListDataDto implements Serializable{
	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "页码")
	private Integer page;	
	
	@ApiModelProperty(value = "页码大小")
	private Integer limit;

	//1：每日爆款，2每日推荐
	private Short type;

	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间
	
}