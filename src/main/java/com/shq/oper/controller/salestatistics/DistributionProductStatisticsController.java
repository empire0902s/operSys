package com.shq.oper.controller.salestatistics;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.MongoDao;
import com.shq.oper.mapper.primarydb.CouponsUserMapper;
import com.shq.oper.model.domain.mongo.AppDataStatistics;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.salestatistics.*;
import com.shq.oper.service.primarydb.AppDataStatisticsService;
import com.shq.oper.service.primarydb.DistributionOrdersService;
import com.shq.oper.service.primarydb.DistributionProductService;
import com.shq.oper.service.primarydb.ShopperService;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/distribute/statistics")
public class DistributionProductStatisticsController extends BaseController {

	@Autowired
	private DistributionOrdersService distributionOrdersService;

	@Autowired
	private DistributionProductService distributionProductService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private ShopperService shopperService;

	@Autowired
	private CouponsUserMapper couponsUserMapper;

	@Autowired
	private MongoDao<AppDataStatistics> appDataStatisticsMongoDao;
	
	@Autowired
	private AppDataStatisticsService appDataStatisticsService;
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) throws Exception {
		
		LocalDateTime dtn=LocalDateTime.now();
		Map<String,Long> countmap=appDataStatisticsService.getCountMap(dtn);

		ProductShelfStatusCountStatisticsDto statisticsResult = distributionProductService
				.queryFinalProductShelfStatusCountStatistics();

		OrderCountStatisticsDto ordersTotalStatisticsMsg = distributionOrdersService.queryOrdersTotalStatisticsMsg();

		UnpaidOrderStatisticsDto unpaidOrderMsg = distributionOrdersService.queryUnpaidOrderMsg();
		//爱之家注册总人数统计
		LoveOfHomeDataCountStatisticsDto countLoveOfHomeUsersStatistics = shopperService.countLoveOfHomeUsersStatistics();
		//爱之家购买人数，二次购买人数统计
		LoveOfHomeDataCountStatisticsDto buyCountsStatistics = distributionOrdersService.buyCountsStatistics();
		OrderTestCountStatisticsDto testOrderStatisticsMsg = distributionOrdersService.queryTestStatisticsMsg();
		
		//爱之家app安装与启动次数统计
		long installationCounts = getStatistics("installation_counts");
		long startupCounts = getStatistics("startup_counts");
		installationCounts+=countmap.get("installationCounts");
		startupCounts+=countmap.get("startupCounts");
		
		request.setAttribute("statisticsResult", statisticsResult);
		request.setAttribute("ordersTotalStatisticsMsg", ordersTotalStatisticsMsg);
		request.setAttribute("countLoveOfHomeUsersStatistics", countLoveOfHomeUsersStatistics);
		request.setAttribute("buyCountsStatistics", buyCountsStatistics);
		request.setAttribute("unpaidOrderMsg", unpaidOrderMsg);
		request.setAttribute("testOrderStatisticsMsg", testOrderStatisticsMsg);
		request.setAttribute("installationCounts", installationCounts);
		request.setAttribute("startupCounts", startupCounts);
		return toPage(request);
	}

	/**
	 * 分销订单统计
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/ordersStatistics")
	public ModelAndView ordersStatistics(HttpServletRequest request) {
		Integer twoPurchasesOfUsers = distributionOrdersService.queryTwoPurchasesOfUsers();
		request.setAttribute("twoPurchasesOfUsers", twoPurchasesOfUsers);
		return toPage(request);
	}

	/**
	 * 赠送优惠券统计
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/giveCouponsStatistic")
	public ModelAndView giveCouponsStatistic(HttpServletRequest request) {

		GiveCouponsStatisticsDto giveCouponsStatisticsDto=couponsUserMapper.queryGiveCouponsStatic();
		if (StringUtils.isEmpty(giveCouponsStatisticsDto.getOutTotal())){
			giveCouponsStatisticsDto.setOutTotal(0.0);
		}
		request.setAttribute("giveCouponsStatisticsDto", giveCouponsStatisticsDto);
		return toPage(request);
	}

	/**
	 * 分销活动订单统计
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/activityOrdersStatistics")
	public ModelAndView activityOrdersStatistics(HttpServletRequest request) {
		return toPage(request);
	}

	/**
	 * app统计
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/appStatistic")
	public ModelAndView appStatistics(HttpServletRequest request) {

		Aggregation aggregation =Aggregation.newAggregation(

				Aggregation.group().sum("installation_counts").as("installationCounts")
				.sum("startup_counts").as("startupCounts")
				.sum("instaCounts520").as("instaCounts520")
				.sum("instaCounts360").as("instaCounts360")
				.sum("instaCountBaidu").as("instaCountsTencent")
				.sum("instaCountSugou").as("instaCountSugou")
				.sum("instaCountHuawei").as("instaCountHuawei")
				.sum("instaCountXiaomi").as("instaCountXiaomi")
				.sum("instaCountVivo").as("instaCountVivo")
				.sum("instaCountWandoujia").as("instaCountWandoujia")
				.sum("instaCountOPPO").as("instaCountOPPO")
				.sum("instaCountJinli").as("instaCountJinli")
				.sum("instaCountIOS").as("instaCountIOS")
		) ;

		AggregationResults<BasicDBObject>  results= appDataStatisticsMongoDao.aggregate(aggregation,"t_app_data_statistics");

		String str=JsonUtils.toDefaultJson(results.getMappedResults().get(0));
		AppDataStatistics appDataStatistics=JsonUtils.parse(str,AppDataStatistics.class);
		if (null==appDataStatistics){
			appDataStatistics=new AppDataStatistics();
		}
//
//		GiveCouponsStatisticsDto giveCouponsStatisticsDto=couponsUserMapper.queryGiveCouponsStatic();
//		if (StringUtils.isEmpty(giveCouponsStatisticsDto.getOutTotal())){
//			giveCouponsStatisticsDto.setOutTotal(0.0);
//		}
		request.setAttribute("appDataStatistics", appDataStatistics);
		return toPage(request);
	}

	/**
	 * 活动订单统计
	 * 
	 * @param entity
	 * @param page
	 * @param timeRange
	 * @return
	 */
	@RequestMapping(value = "/activityOrdersStatisticsList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<ActivityCommodityOrderStatisticsDto> activityOrdersStatisticsList(
			ActivityCommodityOrderStatisticsDto entity, PageDto page, String timeRange) {

		if (!StringUtils.isEmpty(timeRange)) {
			String[] strings = timeRange.split(" - ");
			entity.setStartTime(DateUtils.parse(strings[0] + " 00:00:00"));
			entity.setEndTime(DateUtils.parse(strings[1] + " 23:59:59"));
		}
		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		return distributionOrdersService.queryActivityCommodityOrderStatisticsMsg(entity);
	}

	/**
	 * 订单统计
	 * 
	 * @param entity
	 * @param page
	 * @param timeRange
	 * @return
	 */
	@RequestMapping(value = "/ordersStatisticsList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<CommodityOrderStatisticsDto> ordersStatisticsList(CommodityOrderStatisticsDto entity, PageDto page,
			String timeRange) {

		if (!StringUtils.isEmpty(timeRange)) {
			String[] strings = timeRange.split(" - ");
			entity.setStartTime(DateUtils.parse(strings[0] + " 00:00:00"));
			entity.setEndTime(DateUtils.parse(strings[1] + " 23:59:59"));
		}
		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		Data<CommodityOrderStatisticsDto> e = distributionOrdersService.queryCommodityOrderStatisticsMsg(entity);
		return distributionOrdersService.queryCommodityOrderStatisticsMsg(entity);
	}

	/**
	 * 商品交易金额统计排名
	 * 
	 * @param entity
	 * @param page
	 * @param timeRange
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<CommodityStatisticsRankingDto> list(CommodityStatisticsRankingDto entity, PageDto page,
			String timeRange) {

		if (!StringUtils.isEmpty(timeRange)) {
			String[] strings = timeRange.split(" - ");
			// 拼接start:xx-xx-xx :00:00:00 到 end：xx-xx-xx 23：59：59
			entity.setStartTime(DateUtils.parse(strings[0] + " 00:00:00"));
			entity.setEndTime(DateUtils.parse(strings[1] + " 23:59:59"));
		}
		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		return distributionOrdersService.queryCommodityTradingVolumeDataMsg(entity);
	}

	/**
	 * 商品销售数量统计排名
	 * 
	 * @param entity
	 * @param page
	 * @param timeRange2
	 * @return
	 */
	@RequestMapping(value = "/commoditySalesDataList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<CommodityStatisticsRankingDto> commoditySalesDataList(CommodityStatisticsRankingDto entity,
			PageDto page, String timeRange2) {

		if (!StringUtils.isEmpty(timeRange2)) {
			String[] strings = timeRange2.split(" - ");
			entity.setStartTime(DateUtils.parse(strings[0] + " 00:00:00"));
			entity.setEndTime(DateUtils.parse(strings[1] + " 23:59:59"));
		}
		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		return distributionOrdersService.queryCommoditySalesDataMsg(entity);
	}

	/**
	 * 供销商交易金额统计排名
	 * 
	 * @param entity
	 * @param page
	 * @param timeRange3
	 * @return
	 */
	@RequestMapping(value = "/supplierTradingVolumeDataList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<SupplierRankingStatisticsDto> supplierTradingVolumeDataList(SupplierRankingStatisticsDto entity,
			PageDto page, String timeRange3) {

		if (!StringUtils.isEmpty(timeRange3)) {
			String[] strings = timeRange3.split(" - ");
			entity.setStartTime(DateUtils.parse(strings[0] + " 00:00:00"));
			entity.setEndTime(DateUtils.parse(strings[1] + " 23:59:59"));
		}
		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		return distributionOrdersService.querySupplierTradingVolumeDataMsg(entity);
	}

	/**
	 * 供销商销售数量统计排名
	 * 
	 * @param entity
	 * @param page
	 * @param timeRange4
	 * @return
	 */
	@RequestMapping(value = "/supplierSalesDataList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<SupplierRankingStatisticsDto> supplierSalesDataList(SupplierRankingStatisticsDto entity, PageDto page,
			String timeRange4) {

		if (!StringUtils.isEmpty(timeRange4)) {
			String[] strings = timeRange4.split(" - ");
			entity.setStartTime(DateUtils.parse(strings[0] + " 00:00:00"));
			entity.setEndTime(DateUtils.parse(strings[1] + " 23:59:59"));
		}
		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		return distributionOrdersService.querySupplierSalesDataMsg(entity);
	}

	/**
	 * 一级类目商品交易额统计排名
	 * 
	 * @param entity
	 * @param page
	 * @param timeRange5
	 * @return
	 */
	@RequestMapping(value = "/categoryIdTradeVolumeDataList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<CategoryIdStatisticsRankingDto> categoryIdTradeVolumeDataList(CategoryIdStatisticsRankingDto entity,
			PageDto page, String timeRange5) {

		if (!StringUtils.isEmpty(timeRange5)) {
			String[] strings = timeRange5.split(" - ");
			entity.setStartTime(DateUtils.parse(strings[0] + " 00:00:00"));
			entity.setEndTime(DateUtils.parse(strings[1] + " 23:59:59"));
		}
		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		return distributionOrdersService
				.queryCategoryIdTradeVolumeDataMsg(entity);
		
	}

	/**
	 * 一级类目商品销量统计排名
	 * 
	 * @param entity
	 * @param page
	 * @param timeRange6
	 * @return
	 */
	@RequestMapping(value = "/categoryIdSalesDataList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<CategoryIdStatisticsRankingDto> categoryIdSalesDataList(CategoryIdStatisticsRankingDto entity,
			PageDto page, String timeRange6) {

		if (!StringUtils.isEmpty(timeRange6)) {
			String[] strings = timeRange6.split(" - ");
			entity.setStartTime(DateUtils.parse(strings[0] + " 00:00:00"));
			entity.setEndTime(DateUtils.parse(strings[1] + " 23:59:59"));
		}
		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		return distributionOrdersService.queryCategoryIdSalesDataMsg(entity);
	}

	/**
	 * 二级类目商品交易额统计排名
	 * 
	 * @param entity
	 * @param page
	 * @param timeRange7
	 * @return
	 */
	@RequestMapping(value = "/genreIdTradeVolumeDataList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<GenreIdStatisticsRankingDto> genreIdTradeVolumeDataList(GenreIdStatisticsRankingDto entity,
			PageDto page, String timeRange7) {

		if (!StringUtils.isEmpty(timeRange7)) {
			String[] strings = timeRange7.split(" - ");
			entity.setStartTime(DateUtils.parse(strings[0] + " 00:00:00"));
			entity.setEndTime(DateUtils.parse(strings[1] + " 23:59:59"));
		}
		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		return distributionOrdersService.queryGenreIdTradeVolumeDataMsg(entity);
	}

	/**
	 * 二级类目商品销量统计排名
	 * 
	 * @param entity
	 * @param page
	 * @param timeRange8
	 * @return
	 */
	@RequestMapping(value = "/genreIdSalesDataList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<GenreIdStatisticsRankingDto> genreIdSalesDataList(GenreIdStatisticsRankingDto entity, PageDto page,
			String timeRange8) {

		if (!StringUtils.isEmpty(timeRange8)) {
			String[] strings = timeRange8.split(" - ");
			entity.setStartTime(DateUtils.parse(strings[0] + " 00:00:00"));
			entity.setEndTime(DateUtils.parse(strings[1] + " 23:59:59"));
		}
		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		return distributionOrdersService.queryGenreIdSalesDataMsg(entity);
	}

	/**
	 * 商品数据统计(包含商品点击率，转化率的统计)
	 * 
	 * @param entity
	 * @param page
	 * @param timeRange9
	 * @return
	 */
	@RequestMapping(value = "/goodsStatisticsMsgDataList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<CommodityStatisticsRankingDto> goodsStatisticsMsgDataList(CommodityStatisticsRankingDto entity,
			PageDto page, String timeRange9) {

		if (!StringUtils.isEmpty(timeRange9)) {
			String[] strings = timeRange9.split(" - ");
			// 拼接start:xx-xx-xx :00:00:00 到 end：xx-xx-xx 23：59：59
			entity.setStartTime(DateUtils.parse(strings[0] + " 00:00:00"));
			entity.setEndTime(DateUtils.parse(strings[1] + " 23:59:59"));
		}
		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		return distributionOrdersService.queryCommodityStatisticsDataMsg(entity);
	}

	/**
	 * 获取赠送优惠券日统计
	 * @param page
	 * @param timeRange
	 * @return
	 */
	@RequestMapping(value = "/queryGiveDayCouponslist", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<GiveCouponsDayStatisticsDto> list(PageDto page, String timeRange) {

		if (StringUtils.isEmpty(timeRange)) {
			return Data.ok(new Page<>());
		}

		String[] strings = timeRange.split(" - ");
		// 拼接start:xx-xx-xx :00:00:00 到 end：xx-xx-xx 23：59：59
		LocalDateTime beginTime=DateUtils.parse(strings[0] + " 00:00:00");
		LocalDateTime endTime=DateUtils.parse(strings[1] + " 23:59:59");

		com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
		Page<GiveCouponsDayStatisticsDto> giveCouponsDtoPage=couponsUserMapper.queryGiveDayCouponsPage(beginTime,endTime);
		return Data.ok(giveCouponsDtoPage);
	}

	public long getStatistics(String condition) {  
        Long total = 0L;
        String reduce = "function(doc, aggr){" +  
                "aggr.total += doc."+condition+";" +  
                "        }";  
        Query query =new Query();
        DBObject result = mongoTemplate.getCollection("t_app_data_statistics").group(null,   
                query.getQueryObject(),   
                new BasicDBObject("total", total),  
                reduce);  
          
        Map<String,BasicDBObject> map = result.toMap();  
        if(map.size() > 0){  
            BasicDBObject bdbo = map.get("0");  
            if(bdbo != null && bdbo.get("total") != null)  
                total = bdbo.getLong("total");  
        }  
        return total;  
    }

	/**
	 * 获取app日统计
	 * @param page
	 * @param timeRange
	 * @return
	 */
	@RequestMapping(value = "/queryAppStaticsList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<AppDataStatistics> queryAppStaticsList(PageDto page, String timeRange) {

		if (StringUtils.isEmpty(timeRange)) {
			return Data.ok(new Page<>());
		}

		String[] strings = timeRange.split(" - ");
		// 拼接start:xx-xx-xx :00:00:00 到 end：xx-xx-xx 23：59：59
		LocalDateTime beginTime=DateUtils.parse(strings[0] + " 00:00:00");
		LocalDateTime endTime=DateUtils.parse(strings[1] + " 23:59:59");

        Query query=new Query();
        query.addCriteria(Criteria.where("check_create_time").gte(beginTime).lte(endTime));
		AppDataStatistics entity=new AppDataStatistics();
		PageInfo<AppDataStatistics> pageInfo=appDataStatisticsMongoDao.findByQueryPage(query,page,entity);

//		com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
//		Page<GiveCouponsDayStatisticsDto> giveCouponsDtoPage=couponsUserMapper.queryGiveDayCouponsPage(beginTime,endTime);
		return Data.ok(pageInfo);
	}
}
