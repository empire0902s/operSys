package com.shq.oper.model.dto.api.homepage;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "砍价商品接口DTO")
@lombok.Data
public class PriceReductionGoodsParamDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 砍价商品Id
	 */
	private String priceReductionGoodsId;

	/** 财务审核状态(0未审核,1审核通过,2审核不通过) **/
	private Integer financeState = 0;

	/** 财务审核人 **/
	private String financeAuditor = "";
	
	
	

}