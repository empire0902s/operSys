package com.shq.oper.model.domain.mongo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

/** 爱之家首页模块 Banner表 **/
@lombok.Data
public class HomePageBanner implements Serializable {
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
	
	/**
	 * 排序值 默认1000
	 */
	private Integer sortNum;
	
	/** 图片标题 	 **/
	private String imgTitle;
	
	/** 图片路径 	 **/
	private String imgUrl;
	
	/** 图片跳转连接 	 **/
	private String imgTarget;

	private String groupItemId;

	private String goodsItemId;


	
	
	
	
	
	
	
	
	
	
	
}