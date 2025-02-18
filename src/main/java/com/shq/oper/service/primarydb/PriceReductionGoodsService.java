package com.shq.oper.service.primarydb;

import com.github.pagehelper.PageInfo;
import com.shq.oper.model.domain.mongo.PriceReductionGoods;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.PriceReductionGoodsDto;
import com.shq.oper.model.dto.api.homepage.PriceReductionGoodsExamineParamDto;

public interface PriceReductionGoodsService{

	PageInfo<PriceReductionGoodsDto> queryPriceReductionGoodsList(PriceReductionGoods entity, String productName, PageDto page);

	Msg<String> goodsExamine(PriceReductionGoodsExamineParamDto dto);
}