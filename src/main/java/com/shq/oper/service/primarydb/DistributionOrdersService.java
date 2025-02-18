package com.shq.oper.service.primarydb;

import java.util.List;
import java.util.Map;

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

public interface DistributionOrdersService extends BaseService<DistributionOrders, Long> {

	OrderCountStatisticsDto queryOrdersTotalStatisticsMsg();

	OrderTestCountStatisticsDto queryTestStatisticsMsg();

	UnpaidOrderStatisticsDto queryUnpaidOrderMsg();

	Data<CommodityOrderStatisticsDto> queryCommodityOrderStatisticsMsg(CommodityOrderStatisticsDto entity);

	Data<CommodityStatisticsRankingDto> queryCommodityTradingVolumeDataMsg(CommodityStatisticsRankingDto entity);

	Data<CommodityStatisticsRankingDto> queryCommoditySalesDataMsg(CommodityStatisticsRankingDto entity);

	Data<SupplierRankingStatisticsDto> querySupplierTradingVolumeDataMsg(SupplierRankingStatisticsDto entity);

	Data<SupplierRankingStatisticsDto> querySupplierSalesDataMsg(SupplierRankingStatisticsDto entity);

	Data<CategoryIdStatisticsRankingDto> queryCategoryIdSalesDataMsg(CategoryIdStatisticsRankingDto entity);

	Data<GenreIdStatisticsRankingDto> queryGenreIdTradeVolumeDataMsg(GenreIdStatisticsRankingDto entity);

	Data<GenreIdStatisticsRankingDto> queryGenreIdSalesDataMsg(GenreIdStatisticsRankingDto entity);

	Data<CategoryIdStatisticsRankingDto> queryCategoryIdTradeVolumeDataMsg(CategoryIdStatisticsRankingDto entity);

	Data<CommodityStatisticsRankingDto> queryCommodityStatisticsDataMsg(CommodityStatisticsRankingDto entity);

	Data<ActivityCommodityOrderStatisticsDto> queryActivityCommodityOrderStatisticsMsg(
			ActivityCommodityOrderStatisticsDto entity);

	Map<String, CommodityOrderStatisticsDto> queryTestCommodityStatisticsDataMsg(CommodityOrderStatisticsDto entity);

	Map<String, ActivityCommodityOrderStatisticsDto> queryActivityTestCommodityStatisticsDataMsg(
			ActivityCommodityOrderStatisticsDto entity);

	Integer queryTwoPurchasesOfUsers();
	LoveOfHomeDataCountStatisticsDto buyCountsStatistics();

	Map<String, CommodityOrderStatisticsDto> queryHomeOfLoveUserStatisticsData(CommodityOrderStatisticsDto entity);
}