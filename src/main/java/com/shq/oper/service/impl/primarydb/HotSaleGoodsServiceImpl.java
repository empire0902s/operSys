package com.shq.oper.service.impl.primarydb;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.shq.oper.dao.MongoDao;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.ApiRequestDataLog;
import com.shq.oper.model.domain.mongo.AppLocationData;
import com.shq.oper.model.domain.mongo.HomePageGoods;
import com.shq.oper.model.domain.mongo.PriceReductionGoods;
import com.shq.oper.model.domain.mongo.brand.HotSaleGoods;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.PriceReductionGoodsDto;
import com.shq.oper.model.dto.api.brandsquare.HotSaleGoodsDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.model.domain.mongo.brand.HotSaleGoods;
import com.shq.oper.service.primarydb.AppLocationDataService;
import com.shq.oper.service.primarydb.HotSaleGoodsService;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("hotSaleGoodsService")
public class HotSaleGoodsServiceImpl implements HotSaleGoodsService {

    
	@Autowired
	private MongoDao<HotSaleGoods> hotSaleGoodsMongo;
	
	@Autowired
	private DistributionProductMapper distributionProductMapper;
	
	@Autowired
	private ShqApi ShqApi;

	@Override
	public PageInfo<HotSaleGoodsDto> queryHotSaleGoodsList(HotSaleGoods entity,String productName, PageDto page) {

		
		
		List<String>codeList=new ArrayList<String>(0);
		/*	Query query = new Query();
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
		*/
		
		
		// 升序排序
		PageInfo<HotSaleGoods> findByQueryPage =new PageInfo<>();
		List<HotSaleGoods> resultList =new ArrayList<>();
		List<HotSaleGoods> resultListTmp =new ArrayList<>();
		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/hotSaleGoodsAction/listAllHotSaleGoodsForOperate";
		String resultJson = HttpClientUtil.doGet(url);
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		Object object = resultMap.get("data");
		resultList = JSONObject.parseArray(JsonUtils.toDefaultJson(object), HotSaleGoods.class); 
		if(resultList.size()<=0) {
			return new PageInfo<>();
		}
	
		
		if (!StringUtils.isEmpty(entity.getGoodsCode())&&StringUtils.isEmpty(entity.getActivateStatus())){
/*			resultList.stream().filter(
					hotSaleGoods ->hotSaleGoods.equals("")&&hotSaleGoods.equals("")
					).collect(Collectors.toList());
			
			
			*/
			for (HotSaleGoods hotSaleGoods : resultList) {
				if(!StringUtils.isEmpty(entity.getColumnId())) {
					if(!StringUtils.isEmpty(hotSaleGoods.getGoodsCode())) {
						if(hotSaleGoods.getGoodsCode().equals(entity.getGoodsCode())&&hotSaleGoods.getColumnId().equals(entity.getColumnId())) {
							resultListTmp.add(hotSaleGoods);
						}
					}
				}else {
					if(!StringUtils.isEmpty(hotSaleGoods.getGoodsCode())) {
						if(hotSaleGoods.getGoodsCode().equals(entity.getGoodsCode())) {
							resultListTmp.add(hotSaleGoods);
						}
					}
					
				}
			}
			findByQueryPage =new PageInfo<>(resultListTmp);
		}else if (!StringUtils.isEmpty(entity.getGoodsCode())&&!StringUtils.isEmpty(entity.getActivateStatus())){
			for (HotSaleGoods hotSaleGoods : resultList) {
				if(!StringUtils.isEmpty(entity.getColumnId())) {
					if(!StringUtils.isEmpty(hotSaleGoods.getGoodsCode())) {
						if(hotSaleGoods.getGoodsCode().equals(entity.getGoodsCode())&&entity.getActivateStatus().equals(hotSaleGoods.getActivateStatus())
								&&hotSaleGoods.getColumnId().equals(entity.getColumnId())) {
							resultListTmp.add(hotSaleGoods);
						}
					}
				}else {
				if(!StringUtils.isEmpty(hotSaleGoods.getGoodsCode())) {
					if(hotSaleGoods.getGoodsCode().equals(entity.getGoodsCode())&&entity.getActivateStatus().equals(hotSaleGoods.getActivateStatus())) {
						resultListTmp.add(hotSaleGoods);
					}
				}
				}
			}
			findByQueryPage =new PageInfo<>(resultListTmp);
		}else if (StringUtils.isEmpty(entity.getGoodsCode())&&!StringUtils.isEmpty(entity.getActivateStatus())){
			for (HotSaleGoods hotSaleGoods : resultList) {
				if(!StringUtils.isEmpty(entity.getColumnId())) {
					if(!StringUtils.isEmpty(hotSaleGoods.getGoodsCode())) {
						if(entity.getActivateStatus().equals(hotSaleGoods.getActivateStatus())&&entity.getColumnId().equals(hotSaleGoods.getColumnId())) {
							resultListTmp.add(hotSaleGoods);
						}
					}
				}else {
					if(!StringUtils.isEmpty(hotSaleGoods.getGoodsCode())) {
						if(entity.getActivateStatus().equals(hotSaleGoods.getActivateStatus())) {
							resultListTmp.add(hotSaleGoods);
						}
					}
				}
			}
			findByQueryPage =new PageInfo<>(resultListTmp);
		}
		else {
			findByQueryPage =new PageInfo<>(resultList);
		}
		
		
		
		if(findByQueryPage.getList().size()>0) {
			for (HotSaleGoods hotSaleGoods : findByQueryPage.getList()) {
				codeList.add(hotSaleGoods.getGoodsCode());
			}
		}else {
			return new PageInfo<>();
		}
		Page<DistributionProduct> queryDiatributionProductByCodeList = distributionProductMapper.queryDiatributionProductByCodeListAndGoodsName(codeList,productName);
		Map<String,DistributionProduct>map=new HashMap<>(0);
		if(queryDiatributionProductByCodeList.getResult().size()>0) {
			for (DistributionProduct ddistributionProduct : queryDiatributionProductByCodeList.getResult()) {
				map.put(ddistributionProduct.getProductCode(), ddistributionProduct);
			}
		}
		List<HotSaleGoodsDto>hotSaleGoodsDtoList=new ArrayList<>();
		List<HotSaleGoodsDto>hotSaleGoodsDtoTmpList=new ArrayList<>();
		PageInfo<HotSaleGoodsDto> result=new PageInfo<>(); 
		for (HotSaleGoods hotSaleGoods : findByQueryPage.getList()) {
			if(!StringUtils.isEmpty(map.get(hotSaleGoods.getGoodsCode()))) {
				HotSaleGoodsDto dto=new HotSaleGoodsDto();
				BeanUtils.copyProperties(hotSaleGoods, dto);
				dto.setGoodsName(map.get(hotSaleGoods.getGoodsCode()).getProductName());
				hotSaleGoodsDtoList.add(dto);
			}
		}
		int start=0,end=0;
		start=(page.getPage()-1)*page.getLimit();
        List<HotSaleGoodsDto> listGoods1=new ArrayList<>();
        if (hotSaleGoodsDtoList.size()>=start+page.getLimit()){
            listGoods1=hotSaleGoodsDtoList.subList(start,start+page.getLimit());
        }else if(hotSaleGoodsDtoList.size()>=start){
            listGoods1=hotSaleGoodsDtoList.subList(start,hotSaleGoodsDtoList.size());
        }
        result.setList(listGoods1);
		result.setPageNum(page.getPage());
		result.setPageSize(page.getLimit());
		result.setTotal(hotSaleGoodsDtoList.size());
		return result;
	}
	

}