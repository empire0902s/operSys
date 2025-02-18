package com.shq.oper.controller.mongo;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.shq.oper.model.domain.primarydb.Dict;
import com.shq.oper.model.dto.api.BaseResponseResultDto;
import com.shq.oper.service.primarydb.*;
import com.shq.oper.util.HttpClientUtil;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.mongo.HomePageModuleAttachedMongo;
import com.shq.oper.dao.mongo.HomePageModuleMongo;
import com.shq.oper.dao.mongo.SelectionGoodsClassMongo;
import com.shq.oper.enums.HomePageModuleAvtiveEnum;
import com.shq.oper.enums.HomePageModuleStyleTypeEnum;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.HomePageBanner;
import com.shq.oper.model.domain.mongo.HomePageGoods;
import com.shq.oper.model.domain.mongo.HomePageGroupItem;
import com.shq.oper.model.domain.mongo.HomePageModule;
import com.shq.oper.model.domain.mongo.HomePageModuleAttached;
import com.shq.oper.model.domain.mongo.SelectionGoodsClass;
import com.shq.oper.model.domain.primarydb.ActivityGoodsRule;
import com.shq.oper.model.domain.primarydb.ActivityGoodsRuleDetails;
import com.shq.oper.model.domain.primarydb.Banner;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.impl.primarydb.AdminServiceImpl;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.Constant;
import com.shq.oper.util.JsonUtils;
import com.shq.oper.util.DateUtils;

import groovyjarjarcommonscli.ParseException;
import lombok.extern.slf4j.Slf4j;

/**
 * 爱之家首页模块
 * 
 * @author linagjinzhao
 */
/**
 */
/**
 * 
 * 项目名称：operSys 类名称：HomePageModuleController 类描述： 创建人：ljz 创建时间：2018年8月21日
 * 下午2:40:31 修改人：ljz 修改时间：2018年8月21日 下午2:40:31 修改备注：
 * 
 * @version
 * 
 */
@RestController
@Slf4j
@RequestMapping("/mongo/homepagemodule")
public class HomePageModuleController extends BaseController {

	@Autowired
	private HomePageModuleMongo mongoDao;
	
	@Autowired
	private SelectionGoodsClassMongo selectionGoodsClassMongo;
	
	@Autowired
	private HomePageModuleAttachedMongo HomePageModuleAttachedDao;

	@Autowired
	private HomePageModuleService homePageModuleService;

	@Autowired
	private ActivityGoodsRuleService activityGoodsRuleService;

	@Autowired
	private ActivityGoodsRuleDetailsService activityGoodsRuleDetailsService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private BannerService bannerService;
	
	@Autowired
	private DistributionProductMapper distributionProductMapper;

	@Autowired
	private DictService dictService;

	@Autowired
	SystemProperties systemProperties;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		Map<String, Object> homePageModuleAvtiveEnumMap = HomePageModuleAvtiveEnum.getObjectMap();
		Map<String, Object> homePageModuleStyleTypeEnumMap = HomePageModuleStyleTypeEnum.getObjectMap();
		request.setAttribute("homePageModuleAvtiveEnumMap", homePageModuleAvtiveEnumMap);
		request.setAttribute("homePageModuleStyleTypeEnumMap", homePageModuleStyleTypeEnumMap);
		
