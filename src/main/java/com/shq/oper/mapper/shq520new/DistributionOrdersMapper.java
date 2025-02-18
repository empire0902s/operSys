package com.shq.oper.mapper.shq520new;

import com.github.pagehelper.Page;
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
import com.shq.oper.util.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistributionOrdersMapper extends BaseMapper<DistributionOrders> {
	
	Page<CommodityStatisticsRankingDto>queryCommodityTradingVolumeDataMsg( CommodityStatisticsRankingDto param);
	Page<CommodityStatisticsRankingDto>queryCommoditySalesDataMsg( CommodityStatisticsRankingDto param);
	
	Page<SupplierRankingStatisticsDto>querySupplierTradingVolumeDataMsg( SupplierRankingStatisticsDto param);
	Page<SupplierRankingStatisticsDto>querySupplierSalesDataMsg( SupplierRankingStatisticsDto param);
	
	Page<CategoryIdStatisticsRankingDto>queryCategoryIdTradeVolumeDataMsg( CategoryIdStatisticsRankingDto param);
	Page<CategoryIdStatisticsRankingDto>queryCategoryIdSalesDataMsg( CategoryIdStatisticsRankingDto param);
	
	Page<GenreIdStatisticsRankingDto>queryGenreIdTradeVolumeDataMsg( GenreIdStatisticsRankingDto param);
	Page<GenreIdStatisticsRankingDto>queryGenreIdSalesDataMsg( GenreIdStatisticsRankingDto param);
	
	Page<CommodityOrderStatisticsDto>queryCommodityOrderStatisticsMsg( CommodityOrderStatisticsDto param);
	
	Page<CommodityStatisticsRankingDto>queryCommodityStatisticsDataMsg( CommodityStatisticsRankingDto param);
	
	OrderCountStatisticsDto queryOrdersTotalStatisticsMsg();
	UnpaidOrderStatisticsDto queryUnpaidOrderMsg();
	
	Page<ActivityCommodityOrderStatisticsDto> queryActivityCommodityOrderStatisticsMsg( ActivityCommodityOrderStatisticsDto param);
	
	OrderTestCountStatisticsDto queryTestStatisticsMsg();
	
	List<CommodityOrderStatisticsDto>queryTestCommodityStatisticsDataMsg(CommodityOrderStatisticsDto entity);
	
	List<ActivityCommodityOrderStatisticsDto>queryActivityTestCommodityStatisticsDataMsg(ActivityCommodityOrderStatisticsDto param);
	
	Integer queryTwoPurchasesOfUsers();
	
	LoveOfHomeDataCountStatisticsDto buyCountsStatistics();
	
}