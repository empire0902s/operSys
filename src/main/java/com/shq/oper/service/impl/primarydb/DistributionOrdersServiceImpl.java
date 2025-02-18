package com.shq.oper.service.impl.primarydb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.Page;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.shq520new.DistributionOrdersMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.shq520new.DistributionOrders;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.salestatistics.ActivityCommodityOrderStatisticsDto;
import com.shq.oper.model.dto.salestatistics.CategoryIdStatisticsRankingDto;
import com.shq.oper.model.dto.salestatistics.CommodityOrderStatisticsDto;
import com.shq.oper.model.dto.salestatistics.CommodityStatisticsRankingDto;
import com.shq.oper.model.dto.salestatistics.GenreIdStatisticsRankingDto;
import com.shq.oper.model.dto.salestatistics.LoveOfHomeDataCountStatisticsDto;
import com.shq.oper.model.dto.salestatistics.OrderCountStatisticsDto;
import com.shq.oper.model.dto.salestatistics.OrderTestCountStatisticsDto;
import com.shq.oper.model.dto.salestatistics.SupplierRankingStatisticsDto;
import com.shq.oper.model.dto.salestatistics.UnpaidOrderStatisticsDto;
import com.shq.oper.service.primarydb.DistributionOrdersService;
import com.shq.oper.service.primarydb.ShopperService;
import com.shq.oper.util.Constant;

