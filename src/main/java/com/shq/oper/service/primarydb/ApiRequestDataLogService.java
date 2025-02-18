package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.mongo.ApiRequestDataLog;

public interface ApiRequestDataLogService {

	Long getAppStatisticsData(ApiRequestDataLog entity) throws Exception;

	
}