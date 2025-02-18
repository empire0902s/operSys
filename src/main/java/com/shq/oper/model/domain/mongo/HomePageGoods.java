package com.shq.oper.model.domain.mongo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** 爱之家首页模块Goods表 **/
@lombok.Data
public class HomePageGoods implements Serializable {
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
	

	/** 商品Code 	 **/
	private String goodsCode;

	private String groupItemId;

	private String goodsItemId;

	//供应商
	private String companyName;

	private String productName;

	private Integer isSale;








//	/** 商品id 	 **/
//	private String goodsId;
//	
//	/** 商品名称标题 	 **/
//	private String goodsTitle;
//	
//	/** 商品显示价格 	 **/
//	private String goodsShowPrice;
//	
//	/** 商品禁用价格 	 **/
//	private String goodsDisablePrice;
//	
//	
//	
//	
//	/** 商品 图片标题 	 **/
//	private String goodsImgTitle;
//	
//	/** 商品 图片路径 	 **/
//	private String goodsImgUrl;
//	
//	/** 商品 图片跳转连接 	 **/
//	private String goodsImgTarget;
	
	
	
	
	
	
	
}