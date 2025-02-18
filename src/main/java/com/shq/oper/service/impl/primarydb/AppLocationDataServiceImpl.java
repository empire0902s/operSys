package com.shq.oper.service.impl.primarydb;


import com.mongodb.BasicDBObject;
import com.shq.oper.dao.MongoDao;
import com.shq.oper.model.domain.mongo.AppDataStatistics;
import com.shq.oper.model.domain.mongo.AppLocationData;
import com.shq.oper.model.dto.CountChannelDto;
import com.shq.oper.service.primarydb.AppLocationDataService;
import com.shq.oper.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service("appLocationDataService")
public class AppLocationDataServiceImpl implements AppLocationDataService {


	@Autowired
	private MongoDao<AppLocationData> appLocationDataMongo;

	@Autowired
	private MongoDao<AppDataStatistics> appDataStatisticsMongoDao;
	
	@Override
	public Long getAppInstallationCounts(AppLocationData entity, LocalDateTime start, LocalDateTime end) throws Exception {
		SimpleDateFormat format =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
 		Query query = new Query();
			if (!StringUtils.isEmpty(start)&&!StringUtils.isEmpty(end)){
			query.addCriteria(Criteria.where("createTime").gte(start.toString()).lte(end.toString()));
		}
		if (!StringUtils.isEmpty(entity.getIsInstallStartUp())){
			query.addCriteria(Criteria.where("isInstallStartUp").is(entity.getIsInstallStartUp()));
		}
		return appLocationDataMongo.selectCount(query, AppLocationData.class);
	}

	@Override
	public void saveAppDataStatistics(AppLocationData entity, LocalDateTime start, LocalDateTime end) {

		Aggregation aggregation =Aggregation.newAggregation(
				Aggregation.match(Criteria.where("createTime").gte(start.toString()).lte(end.toString())
						.and("isInstallStartUp").is(true)),
				Aggregation.group("channel").count().as("count")
		) ;

		AggregationResults<BasicDBObject> results = appLocationDataMongo.aggregate(aggregation, "t_app_location_data");

		List<BasicDBObject> mappedResults=results.getMappedResults();
		String str=JsonUtils.toDefaultJson(mappedResults);
		System.out.println(str);

		List<CountChannelDto> countList=JsonUtils.parseList(str,CountChannelDto.class);
		AppDataStatistics appDataStatistics=new AppDataStatistics();

		int nstallationCounts=0;

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

		Query query=new Query();
		query.addCriteria(Criteria.where("createTime").gte(start.toString()).lte(end.toString()));
		Long count=appLocationDataMongo.selectCount(query,AppLocationData.class);
		appDataStatistics.setStartupCounts(count);
		appDataStatistics.setInstallationCounts(nstallationCounts);
		appDataStatistics.setCreateTime(start.toString());
		appDataStatistics.setCheckCreateTime(start);

		Query query1=new Query();
		start=start.plusDays(1);
		end=end.plusDays(1);
		query1.addCriteria(Criteria.where("create_time").gte(start.toString()).lte(end.toString()));
		long  count1=appDataStatisticsMongoDao.selectCount(query1,AppDataStatistics.class);
		if (count1<=0){
			appDataStatisticsMongoDao.insert(appDataStatistics);
		}
    }
}