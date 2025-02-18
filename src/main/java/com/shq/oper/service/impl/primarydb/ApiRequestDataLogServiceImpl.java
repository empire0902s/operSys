package com.shq.oper.service.impl.primarydb;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.shq.oper.dao.MongoDao;
import com.shq.oper.model.domain.mongo.ApiRequestDataLog;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.service.primarydb.ApiRequestDataLogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("apiRequestDataLogService")
public class ApiRequestDataLogServiceImpl implements ApiRequestDataLogService {

    
	@Autowired
	private MongoDao<ApiRequestDataLog> apiRequestDataLogMongo;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	

	@Override
	public Long getAppStatisticsData(ApiRequestDataLog entity) throws Exception {
		SimpleDateFormat format =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		Criteria cr = new Criteria();
		Criteria c1=Criteria.where("action_device_type").is("ANDROID");
		Criteria c2=Criteria.where("action_device_type").is("IOS");
		Query query = new Query(cr.orOperator(c1,c2));
		if (!StringUtils.isEmpty(entity.getStartTime())&&StringUtils.isEmpty(entity.getEndTime())){
			query.addCriteria(Criteria.where("create_time").gte(format.parse(entity.getStartTime())));
		}
		if (!StringUtils.isEmpty(entity.getEndTime())&&StringUtils.isEmpty(entity.getStartTime())){
			query.addCriteria(Criteria.where("create_time").lte(format.parse(entity.getEndTime())));
		}
		if (!StringUtils.isEmpty(entity.getStartTime())&&!StringUtils.isEmpty(entity.getEndTime())){
			query.addCriteria(Criteria.where("create_time").gte(format.parse(entity.getStartTime())).lte(format.parse(entity.getEndTime())));
		}
		if (!StringUtils.isEmpty(entity.getActionCode())){
			query.addCriteria(Criteria.where("action_code").is(entity.getActionCode()));
		}
		/*if (!StringUtils.isEmpty(entity.getActionDeviceType())){
			
		}*/
		if (!StringUtils.isEmpty(entity.getReturnData())){
			query.addCriteria(Criteria.where("return_data").regex(".*?" +entity.getReturnData()+ ".*"));
		}
		return apiRequestDataLogMongo.selectCount(query, ApiRequestDataLog.class);
	}
	
}