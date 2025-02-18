package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.Product;
import com.shq.oper.model.domain.primarydb.ProductPrice;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.api.goods.Distribution;

public interface ProductService extends BaseService<Product, Long> {

	Data<DistributionProduct> searchGoods(String code);
	
}