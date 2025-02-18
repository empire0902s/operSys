package com.shq.oper.service.impl.primarydb;

import java.time.LocalDateTime;
import java.util.List;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageInfo;
import com.shq.oper.dao.mongo.HomePageModuleMongo;
import com.shq.oper.model.domain.mongo.HomePageModule;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.service.primarydb.HomePageModuleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("homePageModuleService")
public class HomePageModuleServiceImpl implements HomePageModuleService {

	@Autowired
	private HomePageModuleMongo mongoDao;

	public Msg<String> save(HomePageModule homePageModule) {
		if(StringUtils.isEmpty(homePageModule.getSortNum())) {
			return Msg.error("排序不能为空");
		}

		try {
			//排序不是默认1000，则修改库中与当前排序相同的数据的排序为1000
//			if(homePageModule.getSortNum()!=1000) {
//				HomePageModule tempHomePageModule=new HomePageModule();
//				tempHomePageModule.setSortNum(homePageModule.getSortNum());
//				Query query = new Query();
//					query.addCriteria(Criteria.where("sortNum").is(homePageModule.getSortNum()));
//				PageInfo<HomePageModule>resultPage = mongoDao.findByQueryPage(query,new PageDto(),tempHomePageModule);
//				if(resultPage.getList().size()>0) {
//					for (HomePageModule tmp : resultPage.getList()) {
//						//排序设置为1000
//						tmp.setSortNum(1000);
//					}
//					//更新数据
//					HomePageModule result = resultPage.getList().get(0);
//					result.setUpdateAdmin(homePageModule.getUpdateAdmin());
//					result.setUpdateTime(LocalDateTime.now().toString());
//					mongoDao.update(result);
//				}
//			}

			//排序不是默认1000，则修改库中与当前排序相同的数据的排序为1000
			if(homePageModule.getSortNum()!=1000) {
				Query query = new Query();
				query.addCriteria(Criteria.where("sortNum").is(homePageModule.getSortNum()));
				Update update = Update.update("sortNum", "1000");
				mongoDao.updateMulti(query,update,HomePageModule.class);
			}

			//设置更新时间
			homePageModule.setUpdateTime(LocalDateTime.now().toString());
			if (StringUtils.isEmpty(homePageModule.getId())) {
				homePageModule.setId(null);
				homePageModule.setCreateTime(LocalDateTime.now().toString());
				homePageModule.setCreateAdmin(homePageModule.getUpdateAdmin());
				mongoDao.insert(homePageModule);
			} else {
				BasicDBObject obj = new BasicDBObject();
				obj.put("_id", new ObjectId(homePageModule.getId()));
				Query query=new BasicQuery(obj);

				Update update = new Update();
				update.set("moduleName", homePageModule.getModuleName());
				update.set("sortNum", homePageModule.getSortNum());
				update.set("styleType", homePageModule.getStyleType());
				update.set("actityType", homePageModule.getActityType());
				update.set("loadMoreTarget",homePageModule.getLoadMoreTarget());
				update.set("updateTime",LocalDateTime.now().toString());
				mongoDao.updateFirst(query,update,HomePageModule.class);
			}
			return Msg.ok("保存成功");
		} catch (Exception e) {
			return Msg.error("保存失败");
		}
	}

}