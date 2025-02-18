package com.shq.oper.controller.coupons;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.mongo.DeducitbleStatisticsDayMongo;
import com.shq.oper.enums.CouponsTypeEnum;
import com.shq.oper.mapper.primarydb.CouponsMapper;
import com.shq.oper.mapper.primarydb.CouponsUserMapper;
import com.shq.oper.model.domain.mongo.DeducitbleStatisticsDay;
import com.shq.oper.model.domain.primarydb.Activity;
import com.shq.oper.model.dto.CouponsStatisticsDto;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.ReqCouponsStatisticsDtoDto;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.service.primarydb.ActivityService;
import com.shq.oper.service.primarydb.DeductibleService;
import com.shq.oper.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

/**
 * 
 * @author ljz
 *
 */
@RestController
@Slf4j
@RequestMapping("/coupons/statistics")
public class CouponStatisticsController extends BaseController {

	@Autowired
	SystemProperties systemProperties;

	@Autowired
	CouponsMapper couponsMapper;
	
	@Autowired
	private ActivityService activityService;

	@Autowired
	private DeducitbleStatisticsDayMongo deducitbleStatisticsDayMongo;

	@Autowired
	private DeductibleService deductibleService;

	@Autowired
	CouponsUserMapper couponsUserMapper;
	private Map<String, Object> result;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		
		Map<String, Object> couponsTypeList = CouponsTypeEnum.getMap();

		// 昨天00：00：00-23：59：59
		LocalDateTime start = DateUtils.addDaysFormatStart(LocalDateTime.now(), -1);
		LocalDateTime end = DateUtils.addDaysFormatEnd(LocalDateTime.now(), -1);

		// 统计昨天的优惠券使用数量
		CouponsStatisticsDto couponsUseNumsByYesterday;
		couponsUseNumsByYesterday = couponsUserMapper.queryCouponsUseStatistics(start, end);
		if (StringUtils.isEmpty(couponsUseNumsByYesterday)) {
			couponsUseNumsByYesterday = new CouponsStatisticsDto();
			couponsUseNumsByYesterday.setTotalSendNums(0);
			couponsUseNumsByYesterday.setTotalOrderMoney(0);
			couponsUseNumsByYesterday.setTotalSpendMoney(0);
		}
		// 统计优惠券总使用数量
		CouponsStatisticsDto totalCouponsUseNums;
		totalCouponsUseNums = couponsUserMapper.queryCouponsUseStatistics(null, null);
		if (StringUtils.isEmpty(totalCouponsUseNums)) {
			totalCouponsUseNums = new CouponsStatisticsDto();
			totalCouponsUseNums.setTotalSendNums(0);
			totalCouponsUseNums.setTotalOrderMoney(0);
			totalCouponsUseNums.setTotalSpendMoney(0);
		}