@Service("distributionOrdersService")
public class DistributionOrdersServiceImpl extends BaseServiceImpl<DistributionOrders, Long>
		implements DistributionOrdersService {

	@Autowired
	private DistributionOrdersMapper distributionOrdersMapper;
	
	@Autowired
	private ShopperMapper shopperMapper;
	
	@Autowired
	private ShopperService shopperService;
	

	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryOrdersTotalStatisticsMsg--'", unless = "#result eq null")
	@Override
	public OrderCountStatisticsDto queryOrdersTotalStatisticsMsg() {
		return distributionOrdersMapper.queryOrdersTotalStatisticsMsg();
	}

	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryUnpaidOrderMsg--'", unless = "#result eq null")
	@Override
	public UnpaidOrderStatisticsDto queryUnpaidOrderMsg() {
		return distributionOrdersMapper.queryUnpaidOrderMsg();
	}

	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryCommodityOrderStatisticsMsg--' +#entity ", unless = "#result eq null")
	@Override
	public Data<CommodityOrderStatisticsDto> queryCommodityOrderStatisticsMsg(CommodityOrderStatisticsDto entity) {
		Page<CommodityOrderStatisticsDto> page = distributionOrdersMapper.queryCommodityOrderStatisticsMsg(entity);
		Map<String, CommodityOrderStatisticsDto> resultMap = this.queryTestCommodityStatisticsDataMsg(entity);
		Map<String, CommodityOrderStatisticsDto> commodityOrderStatisticsDtoMap = this.queryHomeOfLoveUserStatisticsData(entity);
		if(page.getResult().size()>0&&resultMap.size()>0) {
			for (CommodityOrderStatisticsDto commodityOrderStatisticsDto : page.getResult()) {
				CommodityOrderStatisticsDto tmp = resultMap.get(commodityOrderStatisticsDto.getDate().toString());
				if(!StringUtils.isEmpty(tmp)) {
					if(!StringUtils.isEmpty(tmp.getTestOrdersTotalCount())) {
						commodityOrderStatisticsDto.setTestOrdersTotalCount(tmp.getTestOrdersTotalCount());
					}
					if(!StringUtils.isEmpty(tmp.getTestOrderTotalAmmount())) {
						commodityOrderStatisticsDto.setTestOrderTotalAmmount(tmp.getTestOrderTotalAmmount());
					}
					if(!StringUtils.isEmpty(tmp.getSeqCounts())) {
						commodityOrderStatisticsDto.setSeqCounts(tmp.getSeqCounts());
					}
				}
			}
			if(commodityOrderStatisticsDtoMap.size()>0) {
				for (CommodityOrderStatisticsDto commodityOrderStatisticsDto : page.getResult()) {
					CommodityOrderStatisticsDto tmp = commodityOrderStatisticsDtoMap.get(commodityOrderStatisticsDto.getDate().toString());
					if(!StringUtils.isEmpty(tmp)) {
						if(!StringUtils.isEmpty(tmp.getRegistCounts())) {
							commodityOrderStatisticsDto.setRegistCounts(tmp.getRegistCounts());
						}
					}
				}
			}
		}
		return Data.ok(page);
	}

	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryCommodityTradingVolumeDataMsg--' +#entity ", unless = "#result eq null")
	@Override
	public Data<CommodityStatisticsRankingDto> queryCommodityTradingVolumeDataMsg(CommodityStatisticsRankingDto entity) {
		return Data.ok(distributionOrdersMapper.queryCommodityTradingVolumeDataMsg(entity));
	}

	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryCommoditySalesDataMsg--' +#entity ", unless = "#result eq null")
	@Override
	public Data<CommodityStatisticsRankingDto> queryCommoditySalesDataMsg(CommodityStatisticsRankingDto entity) {
		return Data.ok(distributionOrdersMapper.queryCommoditySalesDataMsg(entity));
	}

	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--querySupplierTradingVolumeDataMsg--' +#entity ", unless = "#result eq null")
	@Override
	public Data<SupplierRankingStatisticsDto> querySupplierTradingVolumeDataMsg(SupplierRankingStatisticsDto entity) {
		Page<SupplierRankingStatisticsDto> page = distributionOrdersMapper.querySupplierTradingVolumeDataMsg(entity);
		List<SupplierRankingStatisticsDto> resultList = page.getResult();
		Set<String>set=new HashSet<>();
		Map<String, Shopper> shopperMap =new HashMap<>();
		List<Shopper> shopperList =new ArrayList<>();
		if(resultList.size()>0) {
			for (SupplierRankingStatisticsDto supplierRankingStatisticsDto : resultList) {
				set.add(supplierRankingStatisticsDto.getSeq());
			}
		}
	
		if(set.size()>0) {
		 shopperMap = shopperService.queryShopperBySeqSet(set);
		}
		if(shopperMap.size()>0) {
			for (SupplierRankingStatisticsDto supplierRankingStatisticsDto : page.getResult()) {
				Shopper shopper = shopperMap.get(supplierRankingStatisticsDto.getSeq());
				if(!StringUtils.isEmpty(shopper)) {
					if(!StringUtils.isEmpty(shopper.getShopname())) {
						supplierRankingStatisticsDto.setCompanyName(shopper.getShopname());
						}
					if(!StringUtils.isEmpty(shopper.getMobile())) {
						supplierRankingStatisticsDto.setContact(shopper.getMobile());
					}
				}
			}
		}
		return Data.ok(page);
	}
	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--querySupplierSalesDataMsg--' +#entity ", unless = "#result eq null")
	@Override
	public Data<SupplierRankingStatisticsDto> querySupplierSalesDataMsg(SupplierRankingStatisticsDto entity) {
		Page<SupplierRankingStatisticsDto> page = distributionOrdersMapper.querySupplierSalesDataMsg( entity);
		List<SupplierRankingStatisticsDto> resultList = page.getResult();
		Set<String>set=new HashSet<>();
		Map<String, Shopper> shopperMap =new HashMap<>();
		List<Shopper> shopperList =new ArrayList<>();
		if(resultList.size()>0) {
			for (SupplierRankingStatisticsDto supplierRankingStatisticsDto : resultList) {
				set.add(supplierRankingStatisticsDto.getSeq());
			}
		}
	
		if(set.size()>0) {
		 shopperMap = shopperService.queryShopperBySeqSet(set);
		}
		if(shopperMap.size()>0) {
			for (SupplierRankingStatisticsDto supplierRankingStatisticsDto : page.getResult()) {
				Shopper shopper = shopperMap.get(supplierRankingStatisticsDto.getSeq());
				if(!StringUtils.isEmpty(shopper)) {
					if(!StringUtils.isEmpty(shopper.getShopname())) {
						supplierRankingStatisticsDto.setCompanyName(shopper.getShopname());
						}
					if(!StringUtils.isEmpty(shopper.getMobile())) {
						supplierRankingStatisticsDto.setContact(shopper.getMobile());
					}
				}
			}
		}
		return Data.ok(page);
	}

	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryCategoryIdSalesDataMsg--' +#entity ", unless = "#result eq null")
	@Override
	public Data<CategoryIdStatisticsRankingDto> queryCategoryIdSalesDataMsg(CategoryIdStatisticsRankingDto entity) {
		return Data.ok(distributionOrdersMapper.queryCategoryIdSalesDataMsg( entity));
	}

	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryGenreIdTradeVolumeDataMsg--' +#entity ", unless = "#result eq null")
	@Override
	public Data<GenreIdStatisticsRankingDto> queryGenreIdTradeVolumeDataMsg(GenreIdStatisticsRankingDto entity) {
		return Data.ok(distributionOrdersMapper.queryGenreIdTradeVolumeDataMsg( entity));
	}

	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryGenreIdSalesDataMsg--' +#entity ", unless = "#result eq null")
	@Override
	public Data<GenreIdStatisticsRankingDto> queryGenreIdSalesDataMsg(GenreIdStatisticsRankingDto entity) {
		return Data.ok(distributionOrdersMapper.queryGenreIdSalesDataMsg( entity));
	}

	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryCategoryIdTradeVolumeDataMsg--' +#entity ", unless = "#result eq null")
	@Override
	public Data<CategoryIdStatisticsRankingDto> queryCategoryIdTradeVolumeDataMsg(
			CategoryIdStatisticsRankingDto entity) {
		return Data.ok(distributionOrdersMapper.queryCategoryIdTradeVolumeDataMsg(
				 entity));
	}

	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryCommodityStatisticsDataMsg--' +#entity ", unless = "#result eq null")
	@Override
	public Data<CommodityStatisticsRankingDto> queryCommodityStatisticsDataMsg(CommodityStatisticsRankingDto entity) {
		return Data.ok(distributionOrdersMapper.queryCommodityStatisticsDataMsg(entity));
	}
	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryActivityCommodityOrderStatisticsMsg--' +#entity ", unless = "#result eq null")
	@Override
	public Data<ActivityCommodityOrderStatisticsDto> queryActivityCommodityOrderStatisticsMsg(ActivityCommodityOrderStatisticsDto entity) {
		
		Page<ActivityCommodityOrderStatisticsDto> page = distributionOrdersMapper.queryActivityCommodityOrderStatisticsMsg(entity);
		Map<String, ActivityCommodityOrderStatisticsDto> resultMap = this.queryActivityTestCommodityStatisticsDataMsg(entity);
		if(page.getResult().size()>0&&resultMap.size()>0) {
			for (ActivityCommodityOrderStatisticsDto activityCommodityOrderStatisticsDto : page.getResult()) {
				ActivityCommodityOrderStatisticsDto tmp = resultMap.get(activityCommodityOrderStatisticsDto.getActiveColumnMark());
				if(!StringUtils.isEmpty(tmp)) {
					if(!StringUtils.isEmpty(tmp.getTestOrdersTotalCount())) {
						activityCommodityOrderStatisticsDto.setTestOrdersTotalCount(tmp.getTestOrdersTotalCount());
						}
					if(!StringUtils.isEmpty(tmp.getTestOrderTotalAmmount())) {
						activityCommodityOrderStatisticsDto.setTestOrderTotalAmmount(tmp.getTestOrderTotalAmmount());
						}
					if(!StringUtils.isEmpty(tmp.getSeqCounts())) {
						activityCommodityOrderStatisticsDto.setSeqCounts(tmp.getSeqCounts());
						}
				}
			}
		}
		return Data.ok(page);
	}
	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryTestStatisticsMsg--'", unless = "#result eq null")
	@Override
	public OrderTestCountStatisticsDto queryTestStatisticsMsg() {
		return distributionOrdersMapper.queryTestStatisticsMsg();
	}
	
	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryTestCommodityStatisticsDataMsg--' +#entity ", unless = "#result eq null")
	@Override
	public Map<String, CommodityOrderStatisticsDto> queryTestCommodityStatisticsDataMsg(CommodityOrderStatisticsDto entity) {
		List<CommodityOrderStatisticsDto> resultList =distributionOrdersMapper.queryTestCommodityStatisticsDataMsg(entity);
		Map<String, CommodityOrderStatisticsDto> map = new HashMap<>(0);
		if (resultList.size() > 0) {
			for (CommodityOrderStatisticsDto commodityOrderStatisticsDto : resultList) {
				map.put(commodityOrderStatisticsDto.getDate().toString(),commodityOrderStatisticsDto);
			}
		}
		return map;
	}
	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryActivityTestCommodityStatisticsDataMsg--' +#entity ", unless = "#result eq null")
	@Override
	public Map<String, ActivityCommodityOrderStatisticsDto> queryActivityTestCommodityStatisticsDataMsg(ActivityCommodityOrderStatisticsDto entity) {
		List<ActivityCommodityOrderStatisticsDto> resultList = distributionOrdersMapper.queryActivityTestCommodityStatisticsDataMsg(entity);
		Map<String, ActivityCommodityOrderStatisticsDto> map = new HashMap<>(0);
		if (resultList.size() > 0) {
			for (ActivityCommodityOrderStatisticsDto activityCommodityOrderStatisticsDto : resultList) {
				map.put(activityCommodityOrderStatisticsDto.getActiveColumnMark(),activityCommodityOrderStatisticsDto);
			}
		}
		return map;
	}
	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryTwoPurchasesOfUsers--' +#entity ", unless = "#result eq null")
	@Override
	public Integer queryTwoPurchasesOfUsers() {
		return distributionOrdersMapper.queryTwoPurchasesOfUsers();
	}
	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--buyCountsStatistics--' +#entity ", unless = "#result eq null")
	@Override
	public LoveOfHomeDataCountStatisticsDto buyCountsStatistics() {
		return distributionOrdersMapper.buyCountsStatistics();
	}
	
	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_STATISTICS +'--queryHomeOfLoveUserStatisticsData--' +#entity ", unless = "#result eq null")
	@Override
	public Map<String, CommodityOrderStatisticsDto> queryHomeOfLoveUserStatisticsData(CommodityOrderStatisticsDto entity) {
		List<CommodityOrderStatisticsDto> resultList =shopperMapper.queryHomeOfLoveUserStatisticsData(entity);
		Map<String, CommodityOrderStatisticsDto> map = new HashMap<>(0);
		if (resultList.size() > 0) {
			for (CommodityOrderStatisticsDto commodityOrderStatisticsDto : resultList) {
				map.put(commodityOrderStatisticsDto.getDate().toString(),commodityOrderStatisticsDto);
			}
		}
		return map;
	}
	
}