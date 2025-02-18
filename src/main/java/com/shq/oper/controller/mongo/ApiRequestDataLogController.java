package com.shq.oper.controller.mongo;

import javax.servlet.http.HttpServletRequest;

import com.shq.oper.enums.api.*;
import com.shq.oper.model.domain.primarydb.Dict;
import com.shq.oper.service.primarydb.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
import com.shq.oper.model.domain.mongo.ApiRequestDataLog;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.BaseReceiveParamDto;
import com.shq.oper.model.dto.yml.SystemProperties;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 后台用户日志
 * 
 * @author tjun
 */
@RestController
@Slf4j
@RequestMapping("/mongo/apirequestdatalog")
public class ApiRequestDataLogController extends BaseController {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MongoDao<ApiRequestDataLog> mongoDao;

	@Autowired
	private DictService dictService;
	
	@Autowired
	SystemProperties systemProperties;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		
		BaseReceiveParamDto result=new BaseReceiveParamDto();
		List<String>codeList=new LinkedList<>();
		Map<Object, Object> codeMap=new LinkedHashMap<>();
		Map<String, ReceiveCodeEnums> map = ReceiveCodeEnums.getMap();//1001--1010
		Map<String, ActivityReceiveCodeEnums> map2 = ActivityReceiveCodeEnums.getMap();//2001--2009
		Map<String, OrderReceiveCodeEnums> map3 = OrderReceiveCodeEnums.getMap();//3001--3003
		Map<String, HomePageReceiveCodeEnums> map4 = HomePageReceiveCodeEnums.getMap();//5001--5003
//		Map<String,MessageSendEnum> map5=MessageSendEnum.getMap();
		Map<String,BaiduPositionReportEnum> map6=BaiduPositionReportEnum.getMap();
		Map<String,ChannelEnums> map7=ChannelEnums.getMap();

		Map<String,CirCleReceiveCodeEnums> map8=CirCleReceiveCodeEnums.getMap();
		Map<String,PubliceNumEnums> map9=PubliceNumEnums.getMap();

		Map<String,String> map5=new HashMap<>();
		Dict dictParam=new Dict();
		dictParam.setCode("messageParam");
		List<Dict> dictList=dictService.select(dictParam);

		for (String key : map.keySet()) {
			ReceiveCodeEnums receiveCodeEnums = map.get(key);
			codeMap.put("["+key+"]-"+receiveCodeEnums.getName(),key);
		}
		for (String key : map2.keySet()) {
			ActivityReceiveCodeEnums activityReceiveCodeEnums = map2.get(key);
			codeMap.put("["+key+"]-"+activityReceiveCodeEnums.getName(),key);
		}
		for (String key : map3.keySet()) {
			OrderReceiveCodeEnums orderReceiveCodeEnums = map3.get(key);
			codeMap.put("["+key+"]-"+orderReceiveCodeEnums.getName(),key);
		}

		if (null!=dictList&&dictList.size()>0){
			dictList.forEach(
					p->{
						codeMap.put("["+p.getDictKey()+"]-"+p.getName()+"信息发送",p.getDictKey());
					}
			);
		}
//
//		for (String key : map5.keySet()) {
//			MessageSendEnum messageSendEnum = map5.get(key);
//			codeMap.put("["+key+"]-"+messageSendEnum.getName()+"信息发送",key);
//		}
		for (String key : map4.keySet()) {
			HomePageReceiveCodeEnums homePageReceiveCodeEnums = map4.get(key);
			codeMap.put("["+key+"]-"+homePageReceiveCodeEnums.getName(),key);
		}
		for (String key : map6.keySet()) {
			BaiduPositionReportEnum baiduPositionReportEnum = map6.get(key);
			codeMap.put("["+key+"]-"+baiduPositionReportEnum.getName(),key);
		}
		for (String key : map7.keySet()) {
			ChannelEnums channelEnums = map7.get(key);
			codeMap.put("["+key+"]-"+channelEnums.getName(),key);
		}

		for (String key : map8.keySet()) {
			CirCleReceiveCodeEnums channelEnums = map8.get(key);
			codeMap.put("["+key+"]-"+channelEnums.getName(),key);
		}

		for (String key : map9.keySet()) {
			PubliceNumEnums publiceNumEnums = map9.get(key);
			codeMap.put("["+key+"]-"+publiceNumEnums.getName(),key);
		}


		List<String> deviceTypeList = result.getDeviceTypeList();
		request.setAttribute("deviceTypeList", deviceTypeList);
		request.setAttribute("codeMap", codeMap);
		return toPage(request);
	}


	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<ApiRequestDataLog> list(ApiRequestDataLog entity,String timeRange, PageDto page) throws ParseException {

		SimpleDateFormat format =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		
		if (!StringUtils.isEmpty(timeRange)) {
			String[] strings = timeRange.split(" - ");
			entity.setStartTime(strings[0] + " 00:00:00");
			entity.setEndTime(strings[1] + " 23:59:59");
		}
		Query query = new Query();
		if (!StringUtils.isEmpty(entity.getActionCode())){
			query.addCriteria(Criteria.where("action_code").is(entity.getActionCode()));
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
		if (!StringUtils.isEmpty(entity.getActionDeviceType())){
			query.addCriteria(Criteria.where("action_device_type").is(entity.getActionDeviceType()));
		}
		if (!StringUtils.isEmpty(entity.getActionPara())){
			query.addCriteria(Criteria.where("action_para").regex(".*?" +entity.getActionPara()+ ".*"));
		}
		if (!StringUtils.isEmpty(entity.getReturnData())){
			query.addCriteria(Criteria.where("return_data").regex(".*?" +entity.getReturnData()+ ".*"));
		}
		PageInfo<ApiRequestDataLog> entitys=mongoDao.findByQueryPage(query,page,entity);
		return Data.ok(entitys);
	}


}