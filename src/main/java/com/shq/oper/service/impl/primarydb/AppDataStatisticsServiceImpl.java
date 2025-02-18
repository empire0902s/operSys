package com.shq.oper.service.impl.primarydb;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.shq.oper.dao.MongoDao;
import com.shq.oper.enums.api.BaiduPositionReportEnum;
import com.shq.oper.enums.api.HomePageReceiveCodeEnums;
import com.shq.oper.model.domain.mongo.ApiRequestDataLog;
import com.shq.oper.model.domain.mongo.AppDataStatistics;
import com.shq.oper.model.domain.mongo.AppLocationData;
import com.shq.oper.service.primarydb.ApiRequestDataLogService;
import com.shq.oper.service.primarydb.AppDataStatisticsService;
import com.shq.oper.service.primarydb.AppLocationDataService;
import com.shq.oper.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("appDataStatisticsService")
public class AppDataStatisticsServiceImpl  implements AppDataStatisticsService {
	
	@Autowired
	private MongoDao<AppDataStatistics> mongoDao;
	
	@Autowired
	private ApiRequestDataLogService apiRequestDataLogService;
	
	@Autowired
	private AppLocationDataService appLocationDataService;
	
	@Transactional
	@Override
	public Map<String,Long> getCountMap(LocalDateTime dtn) throws Exception {
		LocalDateTime todayStart = DateUtils.addDaysFormatStart(dtn, 0);
		LocalDateTime todayEnd = DateUtils.addDaysFormatEnd(dtn, 0);
		String[] timeString = todayStart.toString().split("T");
		Query query = new Query();
			query.addCriteria(Criteria.where("create_time").regex(".*?"+timeString[0]+ ".*"));
		List<AppDataStatistics> resultList = mongoDao.findByCollectionNameAll(query, AppDataStatistics.class, "t_app_data_statistics");
		//统计app安装次数
	/*	ApiRequestDataLog entity=new ApiRequestDataLog();
		entity.setStartTime(todayStart.toString().replace("T", " "));
		entity.setEndTime(todayEnd.toString().replace("T", " "));
		entity.setActionCode(BaiduPositionReportEnum.APP_POSITTION_REPORT_ENUM.getCode());
		entity.setActionDeviceType("ANDROID");
		entity.setReturnData("操作成功");
		entity.setActionPara("true");
		Long installationCounts = apiRequestDataLogService.getAppStatisticsData(entity);*/
		AppLocationData entity=new AppLocationData();
		entity.setIsInstallStartUp(true);
		long installationCounts = appLocationDataService.getAppInstallationCounts(entity,todayStart,todayEnd);
		
		//统计app启动次数
		ApiRequestDataLog entityTemp=new ApiRequestDataLog();
		entityTemp.setActionCode(HomePageReceiveCodeEnums.ADV_APP_LIST.getCode());
		entityTemp.setReturnData("操作成功");
		entityTemp.setStartTime(todayStart.toString().replace("T", " "));
		entityTemp.setEndTime(todayEnd.toString().replace("T", " "));
		long startupCounts = apiRequestDataLogService.getAppStatisticsData(entityTemp);

		Map<String,Long> map=new HashMap<>();
		map.put("installationCounts",installationCounts);
		map.put("startupCounts",startupCounts);
		return map;

	}
}