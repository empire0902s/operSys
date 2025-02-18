package com.shq.oper.model.domain.mongo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;

/** 爱之家首页模块 组合Banner和Goods表 **/
@lombok.Data
public class HomePageGroupItem implements Serializable {
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

	/**
	 * 组合名称
	 */
	private String groupName;


	/** 商品Code 	 **/
//	private List<HomePageGroupItemDetail> listGoodsItem;

	/**
	 * 组合模块 Banner 列表
	 */
	private List<HomePageBanner> itemListBanner;

	/**
	 * 组合模块 商品goods 列表
	 */
	private List<HomePageGoods> itemListGoods;










}