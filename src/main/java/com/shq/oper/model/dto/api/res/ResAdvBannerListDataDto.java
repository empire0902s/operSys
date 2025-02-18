package com.shq.oper.model.dto.api.res;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "app启动页广告返回数据接口DTO", description = "app启动页广告返回数据接口DTO")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ResAdvBannerListDataDto implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private String AppPictureUrl;	//来源系统
	
	private String ActivityUrl;	//来源系统Code

	

	
	
}