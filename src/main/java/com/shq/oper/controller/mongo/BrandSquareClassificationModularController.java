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
import com.shq.oper.dao.MongoDao;
import com.shq.oper.dao.mongo.HomePageModuleAttachedMongo;
import com.shq.oper.dao.mongo.HomePageModuleMongo;
import com.shq.oper.dao.mongo.SelectionGoodsClassMongo;
import com.shq.oper.enums.BrandSquareModularTypeEnum;
import com.shq.oper.enums.HomePageModuleAvtiveEnum;
import com.shq.oper.enums.HomePageModuleStyleTypeEnum;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.HomePageBanner;
import com.shq.oper.model.domain.mongo.HomePageGoods;
import com.shq.oper.model.domain.mongo.HomePageGroupItem;
import com.shq.oper.model.domain.mongo.HomePageModule;
import com.shq.oper.model.domain.mongo.HomePageModuleAttached;
import com.shq.oper.model.domain.mongo.SelectionGoodsClass;
import com.shq.oper.model.domain.mongo.brand.BrandSquareModular;
import com.shq.oper.model.domain.primarydb.ActivityGoodsRule;
import com.shq.oper.model.domain.primarydb.ActivityGoodsRuleDetails;
import com.shq.oper.model.domain.primarydb.Banner;
import com.shq.oper.model.domain.primarydb.Dict;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.BaseResponseResultDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.impl.primarydb.AdminServiceImpl;
import com.shq.oper.service.primarydb.ActivityGoodsRuleDetailsService;
import com.shq.oper.service.primarydb.ActivityGoodsRuleService;
import com.shq.oper.service.primarydb.BannerService;
import com.shq.oper.service.primarydb.DictService;
import com.shq.oper.service.primarydb.HomePageModuleService;
import com.shq.oper.service.primarydb.ProductService;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.Constant;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;

import groovyjarjarcommonscli.ParseException;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ljz
 *
 */
@RestController
@Slf4j
@RequestMapping("/mongo/brandsquareclassificationmodular")
public class BrandSquareClassificationModularController extends BaseController {

	@Autowired
	private HomePageModuleMongo mongoDao;

	@Autowired
	private DistributionProductMapper distributionProductMapper;

	@Autowired
	private MongoDao<BrandSquareModular> BrandSquareModularMongo;

	@Autowired
	private ShqApi ShqApi;

	@Autowired
	SystemProperties systemProperties;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		Map<String, Object> brandSquareModularTypeEnumMap = BrandSquareModularTypeEnum.getObjectMap();
		request.setAttribute("brandSquareModularTypeEnumMap", brandSquareModularTypeEnumMap);

		return toPage(request);
	}

	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, BrandSquareModular brandSquareModular) {
		if(StringUtils.isEmpty(brandSquareModular.getColumnType())) {
			return Msg.error("模块类型不能为空");
		}
		if(StringUtils.isEmpty(brandSquareModular.getModularName())) {
			return Msg.error("品牌店铺名称不能为空");
		}
		if(StringUtils.isEmpty(brandSquareModular.getAssociatedShopSeq())) {
			return Msg.error("品牌店铺seq不能为空");
		}
		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/brandSquareShopAction/addOrUpdateModular";
		if (StringUtils.isEmpty(brandSquareModular.getId())) {//新增
			brandSquareModular.setId(null);
		}
		String resultJson = HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(brandSquareModular));
		if(StringUtils.isEmpty(resultJson)) {
			return Msg.error("调用接口出错");
		}
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		String object = resultMap.get("code")+"";
		if(object.equals("200")) {
			return Msg.ok("保存成功");
		}else {
			return Msg.error(resultMap.get("message")+"");
		}
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<BrandSquareModular> list(BrandSquareModular entity, String timeRange, PageDto page) throws ParseException {

		Criteria cr=new Criteria();
		Criteria c1=Criteria.where("columnType").is(6);
		Query query = new Query(cr.orOperator(c1));
		// 升序排序
		query.with(new Sort(new Order(Direction.ASC, "sortNum")));
		if (!StringUtils.isEmpty(entity.getModularName())) {
			query.addCriteria(Criteria.where("modularName").is(entity.getModularName()));
		}
		if (!StringUtils.isEmpty(entity.getModularStatus())) {
			query.addCriteria(Criteria.where("modularStatus").is(entity.getModularStatus()));
		}
		PageInfo<BrandSquareModular> entitys = BrandSquareModularMongo.findByQueryPage(query, page, entity);
		return Data.ok(entitys);
	}

	@RequestMapping("/details")
	public ModelAndView details(HttpServletRequest request, BrandSquareModular entity) throws Exception {
		BrandSquareModular brandSquareModular = BrandSquareModularMongo.findById(entity.getId(), BrandSquareModular.class);
		request.setAttribute("brandSquareModular", brandSquareModular);
		log.debug("===============" + JsonUtils.toDefaultJson(brandSquareModular));
		return toPage(request);
	}

	/**
	 * 模块的启用显示与禁用显示
	 *
	 * @param request
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateModularStatus")
	public Msg<String> updateShowModuleName(HttpServletRequest request, BrandSquareModular entity) throws Exception {
		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/brandSquareShopAction/addOrUpdateModular";
		String resultJson = HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(entity));
		if(StringUtils.isEmpty(resultJson)) {
			return Msg.error("调用接口出错");
		}
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		String object = resultMap.get("code")+"";
		if(object.equals("200")) {
			return Msg.ok("操作成功");
		}else {
			return Msg.error(resultMap.get("message")+"");
		}
	}
	@RequestMapping("/delete")
	public Msg<String> enableById(HttpServletRequest request, String id) {
		if(StringUtils.isEmpty(id)) {
			return Msg.error("数据有误,请稍后重试");
		}
		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/brandSquareShopAction/deleteOrUpdateModular?brandSquareModularId="+id;
		String resultJson = HttpClientUtil.doGet(url);
		if(StringUtils.isEmpty(resultJson)) {
			return Msg.error("接口调用错误");
		}
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		String object = resultMap.get("code")+"";
		if(object.equals("200")) {
			return Msg.ok("删除成功");
		}else {
			return Msg.error(resultMap.get("message")+"");
		}
	}

}