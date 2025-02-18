package com.shq.oper.model.dto.api.orderingsystem;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "订货系统品牌广场DTO")
@lombok.Data
public class OSBrasndSquareDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long Id;

	/**
	 * 品牌商名称
	 */
	private String StoresName;

	private String FromTable = "BusinessStores";

	/**
	 * 图片相对路径
	 */
	private String StoresUrl;

	/**
	 * 图片绝对路径
	 */
	private String StrStoresUrl;

	/**
	 * 品牌商seq
	 */
	private String ContentUrl;

	/**
	 * 排序
	 */
	private String Sort;

	/*
	 * 标志
	 */
	private Long DealerId;

}