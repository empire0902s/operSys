package com.shq.oper.controller.mongo;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.MongoDao;
import com.shq.oper.model.domain.mongo.AdminActionLog;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.yml.SystemProperties;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 后台用户日志
 * 
 * @author tjun
 */
@RestController
@Slf4j
@RequestMapping("/mongo/adminactionlog")
public class AdminActionLogController extends BaseController {
	
	@Autowired
	private MongoDao<AdminActionLog> mongoDao;
	
	@Autowired
	SystemProperties systemProperties;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}


	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<AdminActionLog> list(AdminActionLog entity,String timeRange, PageDto page) throws ParseException {
		SimpleDateFormat format =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		if (!StringUtils.isEmpty(timeRange)) {
			String[] strings = timeRange.split(" - ");
			entity.setStartTime(strings[0] + " 00:00:00");
			entity.setEndTime(strings[1] + " 23:59:59");
		}
		Query query = new Query();
		if (!StringUtils.isEmpty(entity.getAdminName())){
			query.addCriteria(Criteria.where("admin_name").is(entity.getAdminName()));
		}
		if (!StringUtils.isEmpty(entity.getStartTime())&&StringUtils.isEmpty(entity.getEndTime())){
			query.addCriteria(Criteria.where("create_time").gte(format.parse(entity.getStartTime())));
		}
		if (!StringUtils.isEmpty(entity.getEndTime())&&StringUtils.isEmpty(entity.getStartTime())){
			query.addCriteria(Criteria.where("create_time").lte(format.parse(entity.getEndTime())));
		}
		if (!StringUtils.isEmpty(entity.getStartTime())&&!StringUtils.isEmpty(entity.getEndTime())){
			query.addCriteria(Criteria.where("create_time").gte(format.parse(entity.getStartTime())).lte(format.parse(entity.getEndTime())));
		}
		if (!StringUtils.isEmpty(entity.getAction())){
			query.addCriteria(Criteria.where("action").is(entity.getAction()));
		}
		if (!StringUtils.isEmpty(entity.getActionPara())){
			query.addCriteria(Criteria.where("action_para").regex(".*?" +entity.getActionPara()+ ".*"));
		}
		PageInfo<AdminActionLog> entitys=mongoDao.findByQueryPage(query,page,entity);
		return Data.ok(entitys);
	}


}