package com.shq.oper.controller.mongo;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.mongo.HomePageModuleAttachedMongo;
import com.shq.oper.dao.mongo.HomePageModuleMongo;
import com.shq.oper.model.domain.mongo.HomePageModule;
import com.shq.oper.model.domain.mongo.HomePageModuleAttached;
import com.shq.oper.model.domain.mongo.PriceReductionGoods;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.service.primarydb.HomePageModuleService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* 爱之家首页模块附属模块
* 项目名称：operSys   
* 类名称：HomePageModuleAttachedController   
* 类描述：   
* 创建人：ljz   
* 创建时间：2018年8月22日 下午5:54:18   
* 修改人：ljz   
* 修改时间：2018年8月22日 下午5:54:18   
* 修改备注：   
* @version    
*
 */
@RestController
@Slf4j
@RequestMapping("/mongo/homePageModuleAttached")
public class HomePageModuleAttachedController extends BaseController {

	
	@Autowired
	private HomePageModuleAttachedMongo HomePageModuleAttachedDao;
	
	@Autowired
	private HomePageModuleMongo homePageModuleMongoDao;

	@Autowired
	private HomePageModuleService homePageModuleService;

	@Autowired
	SystemProperties systemProperties;

	
	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, HomePageModuleAttached homePageModuleAttached) throws Exception {
		
		homePageModuleAttached.setUpdateTime(LocalDateTime.now().toString());
		homePageModuleAttached.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		HomePageModule temp =new HomePageModule();
		if(!StringUtils.isEmpty(homePageModuleAttached.getModuleId())) {
			temp = homePageModuleMongoDao.findById(homePageModuleAttached.getModuleId(), HomePageModule.class);
		}
		if(!StringUtils.isEmpty(temp)&&!StringUtils.isEmpty(temp.getModuleAttached())) {
			//判断homePageModuleAttached是新增还是编辑
			if(!StringUtils.isEmpty(temp.getModuleAttached().getModuleId())) {
				//编辑
				BeanUtils.copyProperties(temp.getModuleAttached(), homePageModuleAttached);
				temp.getModuleAttached().setUpdateTime(LocalDateTime.now().toString());
				temp.getModuleAttached().setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
			}else {
				//新增
				homePageModuleAttached.setCreateTime(LocalDateTime.now().toString());
				homePageModuleAttached.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
				temp.setModuleAttached(homePageModuleAttached);
			}
		}
		
		Query query = Query.query(Criteria.where("id").is(homePageModuleAttached.getModuleId()));
        Update update = new Update().set("moduleAttached", temp.getModuleAttached());
        homePageModuleMongoDao.updateFirst(query, update, HomePageModule.class);
		return Msg.ok("保存成功");
	}
	
}