package com.shq.oper.model.domain.mongo.channel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/** 渠道Goods表 **/
@lombok.Data
@Document(collection="t_goods")

public class Goods implements Serializable {
	private static final long serialVersionUID = 1L;
	/****/
	@Id
	private String id;

	private String uniqeKey;

	private String createTime;

	private String createAdmin;
	
	/**
	 * 排序值 默认1000
	 */
	private Integer sortNum;

	/** 商品Code 	 **/
	private String goodsCode;

	//供应商
	private String companyName;

	private String productName;

	//1上架 2下架
	private Integer isSale;

	private String modelName;

	private String modelId;

	private String from;

	/**
	 * 0：平台商品，1品牌商品
	 */
	private Integer type;

	
}