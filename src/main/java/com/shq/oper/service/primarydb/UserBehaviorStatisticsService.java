package com.shq.oper.service.primarydb;

import java.time.LocalDateTime;

import com.shq.oper.model.domain.primarydb.UserBehaviorStatistics;
import com.shq.oper.model.dto.Msg;

public interface UserBehaviorStatisticsService extends BaseService<UserBehaviorStatistics, Long> {

	Msg<String> insertByDate(LocalDateTime dto);
}