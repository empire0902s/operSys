package com.shq.oper.controller.mongo;

import java.time.LocalDateTime;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.Page;
import com.mongodb.BasicDBObject;
import com.shq.oper.dao.mongo.BrandSquareBannerMongo;
import com.shq.oper.dao.mongo.GoodsMongo;
import com.shq.oper.enums.ProductTypeEnum;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.*;
import com.shq.oper.model.domain.mongo.brand.BrandSquareBanner;
import com.shq.oper.model.domain.mongo.brand.HotSaleGoods;
import com.shq.oper.model.domain.mongo.channel.Goods;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.*;
import com.shq.oper.model.dto.api.brandsquare.HotSaleGoodsDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.HotSaleGoodsService;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.mongo.HomePageModuleMongo;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author ljz
 *
 */
@RestController
@Slf4j
@RequestMapping("/mongo/brandsquaresetting")
public class BrandSquareSettingController  extends BaseController {

    @Autowired
    private DistributionProductMapper distributionProductMapper;

    @Autowired
    private HomePageModuleMongo homePageModuleMongo;
    
	@Autowired
	private HotSaleGoodsService hotSaleGoodsService;

    @Autowired
    private GoodsMongo goodsMongo;
    
	
	@Autowired
	private BrandSquareBannerMongo mongoDao;
    
	@Autowired
	private ShqApi ShqApi;


    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/mainindex")
    public ModelAndView mainindex(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/goods")
    public ModelAndView goods(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/groupitem")
    public ModelAndView groupitem(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/addgood")
    public ModelAndView addgoods(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/vipgoods")
    public ModelAndView vipgoods(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/vipandgoods")
    public ModelAndView vipandgoods(HttpServletRequest request) {
        return toPage(request);
    }


    /**
     * 获取模块所有Banner数据
     * @param entity
     * @param page
     * @return
     */
    @RequestMapping("/bannerList")
    public Data<BrandSquareBanner> bannerList(BrandSquareBanner entity, PageDto page){
    	
		Query query = new Query();
		// 升序排序
		query.with(new Sort(new Order(Direction.ASC, "sortNum")));
		if (!StringUtils.isEmpty(entity.getColumnId())) {
			query.addCriteria(Criteria.where("columnId").is(entity.getColumnId()));
		}
		if (!StringUtils.isEmpty(entity.getTitle())) {
			query.addCriteria(Criteria.where("title").regex(".*?" +entity.getTitle()+ ".*"));
		}
		if (!StringUtils.isEmpty(entity.getIdentifyType())) {
			query.addCriteria(Criteria.where("identifyType").is(entity.getIdentifyType()));
		}
		PageInfo<BrandSquareBanner> entitys = mongoDao.findByQueryPage(query, page, entity);
		if(!StringUtils.isEmpty(entitys.getList())&&entitys.getList().size()>0) {
			for (BrandSquareBanner brandSquareBanner : entitys.getList()) {
				brandSquareBanner.setImageLocation(ShqApi.getImageAddrUrl()+brandSquareBanner.getImageLocation());
			}
		}
		return Data.ok(entitys);
    }


    /**	
     * 品牌广场  商品列表
     * @param entity
     * @param pageDto
     * @return
     */
    @RequestMapping("/goodsList")
	public Data<HotSaleGoodsDto> list(HotSaleGoods entity,String productName, PageDto page) {
		
		PageInfo<HotSaleGoodsDto> entitys=new PageInfo<>();
		if(StringUtils.isEmpty(entity.getColumnId())) {
			return Data.ok(new 	PageInfo<HotSaleGoodsDto>());
		}
		try {
			entitys=hotSaleGoodsService.queryHotSaleGoodsList(entity,productName,page);
			return Data.ok(entitys);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("热卖商品error", e.getMessage());
			return Data.ok(new 	PageInfo<HotSaleGoodsDto>());
		}
	}

    /**
     *@author ltg
     *@date 2018/8/20 18:37
     * @params:[entity, request]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/addBrandSquareBanner")
    public Msg<String> addBrandSquareBanner(BrandSquareBanner brandSquareBanner,HttpServletRequest request){

		if(StringUtils.isEmpty(brandSquareBanner.getIdentifyType())) {
			return Msg.error("识别标识(identifyType)不能为空");
		}
		if(StringUtils.isEmpty(brandSquareBanner.getImageLocation())) {
			return Msg.error("图片链接不能为空");
		}
		if(StringUtils.isEmpty(brandSquareBanner.getSortNum())) {
			return Msg.error("排序不能为空");
		}
		if(StringUtils.isEmpty(brandSquareBanner.getColumnId())) {
			return Msg.error("模块Id不能为空");
		}
		
		String url = ShqApi.getGetBannerUrl()+"localQuickPurchase/brandSquareAction/addOrUpdateBrandSquareBanner";
		Map<String, Object> params=new HashMap<>();
		if (StringUtils.isEmpty(brandSquareBanner.getId())) {//新增
		}else {
			params.put("id",brandSquareBanner.getId());
		}
		params.put("imageLocation",brandSquareBanner.getImageLocation().substring(brandSquareBanner.getImageLocation().indexOf("/UploadFile/"), brandSquareBanner.getImageLocation().length()));
		params.put("identifyType",brandSquareBanner.getIdentifyType());
		params.put("sortNum",brandSquareBanner.getSortNum());
		params.put("title",brandSquareBanner.getTitle());
		params.put("jumpTarget",brandSquareBanner.getJumpTarget());
		params.put("content",brandSquareBanner.getContent());
		params.put("columnId",brandSquareBanner.getColumnId());
		
		String resultJson = HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(params));
		if(StringUtils.isEmpty(resultJson)) {
			return Msg.error("调用接口错误");
		}
		Map<String,Object> resultMap = JsonUtils.parse( resultJson,HashMap.class);
		String object = resultMap.get("code")+"";
		if(object.equals("200")) {
			return Msg.ok("保存成功");
		}else {
			return Msg.error(resultMap.get("message")+"");
		}
    	
    	
    }





}
