package com.shq.oper.model.dto.api.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "首页邀请页列表数据接口参数Dto")
@lombok.Data
public class HomePageInvitationPageParamDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 系统来源
	 */
	private String fromSys;

	/**
	 * 系统来源Code
	 */
	private String fromSysCode;


	private Integer limit;

	private Integer page;

}