package com.shq.oper.model.dto.api.brandsquare;

import java.io.Serializable;

import com.shq.oper.model.domain.mongo.brand.HotSaleGoods;

import io.swagger.annotations.ApiModel;

@lombok.Data
public class HotSaleDataDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private HotSaleGoods hotSaleData=new HotSaleGoods();

}