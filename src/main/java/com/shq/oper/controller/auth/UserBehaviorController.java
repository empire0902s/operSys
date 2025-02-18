package com.shq.oper.controller.auth;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.GuestBrowsingLogTemplate;
import com.shq.oper.mapper.primarydb.UserBehaviorStatisticsMapper;
import com.shq.oper.model.domain.primarydb.UserBehaviorStatistics;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.service.primarydb.UserBehaviorStatisticsService;
import com.shq.oper.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Banner
 * 
 * @author Administrator
 *
 */
@RestController
@Slf4j
@RequestMapping("/auth/behavior")
public class UserBehaviorController extends BaseController {

	@Autowired
	private UserBehaviorStatisticsMapper userBehaviorStatisticsMapper;

	@Autowired
	private UserBehaviorStatisticsService userBehaviorStatisticsService;

	@Autowired
	private GuestBrowsingLogTemplate guestBrowsingLogTemplate;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<UserBehaviorStatistics> list(UserBehaviorStatistics entity, String timeRange, PageDto page,
			HttpServletRequest request) {

		LocalDateTime start = DateUtils.addDaysFormatStart(LocalDateTime.now(), 0);
		LocalDateTime end = DateUtils.addDaysFormatEnd(LocalDateTime.now(), 0);
		UserBehaviorStatistics tmp = new UserBehaviorStatistics();
		tmp.setStatisticsTime(DateUtils.addDaysFormatStart(LocalDateTime.now(), 0).withNano(0));
		UserBehaviorStatistics selectOne = userBehaviorStatisticsService.selectOne(tmp);
		if (!StringUtils.isEmpty(selectOne)) {
			// 不为空，则更新
			selectOne.setIp(guestBrowsingLogTemplate.getDistinctTotalIpByTime(start, end));
			selectOne.setPv(guestBrowsingLogTemplate.getPVByTime(start, end));
			selectOne.setUv(guestBrowsingLogTemplate.getUvByTime(start, end));
			userBehaviorStatisticsService.update(selectOne);
		} else {
			// 为空，则新增
			userBehaviorStatisticsService.insertByDate(LocalDateTime.now());
		}
		LocalDateTime begin = null;
		LocalDateTime last = null;
		if (!StringUtils.isEmpty(timeRange)) {
			String[] strings = timeRange.split(" - ");
			// 拼接xx-xx-xx :00:00:00
			begin = DateUtils.parse(strings[0] + " 00:00:00");
			last = DateUtils.parse(strings[1] + " 00:00:00");
		}
		PageHelper.startPage(page.getPage(), page.getLimit());
		Page<UserBehaviorStatistics> entitys = userBehaviorStatisticsMapper.queryUserBehaviorStatistics(entity, begin,
				last);
		return Data.ok(entitys);
	}
}
