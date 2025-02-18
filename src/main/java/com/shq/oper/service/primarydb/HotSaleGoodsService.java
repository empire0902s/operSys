package com.shq.oper.service.primarydb;

import com.github.pagehelper.PageInfo;
import com.shq.oper.model.domain.mongo.brand.HotSaleGoods;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.brandsquare.HotSaleGoodsDto;

public interface HotSaleGoodsService {


	PageInfo<HotSaleGoodsDto> queryHotSaleGoodsList(HotSaleGoods entity, String productName, PageDto page);


	
}