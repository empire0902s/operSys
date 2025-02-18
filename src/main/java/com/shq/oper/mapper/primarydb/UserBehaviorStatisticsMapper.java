package com.shq.oper.mapper.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.UserBehaviorStatistics;
import com.shq.oper.util.BaseMapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserBehaviorStatisticsMapper extends BaseMapper<UserBehaviorStatistics> {

	Page<UserBehaviorStatistics> queryUserBehaviorStatistics(UserBehaviorStatistics entity,
			@Param(value = "begin") LocalDateTime begin, @Param(value = "last") LocalDateTime last);
}