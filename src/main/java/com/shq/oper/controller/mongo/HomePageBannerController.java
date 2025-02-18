package com.shq.oper.controller.mongo;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Map;

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
import com.shq.oper.dao.mongo.HomePageBannerMongo;
import com.shq.oper.enums.HomePageModuleAvtiveEnum;
import com.shq.oper.model.domain.mongo.HomePageBanner;
import com.shq.oper.model.domain.mongo.HomePageModule;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.service.primarydb.HomePageModuleService;
import com.shq.oper.util.JsonUtils;

import groovyjarjarcommonscli.ParseException;
import lombok.extern.slf4j.Slf4j;

/**
 * 爱之家首页Banner模块
 * 
 * @author linagjinzhao
 */
@RestController
@Slf4j
@RequestMapping("/mongo/homepagebanner")
public class HomePageBannerController extends BaseController {
	
	@Autowired
	private HomePageBannerMongo mongoDao;
	
	@Autowired
	private HomePageModuleService homePageModuleService;
	
	@Autowired
	SystemProperties systemProperties;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
    Map<String, Object> homePageModuleAvtiveEnumMap = HomePageModuleAvtiveEnum.getObjectMap();
    request.setAttribute("homePageModuleAvtiveEnumMap", homePageModuleAvtiveEnumMap);
		return toPage(request);
	}
	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, HomePageBanner homePageBanner) {
		
		homePageBanner.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		homePageBanner.setUpdateTime(LocalDateTime.now().toString());
		
		if(StringUtils.isEmpty(homePageBanner.getId())) {
			homePageBanner.setId(null);
			homePageBanner.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
			homePageBanner.setCreateTime(LocalDateTime.now().toString());
			mongoDao.insert(homePageBanner);
		}else {
			mongoDao.update(homePageBanner);
		}
		return Msg.ok("保存成功");
	}
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<HomePageBanner> list(HomePageBanner entity,PageDto page) throws ParseException {
		Query query = new Query();
	if (!StringUtils.isEmpty(entity.getImgTitle())){
			query.addCriteria(Criteria.where("imgTitle").is(entity.getImgTitle()));
		}
	if (!StringUtils.isEmpty(entity.getModuleId())){
		query.addCriteria(Criteria.where("moduleId").is(entity.getModuleId()));
	}
		PageInfo<HomePageBanner> entitys=mongoDao.findByQueryPage(query,page,entity);
		return Data.ok(entitys);
	}

}