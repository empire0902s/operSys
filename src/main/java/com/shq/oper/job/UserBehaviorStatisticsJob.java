package com.shq.oper.job;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.shq.oper.dao.GuestBrowsingLogTemplate;
import com.shq.oper.service.primarydb.UserBehaviorStatisticsService;
import com.shq.oper.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class UserBehaviorStatisticsJob {

	@Autowired
	private GuestBrowsingLogTemplate guestBrowsingLogTemplate;
	@Autowired
	private UserBehaviorStatisticsService userBehaviorStatisticsService;

	@Scheduled(cron = "0 0 1 * * ?")
	public void runByBehaviorStatistics() {
		log.debug("每天1点---------------统计前一天用户行为记录------------" + DateUtils.now());
		LocalDateTime dtn=LocalDateTime.now();
		LocalDateTime yesterday = DateUtils.addDaysFormatStart(dtn, -1);
		userBehaviorStatisticsService.insertByDate(yesterday);
	}

}
