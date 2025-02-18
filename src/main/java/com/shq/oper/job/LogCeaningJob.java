package com.shq.oper.job;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.shq.oper.dao.MongoDao;
import com.shq.oper.dao.mongo.ApiRequestDataLogMongo;
import com.shq.oper.model.domain.mongo.AdminActionLog;
import com.shq.oper.model.domain.mongo.ApiRequestDataLog;
import com.shq.oper.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class LogCeaningJob {

	@Autowired
	private MongoDao<AdminActionLog> mongoDao;
	
	@Autowired
	private MongoDao<ApiRequestDataLog> apiRequestDataLogMongo;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Scheduled(cron ="0 0 0 1 * ?")
	public void runByMin() {
		try {
			log.debug("-----------------日志记录定时清理开始----------"+DateUtils.now());
			LocalDateTime addDaysFormatStart = getDate(3);
			Query query = Query.query(Criteria.where("create_time").lt(addDaysFormatStart));
			mongoDao.remove(query, AdminActionLog.class);
			apiRequestDataLogMongo.remove(query, ApiRequestDataLog.class);
			log.debug("-----------------日志记录定时清理结束----------"+DateUtils.now());
		} catch (Exception e) {
			log.error("[mongodb-remove]", e);
		}
	}
	
	public LocalDateTime getDate(int number) {
		Calendar calendar =Calendar.getInstance();
		calendar.add(Calendar.MONTH, -number);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		SimpleDateFormat sdt=new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
		String format = sdt.format(calendar.getTime());
		return DateUtils.addDaysFormatStart(DateUtils.parse(format), 0);
	}
}
