package com.shq.oper.config;

import com.shq.oper.model.domain.mongo.AppLocationData;
import com.shq.oper.service.primarydb.AppLocationDataService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.shq.oper.dao.mongo.OperSysSettingMongo;
import com.shq.oper.model.domain.mongo.OperSysSetting;
import com.shq.oper.model.domain.mongo.PriceReductionRules;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.impl.primarydb.AdminServiceImpl;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/** 
 *  Order注解的执行优先级是按value值从小到大顺序。 
 */  
@Component  
@Order(value=1000)  
@Slf4j
public class StartInitConfig implements ApplicationRunner{

	@Autowired
	private OperSysSettingMongo operSysSettingMongo;

	@Autowired
	private AppLocationDataService appLocationDataService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.debug("-----------------StartInitConfig----CacheKey------");
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_OPERSYS_KEY_SET);
		
		initPriceReductionRulesMongo();
	}

	private void initPriceReductionRulesMongo() {
		String dtDow = DateUtils.now();
		String createAdmin = "adminApi-0";
		
		OperSysSetting entity = operSysSettingMongo.findOneByuniqueKey("showCode", PriceReductionRules.class.getName(), OperSysSetting.class);
		if( entity == null) {
			entity = new OperSysSetting();
			PriceReductionRules rules = new PriceReductionRules();
			rules.setId(new ObjectId().toHexString());
			
			rules.setUpdateAdmin(createAdmin);
			rules.setUpdateTime(dtDow);
			entity.setUpdateAdmin(createAdmin);
			entity.setUpdateTime(dtDow);
			entity.setShowCode(PriceReductionRules.class.getName());
			entity.setJsonValue(rules);
			operSysSettingMongo.insert(entity);
			log.debug("---初始化--PriceReductionRules--");
		}

//
		log.debug("-----------------App安装与启动次数统计修复开始----------"+DateUtils.now());

//		LocalDateTime dtn=LocalDateTime.now();
		LocalDateTime now=LocalDateTime.now();

		String str="2018-11-17 00:00:00";
		LocalDateTime dtn=DateUtils.parse(str);

		while (now.isAfter(dtn)){

			LocalDateTime yesterdayStart = DateUtils.addDaysFormatStart(dtn, -1);
			LocalDateTime yesterdayEnd = DateUtils.addDaysFormatEnd(dtn, -1);
			AppLocationData entity1=new AppLocationData();
			dtn=dtn.plusDays(1);

			appLocationDataService.saveAppDataStatistics(entity1,yesterdayStart,yesterdayEnd);
		}


		log.debug("-----------------App安装与启动次数统计修复结束----------"+DateUtils.now());
		
	}

}
