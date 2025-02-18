package com.shq.oper.model.dto.api.homepage;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "砍价商品财务审核接口DTO")
@lombok.Data
public class PriceReductionGoodsExamineParamDto implements Serializable {
	private static final long serialVersionUID = 1L;

	
	List<PriceReductionGoodsParamDto>examineParam;
	

}