		request.setAttribute("couponsTypeList", couponsTypeList);
		request.setAttribute("couponsUseNumsByYesterday", couponsUseNumsByYesterday);
		request.setAttribute("totalCouponsUseNums", totalCouponsUseNums);
		return toPage(request);
	}

	@RequestMapping("/deductibleindex")
	public ModelAndView deductibleindex(HttpServletRequest request) {
		return toPage(request);
	}


	@RequestMapping("/chooseActivity")
	public ModelAndView chooseActivity(HttpServletRequest request,String timeRange) {
		request.setAttribute("timeRange", timeRange);
		return toPage(request);
	}
	
	@RequestMapping(value = "/chooseActivityList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<Activity> list(Activity entity, PageDto page,String timeRange) {
		if (!StringUtils.isEmpty(timeRange)) {
			String[] strings = timeRange.split(" - ");
			entity.setSendTimeStart(DateUtils.parse(strings[0] + " 00:00:00"));
			entity.setSendTimeEnd(DateUtils.parse(strings[1] + " 23:59:59"));
		}
		PageHelper.startPage(page.getPage(), page.getLimit());
		Page<Activity> entitys =activityService.queryActivity(entity);
		//PageInfo<Activity> entitys = activityService.selectPageAndCount(entity, page.getPage(), page.getLimit());
		return Data.ok(entitys);
	}
	
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<CouponsStatisticsDto> list(ReqCouponsStatisticsDtoDto entity, PageDto page, String timeRange) {
		if (!StringUtils.isEmpty(timeRange)) {
			String[] strings = timeRange.split(" - ");
			//拼接start:xx-xx-xx :00:00:00 到 end：xx-xx-xx 23：59：59
			entity.setStart(DateUtils.parse(strings[0]+" 00:00:00"));
			entity.setEnd(DateUtils.parse(strings[1]+" 23:59:59"));
		}else {
			//如果不输入查询日期，则默认查询一个月内的记录
			entity.setStart(DateUtils.addDaysFormatStart(LocalDateTime.now(), -30).withNano(0));
			entity.setEnd(DateUtils.addDaysFormatEnd(LocalDateTime.now(), 0).withNano(0));
		}
		com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
		Page<CouponsStatisticsDto> entitys = couponsUserMapper.queryAllCouponsUseStatistics(entity);
		return Data.ok(entitys);
	}

	/**
	 * 抵扣券统计
	 *@author ltg
	 *@date 2018/12/4 18:41
	 * @params:[page, timeRange]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.mongo.DeducitbleStatisticsDay>
	 */
	@RequestMapping(value = "/deductiblelist", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<DeducitbleStatisticsDay> deductiblelist( PageDto page, String timeRange) {

		String start="",end="";

		try{
			LocalDateTime now=LocalDateTime.now();
			deductibleService.saveDeducitbleStatisticsDay(now);

			if (!StringUtils.isEmpty(timeRange)) {
				String[] strings = timeRange.split(" - ");
				start=DateUtils.parse(strings[0]+" 00:00:00").toString();
				end=DateUtils.parse(strings[1]+" 23:59:59").toString();
			}else {
				//如果不输入查询日期，则默认查询一个月内的记录
				LocalDateTime startTime=DateUtils.addDaysFormatStart(LocalDateTime.now(), -30).withNano(0);
				LocalDateTime endTime=DateUtils.addDaysFormatEnd(LocalDateTime.now(), 0).withNano(0);
				start=startTime.toString();
				end=endTime.toString();
			}

			Query query = new Query();
			query.addCriteria(Criteria.where("type").is(2));
			query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
			query.with(new Sort(Sort.Direction.DESC, "createTime"));

			PageInfo<DeducitbleStatisticsDay> pageInfo= deducitbleStatisticsDayMongo.findByQuery(query,page,DeducitbleStatisticsDay.class);

			return Data.ok(pageInfo);
		}catch (Exception e){
			e.printStackTrace();
			return Data.ok(new PageInfo<>());
		}

	}

	/**
	 * 代金券统计
	 *@author ltg
	 *@date 2018/12/4 18:41
	 * @params:[page, timeRange]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.mongo.DeducitbleStatisticsDay>
	 */
	@RequestMapping(value = "/cashlist", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<DeducitbleStatisticsDay> cashlist( PageDto page, String timeRange) {

		String start="",end="";

		try{
			LocalDateTime now=LocalDateTime.now();
			deductibleService.saveDeducitbleStatisticsDay(now);

			if (!StringUtils.isEmpty(timeRange)) {
				String[] strings = timeRange.split(" - ");
				start=DateUtils.parse(strings[0]+" 00:00:00").toString();
				end=DateUtils.parse(strings[1]+" 23:59:59").toString();
			}else {
				//如果不输入查询日期，则默认查询一个月内的记录
				LocalDateTime startTime=DateUtils.addDaysFormatStart(LocalDateTime.now(), -30).withNano(0);
				LocalDateTime endTime=DateUtils.addDaysFormatEnd(LocalDateTime.now(), 0).withNano(0);
				start=startTime.toString();
				end=endTime.toString();
			}

			Query query = new Query();
			query.addCriteria(Criteria.where("type").is(1));
			query.addCriteria(Criteria.where("createTime").gte(start).lte(end));
			query.with(new Sort(Sort.Direction.DESC, "createTime"));

			PageInfo<DeducitbleStatisticsDay> pageInfo= deducitbleStatisticsDayMongo.findByQuery(query,page,DeducitbleStatisticsDay.class);

			return Data.ok(pageInfo);

		}catch (Exception e){
			e.printStackTrace();
			return Data.ok(new PageInfo<>());
		}

	}


}