package com.shq.oper.service.impl.primarydb;

import com.github.pagehelper.PageInfo;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.primarydb.ProductPrice;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.api.BaseResponseResultDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shq.oper.mapper.primarydb.ProductMapper;
import com.shq.oper.model.domain.primarydb.Product;
import com.shq.oper.model.dto.api.goods.Distribution;
import com.shq.oper.service.primarydb.ProductPriceService;
import com.shq.oper.service.primarydb.ProductService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("productService")
public class ProductServiceImpl extends BaseServiceImpl<Product, Long> implements ProductService {

	
	@Autowired
	private ProductPriceService productPriceService;


	@Autowired
	private DistributionProductMapper distributionProductMapper;

	@Autowired
	private ShqApi shqApi;

	private Data<ProductPrice> getProductPriceData(List<ProductPrice> prolist, Product product1) {
		PageInfo<ProductPrice> pages = new PageInfo<>(prolist);
		Data<ProductPrice> resultData =  Data.ok(pages);
		resultData.setMsg(JsonUtils.toDefaultJson(product1));
		return resultData;
	}

	@Transactional
	@Override
	public Data<DistributionProduct> searchGoods(String code){

		List<String> codeList=new ArrayList<>();
		codeList.add(code);
		List<DistributionProduct> list=distributionProductMapper.queryDiatributionProductByCodeList(codeList);
		if (null==list||list.size()<=0){
			return new Data<DistributionProduct>("false",list,1,null);
		}
		return new Data<DistributionProduct>("true",list,1,null);

	}
}
