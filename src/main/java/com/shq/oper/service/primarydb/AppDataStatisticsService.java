package com.shq.oper.service.primarydb;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AppDataStatisticsService {

	Map<String,Long> getCountMap(LocalDateTime dtn) throws Exception ;
	
	
}