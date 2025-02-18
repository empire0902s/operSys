package com.shq.oper.model.domain.mongo.channel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/** 渠道模块 Banner表 **/
@lombok.Data
@Document(collection="t_banner")
public class ChannelBanner implements Serializable {
	private static final long serialVersionUID = 1L;
	/****/
	@Id
	private String id;

	private String createTime;
	
	private String createAdmin;
	
	private String updateTime;
	
	private String updateAdmin;

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

	private String uniqeKey;




	
	
	
	
	
	
	
	
	
	
	
}