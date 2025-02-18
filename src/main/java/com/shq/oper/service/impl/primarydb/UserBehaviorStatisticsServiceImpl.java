package com.shq.oper.service.impl.primarydb;

import com.shq.oper.dao.GuestBrowsingLogTemplate;
import com.shq.oper.model.domain.primarydb.UserBehaviorStatistics;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.service.primarydb.UserBehaviorStatisticsService;
import com.shq.oper.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
@Slf4j
@Service("userBehaviorStatisticsService")
public class UserBehaviorStatisticsServiceImpl extends BaseServiceImpl<UserBehaviorStatistics, Long>
		implements UserBehaviorStatisticsService {

	@Autowired
	private GuestBrowsingLogTemplate guestBrowsingLogTemplate;
	@Autowired
	private UserBehaviorStatisticsService userBehaviorStatisticsService;

	@Transactional
	@Override
	public Msg<String> insertByDate(LocalDateTime date) {
		if (!StringUtils.isEmpty(date)) {
			UserBehaviorStatistics tmp = new UserBehaviorStatistics();
			tmp.setStatisticsTime(DateUtils.addDaysFormatStart(date, 0).withNano(0));
			UserBehaviorStatistics selectOne = userBehaviorStatisticsService.selectOne(tmp);
			// 如果记录已经存在，删除该记录
			if (!StringUtils.isEmpty(selectOne)) {
				userBehaviorStatisticsService.delete(selectOne);
			} else {
				// 计算两个时间的分钟差
				Duration duration = java.time.Duration.between(date, LocalDateTime.now());
				if (duration.toDays() < 0) {
					return Msg.error("很抱歉，不能统计未来的日期记录");
				}
			}
			LocalDateTime start = DateUtils.addDaysFormatStart(date, 0);
			LocalDateTime end = DateUtils.addDaysFormatEnd(date, 0);
			UserBehaviorStatistics tmpUserBehaviorStatistics = new UserBehaviorStatistics();
			tmpUserBehaviorStatistics.setCreateTime(LocalDateTime.now());
			tmpUserBehaviorStatistics.setStatisticsTime(DateUtils.addDaysFormatStart(date, 0).withNano(0));
			tmpUserBehaviorStatistics.setIp(guestBrowsingLogTemplate.getDistinctTotalIpByTime(start, end));
			tmpUserBehaviorStatistics.setPv(guestBrowsingLogTemplate.getPVByTime(start, end));
			tmpUserBehaviorStatistics.setUv(guestBrowsingLogTemplate.getUvByTime(start, end));
			userBehaviorStatisticsService.insert(tmpUserBehaviorStatistics);
			return Msg.ok("统计完毕");
		}
		return Msg.error("统计错误，没有指定统计时间！");

	}
}