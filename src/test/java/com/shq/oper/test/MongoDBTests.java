package com.shq.oper.test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shq.oper.model.domain.mongo.AppDataStatistics;
import com.shq.oper.model.domain.primarydb.deductible.Deductible;
import com.shq.oper.model.dto.CountChannelDto;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.service.primarydb.DeductibleService;
import com.shq.oper.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.PageInfo;
import com.shq.oper.dao.MongoDao;
import com.shq.oper.dao.mongo.AdminActionLogMongo;
import com.shq.oper.dao.mongo.ApiRequestDataLogMongo;
import com.shq.oper.model.domain.mongo.AdminActionLog;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MongoDBTests {

	@Autowired
	private ApiRequestDataLogMongo apiRequestDataLogMongo;
	@Autowired
	private AdminActionLogMongo adminActionLogMongo;

	@Autowired
	private DeductibleService deductibleService;


	

	@Test
	public void testSaveMopngo() {


		AppDataStatistics appDataStatistics=new AppDataStatistics();
		int nstallationCounts=0;

		String str="[{\"count\":1},{\"_id\":6,\"count\":1},{\"_id\":3,\"count\":5},{\"_id\":7,\"count\":20},{\"_id\":1,\"count\":38},{\"_id\":9,\"count\":12},{\"_id\":99,\"count\":6},{\"_id\":5,\"count\":9}]";

		List<CountChannelDto> countList=JsonUtils.parseList(str,CountChannelDto.class);
		for (CountChannelDto p:countList){
			nstallationCounts+=p.getCount();
			if (StringUtils.isEmpty(p.get_id())){
				continue;
			}
			if (p.get_id()==99){
				appDataStatistics.setInstaCountIOS(p.getCount());
			}
			if (p.get_id()==0){
				appDataStatistics.setInstaCounts520(p.getCount());
			}
			if (p.get_id()==1){
				appDataStatistics.setInstaCounts360(p.getCount());
			}
			if (p.get_id()==2){
				appDataStatistics.setInstaCountBaidu(p.getCount());
			}
			if (p.get_id()==3){
				appDataStatistics.setInstaCountsTencent(p.getCount());
			}
			if (p.get_id()==4){
				appDataStatistics.setInstaCountSugou(p.getCount());
			}
			if (p.get_id()==5){
				appDataStatistics.setInstaCountHuawei(p.getCount());
			}
			if (p.get_id()==6){
				appDataStatistics.setInstaCountXiaomi(p.getCount());
			}
			if (p.get_id()==7){
				appDataStatistics.setInstaCountVivo(p.getCount());
			}
			if (p.get_id()==8){
				appDataStatistics.setInstaCountWandoujia(p.getCount());
			}
			if (p.get_id()==10){
				appDataStatistics.setInstaCountJinli(p.getCount());
			}
		}


		System.out.println(JsonUtils.toDefaultJson(countList));








//		LocalDateTime expiredDayDt = LocalDateTime.now();
//		String expiredDay = DateUtils.to(expiredDayDt, DateUtils.DateFormat.LONG_DATE_PATTERN_LINE);
//		Msg<String> msg = deductibleService.updateDetuctibleStatusExpiredByJob(expiredDay);

//
//		LocalDateTime expiredDayDt = LocalDateTime.now();
//		expiredDayDt=expiredDayDt.plusDays(-1);
//		deductibleService.saveDeducitbleStatisticsDay(expiredDayDt);


//		String expiredDay = DateUtils.to(expiredDayDt, DateUtils.DateFormat.SHORT_DATE_PATTERN_LINE);
//		Msg<String> msg = deductibleService.updateDetuctibleStatusExpiredByJob(expiredDay);
//		System.out.println(msg);
//
//		Map<String,String> parMap = new HashMap<>();
//		parMap.put("code_01", "value01");
//		parMap.put("code_02", "value02");
//		AdminActionLog adminActionLog = new AdminActionLog();
//		adminActionLog.setAction("/index000");
//		adminActionLog.setRefAction("ref000");
//		adminActionLog.setAdminId(1L);
//		adminActionLog.setCreateTime(LocalDateTime.now());
//		adminActionLog.setIp("0.0.0.0.1");
//		adminActionLog.setActionPara(JsonUtils.toDefaultJson(parMap));
//		log.debug("adminActionLog====="+JsonUtils.toDefaultJson(adminActionLog));
//
//		adminActionLogMongo.insert(adminActionLog);
//
//		log.info("=========================");
	}
	
	@Test
	public void testMopngoPage() {
		AdminActionLog adminActionLog = new AdminActionLog();
		adminActionLog.setCreateTime(null);
//		adminActionLog.setRefAction("ref222");
//		Query query = new Query();
//		query.addCriteria(Criteria.where("admin_id").is(1L));
//		query.addCriteria(Criteria.where("ref_action").is("333"));
		PageInfo<AdminActionLog> pageData = adminActionLogMongo.findByEntity(adminActionLog, new PageDto(1,3));
		log.debug("adminActionLog====="+JsonUtils.toDefaultJson(pageData));
		log.debug("adminActionLog====="+JsonUtils.toDefaultJson(pageData.getList()));
		
		
		log.info("=========================");
	}
	@Test
	public void testRemove() {
		
		//adminActionLog.setAdminId(1L);
		Query query = new Query();
//		query.addCriteria(Criteria.where("admin_id").is(1L));
		List<AdminActionLog> list = adminActionLogMongo.remove(query, AdminActionLog.class);
		
		log.debug("testRemove====="+JsonUtils.toDefaultJson(list));
		log.debug("testRemove==Num==="+list.size());
	}


}