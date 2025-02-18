package com.shq.oper.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.shq.oper.dao.mongo.HomePageModuleMongo;
import com.shq.oper.model.domain.mongo.HomePageBanner;
import com.shq.oper.model.domain.mongo.HomePageGoods;
import com.shq.oper.model.domain.mongo.HomePageGroupItem;
import com.shq.oper.model.domain.mongo.HomePageModule;
import com.shq.oper.model.domain.mongo.HomePageModuleAttached;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class HomePageMongoDBTests {

	@Autowired
	private HomePageModuleMongo homePageModuleMongo;
	
	
	public static void main(String[] args) {
		log.info("=========================");
		
		String url = "http://192.168.1.191/localQuickPurchase/homePageIntegrationAction/homePageV2";
		String resultJson = HttpClientUtil.doGet(url);
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		
		log.debug(resultMap.keySet().toString());
		
		Map<String,Object> dataMap = (Map<String, Object>) resultMap.get("data");
		log.debug(dataMap.keySet().toString());
		
		List<Map<String,Object>> listMap =  (List<Map<String, Object>>) dataMap.get("homePageColumn");
		
		for(Map<String,Object> tmp : listMap ) {
			mapInit(tmp);
		}
		
		log.info("=========================");
	}
	
	private static Boolean validBoolean(String filedName,Map<String,Object> objMap,boolean isAllowNull) {
		Boolean bool = null;
		Object obj = objMap.get(filedName);
		if(!isAllowNull) {//不允许为空
			Assert.notNull( obj , "参数["+objMap.get("id")+"]的Boolean属性["+filedName+"]为空");
		}
		if(!StringUtils.isEmpty(obj)) {
			if (obj instanceof Boolean) {
				bool = (Boolean) obj;
			}
		}
		return bool;
	}
	private static Integer validInteger(String filedName,Map<String,Object> objMap,boolean isAllowNull) {
		Integer integer = null;
		Object obj = objMap.get(filedName);
		if(!isAllowNull) {//不允许为空
			Assert.notNull( obj , "参数["+objMap.get("id")+"]的Integer属性["+filedName+"]为空");
		}
		if(!StringUtils.isEmpty(obj)) {
			if (obj instanceof Integer) {
				integer = (Integer) obj;
			}
		}
		return integer;
	}
	private static String validString(String filedName,Map<String,Object> objMap,boolean isAllowNull) {
		String str = null;
		Object obj = objMap.get(filedName);
		if(!isAllowNull) {// 不允许为空 
			Assert.notNull( obj , "参数["+objMap.get("id")+"]的String属性["+filedName+"]为空");
		}
		if(!StringUtils.isEmpty(obj)) {
			if (obj instanceof String) {
				str = (String) obj;
			}
		}
		return str;
	}
	private static String validId(String filedName,Map<String,Object> objMap,boolean isAllowNull) {
		String str = null;
		Object obj = objMap.get(filedName);
		if(!isAllowNull) {// 不允许为空 
			Assert.notNull( obj , "参数["+objMap.get("id")+"]的String属性["+filedName+"]为空");
		}
		if(!StringUtils.isEmpty(obj)) {
			if (obj instanceof String) {
				str = (String) obj;
			}
			if (obj instanceof Integer) {
				str = String.valueOf(obj);
			}
		}
		return str;
	}
	private static List<HomePageBanner> validListBanner(String moduleId,String filedName,Map<String,Object> objMap) {
		List<HomePageBanner> list = new ArrayList<>();
		Object obj = objMap.get(filedName);
		if(!StringUtils.isEmpty(obj)) {
			List<Map<String,Object>> listMap = (List<Map<String, Object>>) obj;
			for(Map<String,Object> tmpMap : listMap) {
				HomePageBanner bean = initBannerParam(moduleId);
				
				bean.setImgTarget(validString("jumpTarget",tmpMap,true));
				bean.setImgTitle(validString("title",tmpMap,true));
				bean.setImgUrl(validString("imageLocation",tmpMap,false));
				bean.setId(validId("id",tmpMap,false));
				list.add(bean);
			}
		}
		return list;
	}
	private static List<HomePageBanner> validListBannerByCeloveColumn(String moduleId,String filedName,Map<String,Object> objMap) {
		List<HomePageBanner> list = new ArrayList<>();
		Object obj = objMap.get(filedName);
		if(!StringUtils.isEmpty(obj)) {
			List<Map<String,Object>> listMap = (List<Map<String, Object>>) obj;
			for(Map<String,Object> tmpMap : listMap) {
				HomePageBanner bean = initBannerParam(moduleId);
				
				bean.setImgTarget(validString("contentUrl",tmpMap,true));
				bean.setImgTitle(validString("storesName",tmpMap,true));
				bean.setImgUrl(validString("storesUrl",tmpMap,false));
				bean.setId(new ObjectId().toHexString());
				list.add(bean);
			}
		}
		return list;
	}
	private static HomePageModuleAttached validModuleAttached(String moduleId,String filedName,Map<String,Object> objMap) {
		HomePageModuleAttached moduleAttached = null;
		Object obj = objMap.get(filedName);
		if(!StringUtils.isEmpty(obj)) {
			moduleAttached = new HomePageModuleAttached();
			List<Map<String,Object>> listMap = (List<Map<String, Object>>) obj;
			if(listMap != null && listMap.size() > 0) {
				Map<String,Object> dataMap = listMap.get(0);
				String dtDow = DateUtils.now();
				String createAdmin = "adminApi-1";
				moduleAttached.setCreateAdmin(createAdmin);
				moduleAttached.setCreateTime(dtDow);
				moduleAttached.setUpdateAdmin(createAdmin);
				moduleAttached.setUpdateTime(dtDow);
				moduleAttached.setModuleId(moduleId);
				
				moduleAttached.setLogoURL(validString("logoURL",dataMap,true));
				moduleAttached.setLogoURLTarget(validString("logoURLTarget",dataMap,true));
				moduleAttached.setLogoBannerURL(validString("logoBannerURL",dataMap,true));
				moduleAttached.setLogoBannerURLTarget(validString("logoBannerURLTarget",dataMap,true));
				moduleAttached.setActiveBannerURL(validString("activeBannerURL",dataMap,true));
				moduleAttached.setActiveBannerURLTarget(validString("activeBannerURLTarget",dataMap,true));
				moduleAttached.setId(validId("id",dataMap,false));
				
			}
			
		}
		return moduleAttached;
	}
//	private static List<HomePageBanner> validListBannerByGroup(String moduleId,String filedName,Map<String,Object> objMap) {
//		List<HomePageBanner> list = new ArrayList<>();
//		Object obj = objMap.get(filedName);
//		if(!StringUtils.isEmpty(obj)) {
//			Map<String,Object> groupMap = (Map<String, Object>) obj;
//			Map<String,Object> bannerMap = (Map<String, Object>) groupMap.get("banner");
//			List<Map<String,Object>> listMap = Arrays.asList(bannerMap);
//			for(Map<String,Object> tmpMap : listMap) {
//				HomePageBanner bean = initBannerParam(moduleId);
//				
//				bean.setImgTarget(validString("jumpTarget",tmpMap,true));
//				bean.setImgTitle(validString("title",tmpMap,true));
//				bean.setImgUrl(validString("imageLocation",tmpMap,false));
//				bean.setId(validId("id",tmpMap,false));
//				list.add(bean);
//			}
//		}
//		return list;
//	}
//	private static List<HomePageBanner> validListBannerToGroups(String moduleId,String filedName,Map<String,Object> objMap) {
//		List<HomePageBanner> list = new ArrayList<>();
//		Object obj = objMap.get(filedName);
//		if(!StringUtils.isEmpty(obj)) {
//			List<Map<String,Object>> groups_Map = (List<Map<String,Object>>) obj;
//			for(Map<String,Object> arrMap : groups_Map) {
//				Map<String,Object> bannerMap = (Map<String, Object>) arrMap.get("banner");
//				List<Map<String,Object>> listMap = Arrays.asList(bannerMap);
//				for(Map<String,Object> tmpMap : listMap) {
//					HomePageBanner bean = initBannerParam(moduleId);
//					
//					bean.setImgTarget(validString("jumpTarget",tmpMap,true));
//					bean.setImgTitle(validString("title",tmpMap,true));
//					bean.setImgUrl(validString("imageLocation",tmpMap,false));
//					bean.setId(validId("id",tmpMap,false));
//					list.add(bean);
//				}
//			}
//		}
//		return list;
//	}

	private static HomePageBanner initBannerParam(String moduleId) {
		String dtDow = DateUtils.now();
		String createAdmin = "adminApi-1";
		HomePageBanner bean = new HomePageBanner();
		bean.setCreateAdmin(createAdmin);
		bean.setCreateTime(dtDow);
		bean.setUpdateAdmin(createAdmin);
		bean.setUpdateTime(dtDow);
		bean.setModuleId(moduleId);
		bean.setSortNum(1000);
		return bean;
	}
	
	private static HomePageGoods initGoodsParam(String moduleId) {
		String dtDow = DateUtils.now();
		String createAdmin = "adminApi-1";
		HomePageGoods bean = new HomePageGoods();
		bean.setCreateAdmin(createAdmin);
		bean.setCreateTime(dtDow);
		bean.setUpdateAdmin(createAdmin);
		bean.setUpdateTime(dtDow);
		bean.setModuleId(moduleId);
		bean.setSortNum(1000);
		return bean;
	}
	
	private static List<HomePageGoods> validListGoods(String moduleId,String filedName,Map<String,Object> objMap) {
		List<HomePageGoods> list = new ArrayList<>();
		Object obj = objMap.get(filedName);
		if(!StringUtils.isEmpty(obj)) {
			List<Map<String,Object>> listMap = (List<Map<String, Object>>) obj;
			for(Map<String,Object> tmpMap : listMap) {
				HomePageGoods bean = initGoodsParam(moduleId);
				
				bean.setGoodsCode(validString("goodsCode",tmpMap,false));
				bean.setId(validString("goodsId",tmpMap,false));
				list.add(bean);
			}
		}
		return list;
	}


//	private static List<HomePageGoods> validListGoodsByGroup(String moduleId,String filedName,Map<String,Object> objMap) {
//		List<HomePageGoods> list = new ArrayList<>();
//		Object obj = objMap.get(filedName);
//		if(!StringUtils.isEmpty(obj)) {
//			Map<String,Object> groupMap = (Map<String, Object>) obj;
//			List<Map<String,Object>> listMap = (List<Map<String, Object>>) groupMap.get("listGoods");
//			for(Map<String,Object> tmpMap : listMap) {
//				HomePageGoods bean = initGoodsParam(moduleId);
//				
//				bean.setGoodsCode(validString("goodsCode",tmpMap,false));
//				bean.setId(validString("goodsId",tmpMap,false));
//				list.add(bean);
//			}
//		}
//		return list;
//	}
//	private static List<HomePageGoods> validListGoodsToGroups(String moduleId,String filedName,Map<String,Object> objMap) {
//		List<HomePageGoods> list = new ArrayList<>();
//		Object obj = objMap.get(filedName);
//		if(!StringUtils.isEmpty(obj)) {
//			List<Map<String,Object>> groups_Map = (List<Map<String,Object>>) obj;
//			for(Map<String,Object> arrMap : groups_Map) {
//				List<Map<String,Object>> listMap = (List<Map<String, Object>>) arrMap.get("listGoods");
//				for(Map<String,Object> tmpMap : listMap) {
//					HomePageGoods bean = initGoodsParam(moduleId);
//					
//					bean.setGoodsCode(validString("goodsCode",tmpMap,false));
//					bean.setId(validString("goodsId",tmpMap,false));
//					list.add(bean);
//				}
//			}
//		}
//		return list;
//	}

	private static List<HomePageGroupItem> validListGroupItem(String moduleId,String filedName,Map<String,Object> objMap) {
		List<HomePageGroupItem> list = new ArrayList<>();
		Object obj = objMap.get(filedName);
		if(!StringUtils.isEmpty(obj)) {
			String dtDow = DateUtils.now();
			String createAdmin = "adminApi-1";
			String itemId = System.currentTimeMillis()+"";
			
			//List<HomePageGroupItemDetail> listItemDetail = new ArrayList<>();
			Map<String,Object> groupMap =  (Map<String, Object>) obj;
			List<Map<String,Object>> listGroupMap = Arrays.asList(groupMap);
			for(Map<String,Object> arrMap : listGroupMap) {
				// groups Banner
				List<HomePageBanner> listBanner = new ArrayList<>();
				Map<String,Object> bannerMap = (Map<String, Object>) arrMap.get("banner");
				if( bannerMap == null) {
					bannerMap  = new HashMap<>();
				}
				List<Map<String,Object>> listBannerMap = Arrays.asList(bannerMap);
				if( listBannerMap == null) {
					listBannerMap  = new ArrayList<>();
				}
				for(Map<String,Object> tmpMap : listBannerMap) {
					HomePageBanner bean = initBannerParam(moduleId);
					bean.setImgTarget(validString("jumpTarget",tmpMap,true));
					bean.setImgTitle(validString("title",tmpMap,true));
					bean.setImgUrl(validString("imageLocation",tmpMap,false));
					bean.setId(validId("id",tmpMap,false));
					listBanner.add(bean);
				}
				
				// groups listGoods
				List<HomePageGoods> listGoods = new ArrayList<>();
				List<Map<String,Object>> listGoodsMap = (List<Map<String, Object>>) arrMap.get("listGoods");
				if( listGoodsMap == null) {
					listGoodsMap  = new ArrayList<>();
				}
				for(Map<String,Object> tmpMap : listGoodsMap) {
					HomePageGoods bean = initGoodsParam(moduleId);
					
					bean.setGoodsCode(validString("goodsCode",tmpMap,false));
					bean.setId(validString("goodsId",tmpMap,false));
					listGoods.add(bean);
				}
				
//				HomePageGroupItemDetail itemDetail = new HomePageGroupItemDetail();
//				itemDetail.setCreateAdmin(createAdmin);
//				itemDetail.setCreateTime(dtDow);
//				itemDetail.setUpdateAdmin(createAdmin);
//				itemDetail.setUpdateTime(dtDow);
//				itemDetail.setSortNum(1000);
//				itemDetail.setGroupItemId(itemId);
//				
//				itemDetail.setItemListBanner( listBanner);
//				itemDetail.setItemListGoods( listGoods );
//				itemDetail.setId(new ObjectId().toHexString());
//				listItemDetail.add(itemDetail);
				
				HomePageGroupItem item = new HomePageGroupItem();
				item.setCreateAdmin(createAdmin);
				item.setCreateTime(dtDow);
				item.setUpdateAdmin(createAdmin);
				item.setUpdateTime(dtDow);
				item.setModuleId(moduleId);
				item.setSortNum(1000);
				item.setItemListBanner( listBanner);
				item.setItemListGoods( listGoods );
				item.setId(new ObjectId().toHexString());
				list.add(item);
			}
			
		}
		return list;
	}
	private static List<HomePageGroupItem> validListGroupItemByGroups(String moduleId,String filedName,Map<String,Object> objMap) {
	List<HomePageGroupItem> list = new ArrayList<>();
	Object obj = objMap.get(filedName);
	if(!StringUtils.isEmpty(obj)) {
		String dtDow = DateUtils.now();
		String createAdmin = "adminApi-1";
		String itemId = System.currentTimeMillis()+"";
		
//		List<HomePageGroupItemDetail> listItemDetail = new ArrayList<>();
		List<Map<String,Object>> groups_Map = (List<Map<String,Object>>) obj;
		for(Map<String,Object> arrMap : groups_Map) {
			// groups Banner
			List<HomePageBanner> listBanner = new ArrayList<>();
			Map<String,Object> bannerMap = (Map<String, Object>) arrMap.get("banner");
			if( bannerMap == null) {
				bannerMap  = new HashMap<>();
			}
			List<Map<String,Object>> listBannerMap = Arrays.asList(bannerMap);
			if( listBannerMap == null) {
				listBannerMap  = new ArrayList<>();
			}
			for(Map<String,Object> tmpMap : listBannerMap) {
				HomePageBanner bean = initBannerParam(moduleId);
				bean.setImgTarget(validString("jumpTarget",tmpMap,true));
				bean.setImgTitle(validString("title",tmpMap,true));
				bean.setImgUrl(validString("imageLocation",tmpMap,false));
				bean.setId(validId("id",tmpMap,false));
				listBanner.add(bean);
			}
			
			// groups listGoods
			List<HomePageGoods> listGoods = new ArrayList<>();
			List<Map<String,Object>> listGoodsMap = (List<Map<String, Object>>) arrMap.get("listGoods");
			if( listGoodsMap == null) {
				listGoodsMap  = new ArrayList<>();
			}
			for(Map<String,Object> tmpMap : listGoodsMap) {
				HomePageGoods bean = initGoodsParam(moduleId);
				
				bean.setGoodsCode(validString("goodsCode",tmpMap,false));
				bean.setId(validString("goodsId",tmpMap,false));
				listGoods.add(bean);
			}
			
//			HomePageGroupItemDetail itemDetail = new HomePageGroupItemDetail();
//			itemDetail.setCreateAdmin(createAdmin);
//			itemDetail.setCreateTime(dtDow);
//			itemDetail.setUpdateAdmin(createAdmin);
//			itemDetail.setUpdateTime(dtDow);
//			itemDetail.setSortNum(1000);
//			itemDetail.setGroupItemId(itemId);
//			
//			itemDetail.setItemListBanner( listBanner);
//			itemDetail.setItemListGoods( listGoods );
//			itemDetail.setId(new ObjectId().toHexString());
//			listItemDetail.add(itemDetail);
			
			HomePageGroupItem item = new HomePageGroupItem();
			item.setCreateAdmin(createAdmin);
			item.setCreateTime(dtDow);
			item.setUpdateAdmin(createAdmin);
			item.setUpdateTime(dtDow);
			item.setModuleId(moduleId);
			item.setSortNum(1000);
			item.setItemListBanner( listBanner);
			item.setItemListGoods( listGoods );
			item.setId(new ObjectId().toHexString());
			list.add(item);
			
		}

	}
	return list;
}
	
	public static HomePageModule mapInit(Map<String,Object> tmp) {
		String dtDow = DateUtils.now();
		String createAdmin = "adminApi-1";
		
		HomePageModule model = new HomePageModule();
		model.setCreateTime(dtDow);
		model.setCreateAdmin(createAdmin);
		model.setUpdateTime(dtDow);
		model.setUpdateAdmin(createAdmin);
		
		model.setModuleName(validString("columnName",tmp,true));
		model.setShowModuleName(validBoolean("columnNameShow",tmp,false)?1:0);
		model.setSortNum(validInteger("sort",tmp,false));
		model.setShowState(validInteger("show",tmp,false));
		model.setShowloadMore(validBoolean("hasloadMore",tmp,false)?1:0);
		model.setLoadMoreTarget(validString("jumpTarget",tmp,true));
		model.setFollowingBlank(validInteger("followingBlank",tmp,true));
		
		String moduleId = validId("id",tmp,false);
		model.setId(new ObjectId().toHexString());
		//活动类型 {0,普通商品,1秒杀活动,2砍价活动,3预售活动,4品牌广场}
		model.setActityType(0);
		
		/*
		 * 样式1 (图片轮播区域)   数据内容为banner
		 * 样式2(优惠券) 数据内容为banner
		 * 样式3(广告图)  用于区分 优惠券  样式其实和优惠券一样   数据内容为banner
		 * 样式4 滑动栏  数据内容为商品
		 * 样式5  数据内容为banner
		 * 样式6 数据内容为banner
		 * 样式7 (品牌广场) 数据内容为banner  + 商品
		 * 样式8   秒杀(只有banner) 商品需要另外的接口返回
		 * 样式9   商品列表  (一般会有10个)
		 */
		int styleType  =  validInteger("celoveColumnStyle",tmp,false);
		model.setStyleType(styleType);
		
		if(styleType == 8  && "秒杀".equals(model.getModuleName()) ) { //1秒杀活动
			model.setActityType(1);
		}
		if(styleType == 7 && "品牌广场".equals(model.getModuleName()) ) { //4品牌广场
			model.setActityType(4);
		}
		if(styleType == 5 && "预售".equals(model.getModuleName()) ) { //3预售活动
			model.setActityType(3);
		}
		
		if(styleType == 1  || styleType == 2 
				|| styleType == 3 ) { //listBanner 数据
			model.setListBanner(validListBanner(moduleId,"listBanner",tmp));
		}else if(styleType == 4 || styleType == 9 ) { //listGoods 数据
			model.setListGoods(validListGoods(moduleId,"listGoods",tmp));
		}else if(styleType == 5 || styleType == 6 ) { //listCeloveColumn 数据
			model.setListBanner(validListBannerByCeloveColumn(moduleId,"listCeloveColumn",tmp));
			model.setModuleAttached(validModuleAttached(moduleId,"listCeloveColumn",tmp));
		}else if(styleType == 7) { //品牌广场 groups 数据
			model.setListGroupItem(validListGroupItemByGroups(moduleId,"groups",tmp));
		}else if(styleType == 8) { //秒杀 group 数据
			model.setListGroupItem(validListGroupItem(moduleId,"group",tmp));
		}
		
		log.debug("---------------"+JsonUtils.toDefaultJson(model));
		
		return model;
	}
	
	
	@Test
	public void testInit() {
		log.info("=========================");
		//http://www.biquge.com.tw/8_8950/4446792.html
		String url = "http://192.168.1.191/localQuickPurchase/homePageIntegrationAction/homePageV3";
		try {
			//清空 首页模块数据
			homePageModuleMongo.remove(new Query(), HomePageModule.class);
			
			String resultJson = HttpClientUtil.doGet(url);
			Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
			
			log.debug(resultMap.keySet().toString());
			
			Map<String,Object> dataMap = (Map<String, Object>) resultMap.get("data");
			log.debug(dataMap.keySet().toString());
			
			List<Map<String,Object>> listMap =  (List<Map<String, Object>>) dataMap.get("homePageColumn");
			
			for(Map<String,Object> tmp : listMap ) {
				HomePageModule entity = mapInit(tmp);
				homePageModuleMongo.insert(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
		log.info("=========================");
	}
	

	@Test
	public void testUpdate() {
		log.info("=========================");
		Query query = Query.query(Criteria.where("listBanner.createAdmin").is("litiangui"));
        Update update = new Update().set("listBanner.$.moduleId", "1111111111").set("listBanner.$._id", "2asfaf222222");
        homePageModuleMongo.updateFirst(query, update, HomePageModule.class);
        log.info("=========================");
	}
	
	
	@Test
	public void testSave() {
		String dtDow = DateUtils.now();
		String createAdmin = "admin-0";
		
		HomePageModule model = new HomePageModule();
		model.setCreateTime(dtDow);
		model.setCreateAdmin(createAdmin);
		model.setUpdateTime(dtDow);
		model.setUpdateAdmin(createAdmin);
		model.setModuleName("首页模块"+dtDow);
		model.setShowModuleName(1);
		model.setSortNum(1);
		model.setShowState(1);
		model.setShowloadMore(1);
		//model.setLoadMoreTarget("url"+dtDow);
		model.setActityType(1);
//		model.setListBanner(new ArrayList<HomePageBanner>( Arrays.asList(
//				initBanner(dtDow)
//				,initBanner(dtDow)
//				) ) ); 
//		model.setListGoods(new ArrayList<HomePageGoods>( Arrays.asList(
//				initGoods(dtDow)
//				)  ) ); 
//		model.setListGroupItem(new ArrayList<HomePageGroupItem>( Arrays.asList(
//				initGroupItem(dtDow)
//				)  ) ); 
		
		log.debug("HomePageModule====="+JsonUtils.toDefaultJson(model));
		
		homePageModuleMongo.insert(model);
		
		log.info("=========================");
	}


	private HomePageBanner initBanner(String moduleId) {
		String dtDow = DateUtils.now();
		String createAdmin = "admin-1";
		HomePageBanner bean = new HomePageBanner();
		bean.setCreateAdmin(createAdmin);
		bean.setCreateTime(dtDow);
		bean.setUpdateAdmin(createAdmin);
		bean.setUpdateTime(dtDow);
		bean.setModuleId(moduleId);
		bean.setSortNum(1000);
		
		String str = "aaaaaaaaaaaaaaaaaaaaaaa"+dtDow;
		bean.setImgTarget(str);
		bean.setImgTitle(str);
		bean.setImgUrl(str);
		return bean;
	}


	private HomePageGoods initGoods(String moduleId) {
		String dtDow = DateUtils.now();
		String createAdmin = "admin-2";
		HomePageGoods bean = new HomePageGoods();
		bean.setCreateAdmin(createAdmin);
		bean.setCreateTime(dtDow);
		bean.setUpdateAdmin(createAdmin);
		bean.setUpdateTime(dtDow);
		bean.setModuleId(moduleId);
		bean.setSortNum(1000);
		
		bean.setGoodsCode(dtDow);
		return bean;
	}


	private HomePageGroupItem initGroupItem(String moduleId) {
		String dtDow = DateUtils.now();
		String createAdmin = "admin-3";
		
		HomePageGroupItem bean = new HomePageGroupItem();
		bean.setCreateAdmin(createAdmin);
		bean.setCreateTime(dtDow);
		bean.setUpdateAdmin(createAdmin);
		bean.setUpdateTime(dtDow);
		bean.setModuleId(moduleId);
		bean.setSortNum(1000);
		
		bean.setItemListBanner(Arrays.asList(initBanner(dtDow)));
		bean.setItemListGoods(Arrays.asList(initGoods(dtDow)));
		
		return bean;
	}
	
	
	

}