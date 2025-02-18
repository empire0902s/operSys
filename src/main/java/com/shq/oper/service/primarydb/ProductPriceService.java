package com.shq.oper.service.primarydb;

import java.util.List;

import com.shq.oper.model.domain.primarydb.ProductPrice;

public interface ProductPriceService extends BaseService<ProductPrice, Long> {
	List<ProductPrice> select(ProductPrice entity);
	void saveProductPriceList(String adminName, List<ProductPrice> productPriceList);
}