package com.shq.oper.model.dto.api.brandsquare;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;

@lombok.Data
public class GoodsProStandard implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	 * 分销价格
	 */
	private String distributionPrice;
	/*
	 * 零售价格
	 */
	private String goodsPrice;

}