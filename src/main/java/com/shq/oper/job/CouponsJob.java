package com.shq.oper.job;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import com.github.pagehelper.PageInfo;
import com.shq.oper.enums.ProductTypeEnum;
import com.shq.oper.model.domain.mongo.channel.Goods;
import com.shq.oper.service.primarydb.DeductibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.shq.oper.model.dto.Msg;
import com.shq.oper.service.primarydb.CouponsUserService;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.DateUtils.DateFormat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
@Configuration
@EnableScheduling
public class CouponsJob {

	@Autowired
	private CouponsUserService couponsUserService;

	@Autowired
	private DeductibleService deductibleService;


	@Scheduled(cron = "0 0/30 * * * ?")
	public void runByMin() {
//		log.debug("-----------------5分钟定时任务----------"+DateUtils.now());
		log.debug("------每30分钟定时任务--优惠券过期定时任务-------CouponsStatusExpired------"+DateUtils.now());
		try {
			LocalDateTime expiredDayDt = DateUtils.addDays(LocalDateTime.now(), -1);
			String expiredDay = DateUtils.to(expiredDayDt, DateFormat.LONG_DATE_PATTERN_LINE);
			Msg<String> msg = couponsUserService.updateCouponsStatusExpiredByJob(expiredDay, null);
			log.debug("--CouponsStatusExpired------"+msg);
		} catch (Exception e) {
			log.error("--CouponsStatusExpired---error>>"+e.getMessage(),e);
		}
	}

	
	@Scheduled(cron = "1 0 0 * * ?")
	public void runByCouponsStatusExpired() {
		log.debug("------每天凌晨0点0分1秒--优惠券过期定时任务-------CouponsStatusExpired------"+DateUtils.now());
		try {
			LocalDateTime expiredDayDt = DateUtils.addDays(LocalDateTime.now(), -1);
			String expiredDay = DateUtils.to(expiredDayDt, DateFormat.SHORT_DATE_PATTERN_LINE);
			Msg<String> msg = couponsUserService.updateCouponsStatusExpiredByJob(expiredDay, null);
			log.debug("--CouponsStatusExpired------"+msg);
		} catch (Exception e) {
			log.error("--CouponsStatusExpired---error>>"+e.getMessage(),e);
		}
	}


	@Scheduled(cron = "1 0 0 * * ?")
	public void runByDeductibleStatusExpired() {
		log.debug("------每天凌晨0点0分1秒--抵扣券过期定时任务-------CouponsStatusExpired------"+DateUtils.now());
		try {
			LocalDateTime expiredDayDt = LocalDateTime.now();
			String expiredDay = DateUtils.to(expiredDayDt, DateFormat.LONG_DATE_PATTERN_LINE);
			Msg<String> msg = deductibleService.updateDetuctibleStatusExpiredByJob(expiredDay);
			log.debug("--DeductibleStatusExpired------"+msg);
		} catch (Exception e) {
			log.error("--DeductibleStatusExpired---error>>"+e.getMessage(),e);
		}
	}


	@Scheduled(cron = "1 0 0 * * ?")
//@Scheduled(cron = "0 0/30 * * * ?")
	public void saveDeduticbleDayStatics() {
		log.debug("------每天凌晨0点0分1秒--抵扣券代金券日统计-----------"+DateUtils.now());
		try {
			LocalDateTime expiredDayDt = LocalDateTime.now();
			expiredDayDt=expiredDayDt.plusDays(-1);
//			String expiredDay = DateUtils.to(expiredDayDt, DateFormat.SHORT_DATE_PATTERN_LINE);
			Msg<String> msg = deductibleService.saveDeducitbleStatisticsDay(expiredDayDt);
			log.debug("--DeductibleStatusExpired--抵扣券代金券日统计结束----"+msg);
		} catch (Exception e) {
			log.error("--DeductibleStatusExpired---error>>"+e.getMessage(),e);
		}
	}
}
