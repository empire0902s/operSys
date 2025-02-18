package com.shq.oper.model.domain.mongo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

/** 爱之家首页模块 附加属性表 **/
@lombok.Data
public class HomePageModuleAttached implements Serializable {
	private static final long serialVersionUID = 1L;
	/****/
	@Id
	private String id;

	private String createTime;
	
	private String createAdmin;
	
	private String updateTime;
	
	private String updateAdmin;
	
	
	/** 模块Id 	 **/
	private String moduleId;
	
	
	/**logo图片链接 	 **/
	private String logoURL;
	/**logo图片链接跳转 	 **/
	private String logoURLTarget;
	
	/** Banner图片链接	 **/
	private String logoBannerURL;
	/** Banner图片链接跳转	 **/
	private String logoBannerURLTarget;

	
	/** 活动图片链接 	 **/
	private String activeBannerURL;
	/** 活动图片链接跳转 	 **/
	private String activeBannerURLTarget;


	
	
	
	
	
	
	
	
	
	
	
}