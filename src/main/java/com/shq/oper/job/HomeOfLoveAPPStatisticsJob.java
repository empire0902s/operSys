package com.shq.oper.job;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.shq.oper.dao.MongoDao;
import com.shq.oper.enums.api.HomePageReceiveCodeEnums;
import com.shq.oper.model.domain.mongo.ApiRequestDataLog;
import com.shq.oper.model.domain.mongo.AppDataStatistics;
import com.shq.oper.model.domain.mongo.AppLocationData;
import com.shq.oper.service.primarydb.ApiRequestDataLogService;
import com.shq.oper.service.primarydb.AppLocationDataService;
import com.shq.oper.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class HomeOfLoveAPPStatisticsJob {

	@Autowired
	private AppLocationDataService appLocationDataService;
	
	//每天凌晨2点开始统计前一天数据
	@Scheduled(cron = "0 0 2 * * ?")
	//@Scheduled(cron = "*/5 * * * * ?")
	public void runByMin() {
		try {
			log.debug("-----------------App安装与启动次数统计开始----------"+DateUtils.now());

			LocalDateTime dtn=LocalDateTime.now();
			LocalDateTime yesterdayStart = DateUtils.addDaysFormatStart(dtn, -1);
			LocalDateTime yesterdayEnd = DateUtils.addDaysFormatEnd(dtn, -1);
			AppLocationData entity=new AppLocationData();

			appLocationDataService.saveAppDataStatistics(entity,yesterdayStart,yesterdayEnd);

			log.debug("-----------------App安装与启动次数统计结束----------"+DateUtils.now());
		} catch (Exception e) {
			log.error("[mongodb-appstatistics]", e);
		}
	}
}
