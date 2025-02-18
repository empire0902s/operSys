package com.shq.oper.dao;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Component("guestBrowsingLogTemplate")
public class GuestBrowsingLogTemplate {

	@Autowired
	private MongoTemplate mongoTemplate;

	public Integer getDistinctTotalIpByTime(LocalDateTime startTime, LocalDateTime endTime) {
		Date startDate = localDateTimeFormatToDate(startTime);
		Date endDate = localDateTimeFormatToDate(endTime);
		DBObject query = new BasicDBObject();
		query.put("createTime", BasicDBObjectBuilder.start("$gte", startDate).add("$lt", endDate).get());
		List distinct = mongoTemplate.getCollection("t_guest_browsing_log").distinct("remort_ip", query);
		return distinct.size();
	}

	@Deprecated
	public Integer getDistinctTotalIp() {
		List distinct = mongoTemplate.getCollection("t_guest_browsing_log").distinct("remort_ip");
		return distinct.size();
	}

	public Integer getPVByTime(LocalDateTime startTime, LocalDateTime endTime) {

		Date startDate = localDateTimeFormatToDate(startTime);
		Date endDate = localDateTimeFormatToDate(endTime);
		DBObject query = new BasicDBObject();
		query.put("operateMethod", BasicDBObjectBuilder.start("$eq", "view").get());
		query.put("createTime", BasicDBObjectBuilder.start("$gte", startDate).add("$lt", endDate).get());
		return mongoTemplate.getCollection("t_guest_browsing_log").find(query).count();

	}

	@Deprecated
	public Integer getPV() {
		DBObject query = new BasicDBObject();
		query.put("operateMethod", BasicDBObjectBuilder.start("$eq", "view").get());
		return mongoTemplate.getCollection("t_guest_browsing_log").find(query).count();
	}

	public Integer getUvByTime(LocalDateTime startTime, LocalDateTime endTime) {
		Date startDate = localDateTimeFormatToDate(startTime);
		Date endDate = localDateTimeFormatToDate(endTime);
		DBObject query = new BasicDBObject();
		query.put("createTime", BasicDBObjectBuilder.start("$gte", startDate).add("$lt", endDate).get());
		List distinct = mongoTemplate.getCollection("t_guest_browsing_log").distinct("cookieVisitor_uuid", query);
		return distinct.size();

	}

	@Deprecated
	public Integer getUv() {
		List distinct = mongoTemplate.getCollection("t_guest_browsing_log").distinct("cookieVisitor_uuid");
		return distinct.size();
	}

	private Date localDateTimeFormatToDate(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		return Date.from(zdt.toInstant());
	}

}
