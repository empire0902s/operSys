package com.shq.oper.service.impl.primarydb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.shq.oper.dao.mongo.PriceReductionGoodsMongo;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.PriceReductionGoods;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.PriceReductionGoodsDto;
import com.shq.oper.model.dto.api.homepage.PriceReductionGoodsExamineParamDto;
import com.shq.oper.model.dto.api.homepage.PriceReductionGoodsParamDto;
import com.shq.oper.service.primarydb.PriceReductionGoodsService;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("priceReductionGoodsService")
public class PriceReductionGoodsServiceImpl implements PriceReductionGoodsService {

	@Autowired
	private PriceReductionGoodsMongo priceReductionGoodsMongo;
	
	@Autowired
	private DistributionProductMapper distributionProductMapper;
	
	

	@Override
	public PageInfo<PriceReductionGoodsDto> queryPriceReductionGoodsList(PriceReductionGoods entity,String productName, PageDto page) {
		
		List<String>codeList=new ArrayList<String>(0);
		Query query = new Query();
		DistributionProduct product=new DistributionProduct();
		List<DistributionProduct> productList =new ArrayList<>();
		
		
		//如果只是按照code查询
		if (!StringUtils.isEmpty(entity.getGoodsCode())&&StringUtils.isEmpty(productName)){
			query.addCriteria(Criteria.where("goodsCode").is(entity.getGoodsCode()));
		}
		//如果输入商品名称与商品code或者只是输入商品名称
		if (!StringUtils.isEmpty(productName)||(!StringUtils.isEmpty(productName)&&!StringUtils.isEmpty(entity.getGoodsCode()))){
				product.setProductName(productName);
				if(!StringUtils.isEmpty(entity.getGoodsCode())) {
					product.setProductCode(entity.getGoodsCode());
				}
				productList=distributionProductMapper.select(product);
				if(productList.size()>0) {
					DistributionProduct distributionProduct = productList.get(0);
					if (!StringUtils.isEmpty(distributionProduct.getProductCode())){
						query.addCriteria(Criteria.where("goodsCode").is(distributionProduct.getProductCode()));
					}
				}else {
					return new PageInfo<>();
				}
		}
		// 升序排序
		query.with(new Sort(new Order(Direction.ASC, "sortNum")));
		PageInfo<PriceReductionGoods> findByQueryPage = priceReductionGoodsMongo.findByQueryPage(query, page, entity);
		if(findByQueryPage.getList().size()>0) {
			for (PriceReductionGoods priceReductionGoods : findByQueryPage.getList()) {
				codeList.add(priceReductionGoods.getGoodsCode());
			}
		}else {
			return new PageInfo<>();
		}
		Page<DistributionProduct> queryDiatributionProductByCodeList = distributionProductMapper.queryDiatributionProductByCodeList(codeList);
		Map<String,DistributionProduct>map=new HashMap<>(0);
		if(queryDiatributionProductByCodeList.getResult().size()>0) {
			for (DistributionProduct ddistributionProduct : queryDiatributionProductByCodeList.getResult()) {
				map.put(ddistributionProduct.getProductCode(), ddistributionProduct);
			}
		}
		List<PriceReductionGoodsDto>priceReductionGoodsDtoList=new ArrayList<>();
		PageInfo<PriceReductionGoodsDto> result=new PageInfo<>(); 
		for (PriceReductionGoods priceReductionGoods : findByQueryPage.getList()) {
			if(!StringUtils.isEmpty(map.get(priceReductionGoods.getGoodsCode()))) {
				PriceReductionGoodsDto dto=new PriceReductionGoodsDto();
				BeanUtils.copyProperties(priceReductionGoods, dto);
				dto.setCompanyName(map.get(priceReductionGoods.getGoodsCode()).getCompanyName());
				dto.setProductName(map.get(priceReductionGoods.getGoodsCode()).getProductName());
				dto.setIsSale(map.get(priceReductionGoods.getGoodsCode()).getIsSale().toString());
				priceReductionGoodsDtoList.add(dto);
			}
		}
		result.setPageNum(findByQueryPage.getPageNum());
		result.setPageSize(findByQueryPage.getPageSize());
		result.setTotal(findByQueryPage.getTotal());
		result.setList(priceReductionGoodsDtoList);
		return result;
	}

	@Override
	public Msg<String> goodsExamine(PriceReductionGoodsExamineParamDto dto) {
		
		List<PriceReductionGoodsParamDto> param = dto.getExamineParam();
		List<String>errIdList=new ArrayList<>();
		List<String>errList=new ArrayList<>();
		int sucCount=0;
		for (PriceReductionGoodsParamDto priceReductionGoodsParamDto : param) {
			Query query = Query.query(Criteria.where("id").is(priceReductionGoodsParamDto.getPriceReductionGoodsId()));
	        Update update = new Update().set("financeState", priceReductionGoodsParamDto.getFinanceState())
	        		.set("financeAuditor",priceReductionGoodsParamDto.getFinanceAuditor())
	        		.set("financeAuditTime", LocalDateTime.now().toString())
	        		.set("updateTime", LocalDateTime.now().toString())
	        		.set("updateAdmin", priceReductionGoodsParamDto.getFinanceAuditor());
			try {
				int count = priceReductionGoodsMongo.updateFirstResurnWriteResult(query, update, PriceReductionGoods.class).getN();
				if(count==0) {
					errList.add(priceReductionGoodsParamDto.getPriceReductionGoodsId());
				}
				sucCount+=count;
			} catch (Exception e) {
				errIdList.add(priceReductionGoodsParamDto.getPriceReductionGoodsId());
			}
		}
		if(errIdList.size()>0) {
			return Msg.error("成功审核"+sucCount+"条数据,"+errIdList.size()+"条数据审核失败,id"+errIdList+"","");
		}
		if(errList.size()>0) {
			return Msg.ok("成功审核"+sucCount+"条数据,"+(errList.size()+"条数据审核失败,id"+errList+""),"");
		}
		return Msg.ok("全部审核成功","");
	}


}