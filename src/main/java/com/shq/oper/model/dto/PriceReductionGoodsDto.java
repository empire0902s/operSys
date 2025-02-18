package com.shq.oper.model.dto;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

/** 砍价商品设DTO **/
@lombok.Data
public class PriceReductionGoodsDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;

	private String createTime;
	
	private String createAdmin;
	
	private String updateTime;
	
	private String updateAdmin;
	
	

	/** 商品Code 	 **/
	private String goodsCode = "";
	
	/** 商品名称 	 **/
	private String productName = "";
	/** 供销商名称 	 **/
	private String companyName = "";
	
	
	
	/**
	 * 排序值 默认1000
	 */
	private Integer sortNum = 1000;

	/** 砍价价格 	 **/
	private String reductionGoodsPrice = "";
	
	/** 砍价次数设置 	 **/
	private Integer reductionNumRule = 0;
	
	/** 砍价商品数量	 **/
	private Integer reductionGoodsNum = 0;
	
	/** 推荐人佣金 	 **/
	private String brokerageMoney = "";
	
	/** 砍价商品状态(0:下架,1上架) 只有财务审核通过,才能上架操作 	 **/
	private Integer showState = 0;

	
	
	/** 财务审核状态(0未审核,1审核通过,2审核不通过)	 **/
	private Integer financeState = 0;
	
	/** 财务审核人 	 **/
	private String financeAuditor = "";
	
	/** 财务审核时间 	 **/
	private String financeAuditTime = "";
	
	/**分销后台Net段数据同步状态 **/
	private Integer syncStateNet = 0;
	
	/*
	 * 商品上下架状态
	 */
	private String isSale;
	
	
	
}