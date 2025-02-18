package com.shq.oper.model.dto.api.req.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


@ApiModel(value="7001请求渠道模板接口DTO")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqQuerychannelTemplateDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "渠道名")
	private String name;	//正常

	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间

	private String goodUniqeKey;

	private String bannerUniqeKey;

	@ApiModelProperty(value = "页码")
	private Integer page;

	@ApiModelProperty(value = "页码大小")
	private Integer limit;


}