		return toPage(request);
	}
	@RequestMapping("/appAdv")
	public ModelAndView appAdvPage(HttpServletRequest request) {
		Map<String, Object> homePageModuleAvtiveEnumMap = HomePageModuleAvtiveEnum.getObjectMap();
		HttpSession session = request.getSession();
		Object adminDto = session.getAttribute(Constant.OPER_USER);
		request.setAttribute("homePageModuleAvtiveEnumMap", homePageModuleAvtiveEnumMap);
		request.setAttribute("adminDto", adminDto);
		return toPage(request);
	}
	
	@RequestMapping(value = "/activity/goods/rule/saveDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public Msg<String> saveDetails(HttpServletRequest request, @RequestParam long ruleId, String code,Integer sort) {
		if (StringUtils.isEmpty(code)) {
			return Msg.error("code值不能为空");
		}
		LocalDateTime dtNow = LocalDateTime.now();
		try {
			String adminStr = this.getAdmin(request).getName() + "-" + this.getAdmin(request).getId();
			return activityGoodsRuleDetailsService.insertinvitationPageDetails(adminStr, ruleId, code, dtNow,sort);
		} catch (Exception e) {
			log.error("规则明细保存失败", e);
			return Msg.ok("规则明细保存失败");
		}
	}

	@RequestMapping("/activity/goods/rule/details")
	public ModelAndView activityGoodsRuleIndex(HttpServletRequest request) {

		Dict dict=new Dict();
		dict.setCode("homeOfLove");
		List<Dict> dictList = dictService.select(dict);
		
		ActivityGoodsRule resultActivityGoodsRule = new ActivityGoodsRule();
		ActivityGoodsRule tmp = new ActivityGoodsRule();
		tmp.setFromSys("homeOfLove");
		tmp.setType(0);
		List<ActivityGoodsRule> activityGoodsRuleList = activityGoodsRuleService.select(tmp);
		request.setAttribute("activityGoodsRule", activityGoodsRuleList);
		request.setAttribute("dictList", dictList);
		return toPage(request);
	}

	@RequestMapping("/activity/goods/rule/detailsSetting")
	public ModelAndView detailsSetting(HttpServletRequest request,Long operType,ActivityGoodsRuleDetails entity) {
		
		ActivityGoodsRuleDetails activityGoodsRuleDetailsResult =new ActivityGoodsRuleDetails();
		DistributionProduct distributionProductResult=new DistributionProduct();
		if(operType==2L) {//查看
			activityGoodsRuleDetailsResult = activityGoodsRuleDetailsService.selectOne(entity);
			if (!StringUtils.isEmpty(activityGoodsRuleDetailsResult.getRefSignValue())) {
				DistributionProduct temp=new DistributionProduct();
				temp.setProductCode(activityGoodsRuleDetailsResult.getRefSignValue());
				List<DistributionProduct> select = distributionProductMapper.select(temp);
				if(select.size()>0) {
					distributionProductResult=select.get(0);
					distributionProductResult.setProductDetails("");
				}
			}
		}
		ActivityGoodsRule tmp=new ActivityGoodsRule();
		tmp.setFromSys("homeOfLove");
		List<ActivityGoodsRule> activityGoodsRule = activityGoodsRuleService.select(tmp);
		request.setAttribute("activityGoodsRule",activityGoodsRule);
		request.setAttribute("activityGoodsRuleDetailsResult",activityGoodsRuleDetailsResult);
		request.setAttribute("distributionProductResult",distributionProductResult);
		request.setAttribute("operType",operType);
		return toPage(request);
	}

	/**
	 * 根据code查询商品
	 * 
	 * @param request
	 * @param code
	 * @return
	 */
	@RequestMapping("activity/goods/rule/searchGoods")
	public Data<DistributionProduct> searchGoods(HttpServletRequest request, String code) {

		if (StringUtils.isEmpty(code))
			return Data.error("商品Code不能为空");
		return productService.searchGoods(code);

	}

	/**
	 * 删除已绑定的商品
	 * 
	 * @param detailId
	 * @return
	 */
	@RequestMapping(value = "activity/goods/rule/delete", method = { RequestMethod.GET, RequestMethod.POST })
	public Msg<String> delete(long detailId) {
		activityGoodsRuleDetailsService.deleteByPk(detailId);
		SpringContextHolder.getBean(AdminServiceImpl.class)
				.clearCacheByConstantKey(CacheKeyConstant.BASE_GOODS_RULE_QUERY);
		return Msg.ok("删除成功");
	}

	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, HomePageModule homePageModule) {

		homePageModule.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		Msg<String> result = homePageModuleService.save(homePageModule);
		return result;
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<HomePageModule> list(HomePageModule entity, String timeRange, PageDto page) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Query query = new Query();
		// 升序排序
		query.with(new Sort(new Order(Direction.ASC, "sortNum")));
		if (!StringUtils.isEmpty(entity.getModuleName())) {
			query.addCriteria(Criteria.where("moduleName").is(entity.getModuleName()));
		}
		if (!StringUtils.isEmpty(entity.getShowState())) {
			query.addCriteria(Criteria.where("showState").is(entity.getShowState()));
		}
		if (!StringUtils.isEmpty(entity.getShowloadMore())) {
			query.addCriteria(Criteria.where("showloadMore").is(entity.getShowloadMore()));
		}
		PageInfo<HomePageModule> entitys = mongoDao.findByQueryPage(query, page, entity);
		return Data.ok(entitys);
	}
	@RequestMapping(value = "/listModule", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<HomePageModule> listModule(HomePageModule entity, String timeRange, PageDto page) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Query query = new Query();
		// 升序排序
		query.with(new Sort(new Order(Direction.ASC, "sortNum")));
		if (!StringUtils.isEmpty(entity.getShowState())) {
			query.addCriteria(Criteria.where("showState").is(entity.getShowState()));
		}
		PageInfo<HomePageModule> entitys = mongoDao.findByQueryPage(query, page, entity);
		return Data.ok(entitys);
	}

	@RequestMapping("/details")
	public ModelAndView details(HttpServletRequest request, HomePageModule entity) throws Exception {
		HomePageModule homePageModule = mongoDao.findById(entity.getId(), HomePageModule.class);
		request.setAttribute("homePageModuleResult", homePageModule);
		log.debug("===============" + JsonUtils.toDefaultJson(homePageModule));
		return toPage(request);
	}

	/**
	 * 模块名称的启用显示与禁用显示
	 * 
	 * @param request
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateShowModuleName")
	public Msg<String> updateShowModuleName(HttpServletRequest request, HomePageModule entity) throws Exception {
		HomePageModule homePageModule = mongoDao.findById(entity.getId(), HomePageModule.class);
		homePageModule.setShowModuleName(entity.getShowModuleName());
		homePageModule.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		homePageModule.setUpdateTime(LocalDateTime.now().toString());
		mongoDao.update(homePageModule);
		return Msg.ok("操作成功");
	}

	/**
	 * 展示状态的启用显示与禁用显示
	 * 
	 * @param request
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateShowState")
	public Msg<String> updateShowState(HttpServletRequest request, HomePageModule entity) throws Exception {
		HomePageModule homePageModule = mongoDao.findById(entity.getId(), HomePageModule.class);
		homePageModule.setShowState(entity.getShowState());
		homePageModule.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		homePageModule.setUpdateTime(LocalDateTime.now().toString());
		mongoDao.update(homePageModule);
		return Msg.ok("操作成功");
	}

	/**
	 * 加载更多的启用显示与禁用显示
	 * 
	 * @param request
	 * @param entity
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/updateShowloadMore")
	public Msg<String> updateShowloadMore(HttpServletRequest request, HomePageModule entity) throws Exception {
		HomePageModule homePageModule = mongoDao.findById(entity.getId(), HomePageModule.class);
		homePageModule.setShowloadMore(entity.getShowloadMore());
		homePageModule.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		homePageModule.setUpdateTime(LocalDateTime.now().toString());
		mongoDao.update(homePageModule);
		return Msg.ok("操作成功");
	}
	
	
	@RequestMapping("/homePageModuleAttachedDetailsSetting")
	public ModelAndView homePageModuleAttachedDetailsSetting(HttpServletRequest request, HomePageModule homePageModule) {
		
		HomePageModuleAttached homePageModuleAttached=new HomePageModuleAttached();
		HomePageModule findById = mongoDao.findById(homePageModule.getId(), HomePageModule.class);
		homePageModuleAttached=findById.getModuleAttached();
		/*HomePageModuleAttached homePageModuleAttached=new HomePageModuleAttached();
		HomePageModuleAttached entity = new HomePageModuleAttached();
		entity.setModuleId(homePageModule.getId());
		PageInfo<HomePageModuleAttached> findByEntity = HomePageModuleAttachedDao.findByEntity(entity, new PageDto());
		if(findByEntity.getList().size()>0) {
			homePageModuleAttached = findByEntity.getList().get(0);
		}*/
		request.setAttribute("homePageModule", homePageModule);
		request.setAttribute("homePageModuleAttached", homePageModuleAttached);
		return toPage(request);
	}
	
	/**
	 * app首页广告的详情
	 * @param request
	 * @param banner
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/appAdvDetails")
	public ModelAndView appAdvDetails(HttpServletRequest request, Banner banner) throws Exception {
		Banner bannerDetails = bannerService.selectOne(banner);
		request.setAttribute("bannerDetails", bannerDetails);
		return toPage(request);
	}
	
	
	@RequestMapping(value = "/activeRuledetailsList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<ActivityGoodsRuleDetails> activeRuledetailsList(ActivityGoodsRuleDetails activityGoodsRuleDetails, PageDto page) {
		com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
		Page<ActivityGoodsRuleDetails> entitys = activityGoodsRuleDetailsService.queryLoveOfHomeInvitationPageDataList(activityGoodsRuleDetails);
		Map<String,DistributionProduct>map=new HashMap<String, DistributionProduct>();
		List<String>codeList=new ArrayList<>();
		if(entitys.getResult().size()>0) {
			for (ActivityGoodsRuleDetails temp : entitys.getResult()) {
				codeList.add(temp.getRefSignValue());
			}
			Page<DistributionProduct> queryDiatributionProductByCodeList = distributionProductMapper.queryDiatributionProductByCodeList(codeList);
			if(queryDiatributionProductByCodeList.getResult().size()>0) {
				for (DistributionProduct temp : queryDiatributionProductByCodeList.getResult()) {
				map.put(temp.getProductCode(), temp);
				}
			}
			for (ActivityGoodsRuleDetails temp : entitys.getResult()) {
				if(!StringUtils.isEmpty(map.get(temp.getRefSignValue()))) {
					if(!StringUtils.isEmpty(map.get(temp.getRefSignValue()).getIsSale())) {
						temp.setIsSale(map.get(temp.getRefSignValue()).getIsSale().toString());
					}
				}
			}
		}
		return Data.ok(entitys);
	}

	/**
	 *@author ltg
	 *@date 2018/9/4 14:34
	 * @params:[]
	 * @return: com.shq.oper.model.dto.Msg<java.lang.String>
	 */
	@RequestMapping("/clearCache")
	public Msg<String> clearCahe(){

		try{
			Dict dictParam=new Dict();
			//字典code写死
			dictParam.setCode("systemUrl");
			dictParam.setDictKey("clearCache");
			Dict dict=dictService.selectOne(dictParam);
			if (null==dict){
				return Msg.error("查询不到字典");
			}
			String str=HttpClientUtil.doGetUrl(dict.getDictValue());
			BaseResponseResultDto resultDto=JsonUtils.parse(str,BaseResponseResultDto.class);
			if ("200".equals(resultDto.getCode())){
				return Msg.ok("清除缓存完成");
			}else {
				return Msg.error("清除缓存失败");
			}
		}catch (Exception e){
			return Msg.error("内部错误");
		}

	}
	
	@RequestMapping("/initIndexPageData")
	public Msg<String> initIndexPageData(){

		try{
			Dict dictParam=new Dict();
			//字典code写死systemUrl
			dictParam.setCode("systemUrl");
			dictParam.setDictKey("initIndexPageData");
			Dict dict=dictService.selectOne(dictParam);
			if (null==dict){
				return Msg.error("查询不到字典");
			}
			String url = dict.getDictValue();
//			url = "http://nfxts.520shq.com//localQuickPurchase/homePageIntegrationAction/homePageV3";
			if(StringUtils.isEmpty(url) || !url.contains("homePageIntegrationAction") ) {
				return Msg.error("初始化链接有误["+url+"]");
			}
			
			String resultJson = HttpClientUtil.doGet(url);
			Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
			if(resultMap == null) {
				return Msg.error("初始化数据为空.");
			}
			Map<String,Object> dataMap = (Map<String, Object>) resultMap.get("data");
			if(dataMap == null) {
				return Msg.error("初始化数据Data为空.");
			}
			List<Map<String,Object>> listMap =  (List<Map<String, Object>>) dataMap.get("homePageColumn");
			if(listMap == null || listMap.size()<=0) {
				return Msg.error("初始化数据homePageColumn有误");
			}
			//清空 首页模块数据
			mongoDao.remove(new Query(), HomePageModule.class);
			for(Map<String,Object> tmp : listMap ) {
				HomePageModule entity = mapInit(tmp);
				mongoDao.insert(entity);
			}
			return Msg.ok();
		}catch (Exception e){
			return Msg.error("内部错误");
		}

	}
	
	
	
	private  HomePageModule mapInit(Map<String,Object> tmp) {
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
		 * 样式9   商品列表 
		 */
		int styleType  =  validInteger("celoveColumnStyle",tmp,false);
		model.setStyleType(styleType);
		if(styleType ==Integer.parseInt(HomePageModuleStyleTypeEnum.SECKILL.getCode())  && "秒杀".equals(model.getModuleName()) ) { //1秒杀活动
			model.setActityType(1);
		}
		if(styleType == Integer.parseInt(HomePageModuleStyleTypeEnum.BRAND_SQUARE.getCode()) && "品牌广场".equals(model.getModuleName()) ) { //4品牌广场
			model.setActityType(4);
		}
		if(styleType == Integer.parseInt(HomePageModuleStyleTypeEnum.LOGO_BANNER.getCode()) && "预售".equals(model.getModuleName()) ) { //3预售活动
			model.setActityType(3);
		}
		
//		if(styleType == 1  || styleType == 2
//				|| styleType == 3 ) { //listBanner 数据
//			model.setListBanner(validListBanner(moduleId,"listBanner",tmp));
//		}else if(styleType == 4 || styleType == 9 ) { //listGoods 数据
//			model.setListGoods(validListGoods(moduleId,"listGoods",tmp));
//		}else if(styleType == 5 || styleType == 6 ) { //listCeloveColumn 数据
//			model.setListBanner(validListBannerByCeloveColumn(moduleId,"listCeloveColumn",tmp));
//			model.setModuleAttached(validModuleAttached(moduleId,"listCeloveColumn",tmp));
//		}else if(styleType == 7) { //品牌广场 groups 数据
//			model.setListGroupItem(validListGroupItemByGroups(moduleId,"groups",tmp));
//		}else if(styleType == 8) { //秒杀 group 数据
//			model.setListGroupItem(validListGroupItem(moduleId,"group",tmp));
//		}
		
		
		model.setListBanner(validListBanner(moduleId,"listBanner",tmp));
		model.setListGoods(validListGoods(moduleId,"listGoods",tmp));
		if(styleType == 5 || styleType == 6 ) {
			model.setListBanner(validListBannerByCeloveColumn(moduleId, "listCeloveColumn", tmp));
			model.setModuleAttached(validModuleAttached(moduleId,"listCeloveColumn",tmp));
		}

		
		if(styleType == Integer.parseInt(HomePageModuleStyleTypeEnum.BRAND_SQUARE.getCode())) { //品牌广场 groups 数据
			model.setListGroupItem(validListGroupItemByGroups(moduleId,"groups",tmp));
		}
		if(styleType == Integer.parseInt(HomePageModuleStyleTypeEnum.SECKILL.getCode())) { //秒杀 group 数据
			model.setListGroupItem(validListGroupItem(moduleId,"group",tmp));
		}
		
		return model;
	}
	
	private  Boolean validBoolean(String filedName,Map<String,Object> objMap,boolean isAllowNull) {
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
	private  Integer validInteger(String filedName,Map<String,Object> objMap,boolean isAllowNull) {
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
	private  String validString(String filedName,Map<String,Object> objMap,boolean isAllowNull) {
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
	private  String validId(String filedName,Map<String,Object> objMap,boolean isAllowNull) {
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
	
	private  List<HomePageBanner> validListBanner(String moduleId,String filedName,Map<String,Object> objMap) {
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
	private  List<HomePageBanner> validListBannerByCeloveColumn(String moduleId,String filedName,Map<String,Object> objMap) {
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
	private  HomePageModuleAttached validModuleAttached(String moduleId,String filedName,Map<String,Object> objMap) {
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
	
	private  List<HomePageGoods> validListGoods(String moduleId,String filedName,Map<String,Object> objMap) {
		List<HomePageGoods> list = new ArrayList<>();
		Object obj = objMap.get(filedName);
		if(!StringUtils.isEmpty(obj)) {
			List<Map<String,Object>> listMap = (List<Map<String, Object>>) obj;
			Query query = new Query();
			query.addCriteria(Criteria.where("appGroup").is(Integer.valueOf(moduleId)));
			query.addCriteria(Criteria.where("state").is(0));
			List<SelectionGoodsClass> listEntitys = selectionGoodsClassMongo.findByCollectionNameAll(query,SelectionGoodsClass.class,"selectionGoodsClass");
			for(SelectionGoodsClass tmp : listEntitys) {
				HomePageGoods bean = initGoodsParam(moduleId);
				bean.setSortNum(tmp.getSort());
				bean.setGoodsCode(tmp.getProductCode());
				bean.setId(tmp.getId());
				list.add(bean);
			}
		}
		return list;
	}
	
	private  List<HomePageGroupItem> validListGroupItem(String moduleId,String filedName,Map<String,Object> objMap) {
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
	private  List<HomePageGroupItem> validListGroupItemByGroups(String moduleId,String filedName,Map<String,Object> objMap) {
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
	
	private  HomePageBanner initBannerParam(String moduleId) {
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
	
	private  HomePageGoods initGoodsParam(String moduleId) {
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
	

}