package com.shq.oper.service.primarydb;

import java.time.LocalDateTime;

import com.shq.oper.model.domain.mongo.AppDataStatistics;
import com.shq.oper.model.domain.mongo.AppLocationData;

public interface AppLocationDataService {

	Long getAppInstallationCounts(AppLocationData entity, LocalDateTime start, LocalDateTime end) throws Exception;

	void saveAppDataStatistics(AppLocationData entity, LocalDateTime start, LocalDateTime end);
}