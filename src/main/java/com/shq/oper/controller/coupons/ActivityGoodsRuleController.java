package com.shq.oper.controller.coupons;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.enums.ActivityGoodsRuleTypeEnum;
import com.shq.oper.enums.api.CouponsFromSysEnums;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.CouponsMapper;
import com.shq.oper.mapper.shq520new.DistrFirstCategotyMapper;
import com.shq.oper.mapper.shq520new.DistrFourCategotyMapper;
import com.shq.oper.mapper.shq520new.DistrSecondCategotyMapper;
import com.shq.oper.mapper.shq520new.DistrThirdCategotyMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.ActivityGoodsRule;
import com.shq.oper.model.domain.primarydb.ActivityGoodsRuleDetails;
import com.shq.oper.model.domain.primarydb.ActivityGoodsRuleGoodsProduct;
import com.shq.oper.model.domain.primarydb.Coupons;
import com.shq.oper.model.domain.primarydb.ProductPrice;
import com.shq.oper.model.domain.shq520new.DistrFirstCategoty;
import com.shq.oper.model.domain.shq520new.DistrFourCategoty;
import com.shq.oper.model.domain.shq520new.DistrSecondCategoty;
import com.shq.oper.model.domain.shq520new.DistrThirdCategoty;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.SelectValueData;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.impl.primarydb.AdminServiceImpl;
import com.shq.oper.service.primarydb.ActivityGoodsRuleDetailsService;
import com.shq.oper.service.primarydb.ActivityGoodsRuleGoodsProductService;
import com.shq.oper.service.primarydb.ActivityGoodsRuleService;
import com.shq.oper.service.primarydb.ProductPriceService;
import com.shq.oper.service.primarydb.ProductService;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/coupons/activity/goods/rule")
public class ActivityGoodsRuleController extends BaseController {
	@Autowired
	private ActivityGoodsRuleService activityGoodsRuleService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductPriceService productPriceService;
	@Autowired
	private ActivityGoodsRuleGoodsProductService activityGoodsRuleGoodsProductService;

	@Autowired
	private DistrFirstCategotyMapper distrFirstCategotyMapper;

	@Autowired
	private DistrFourCategotyMapper distrFourCategotyMapper;

	@Autowired
	private DistrSecondCategotyMapper distrSecondCategotyMapper;

	@Autowired
	private DistrThirdCategotyMapper distrThirdCategotyMapper;

	@Autowired
	private ActivityGoodsRuleDetailsService activityGoodsRuleDetailsService;

	@Autowired
	ShopperMapper shopperMapper;

    @Autowired
    private CouponsMapper couponsMapper;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
        Map<String, Object>activityGoodRuleList= new LinkedHashMap<>();
        activityGoodRuleList.put("----请选择----", "");
        activityGoodRuleList.putAll(ActivityGoodsRuleTypeEnum.getObjectMap());
        request.setAttribute("activityGoodRuleList", activityGoodRuleList);
        
        Map<String, Object> operSysList = CouponsFromSysEnums.getObjectMap();
		request.setAttribute("operSysList", operSysList);
	    return toPage(request);
	}
	@RequestMapping("/setting")
	public ModelAndView setting(HttpServletRequest request) {
		return toPage(request);
	}


	@RequestMapping("/rellist")
	public ModelAndView activityGoodsRelationList(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping("/details")
	public ModelAndView detailslist(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping("/supdetails")
	public ModelAndView supdetails(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping("/proprice")
	public ModelAndView proprice(HttpServletRequest request) {
		return toPage(request);
	}

    @RequestMapping("/detailsSetting")
    public ModelAndView detailsSetting(HttpServletRequest request) {
        return toPage(request);
    }

	@RequestMapping("/reedsupdetails")
	public ModelAndView reedsupdetails(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping("/reedcatedetails")
	public ModelAndView reedcatedetails(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping("/supplier")
	public ModelAndView supplier(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping("/catedetails")
	public ModelAndView catedetails(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping("/addcategory")
	public ModelAndView addcategory(HttpServletRequest request) {
		return toPage(request);
	}

    @RequestMapping("/coupons")
    public ModelAndView coupons(HttpServletRequest request) {
        return toPage(request);
    }

//    @RequestMapping("/queryGoods")
//	public Data<ProductPrice> queryGoods(HttpServletRequest request, Product entity,long ruleId ) {
//		if(StringUtils.isEmpty(entity.getProduct_Code())) {
//			return Data.error("商品Code不能为空");
//		}
//		return productService.queryGoods(entity);
//	}

	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, ActivityGoodsRule activityGoodsRule) {
		LocalDateTime dtNow = LocalDateTime.now();
		activityGoodsRule.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		activityGoodsRule.setUpdateTime(dtNow);
		if (ObjectUtils.isEmpty(activityGoodsRule.getId())) {
			activityGoodsRule.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
			activityGoodsRule.setCreateTime(dtNow);
			activityGoodsRuleService.insert(activityGoodsRule);
		} else {
			activityGoodsRuleService.update(activityGoodsRule);
		}
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_GOODS_RULE_QUERY);
		
		return Msg.ok("保存成功");
	}

	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, ActivityGoodsRule activityGoodsRule) {
		LocalDateTime dtNow = LocalDateTime.now();
		activityGoodsRule.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		activityGoodsRule.setUpdateTime(dtNow);
		return activityGoodsRuleService.disabled(activityGoodsRule);
	}

	@RequestMapping("/enableBy")
	public Msg<String> enableBy(HttpServletRequest request, ActivityGoodsRule activityGoodsRule) {
		LocalDateTime dtNow = LocalDateTime.now();
		activityGoodsRule.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		activityGoodsRule.setUpdateTime(dtNow);
		activityGoodsRuleService.enableById(activityGoodsRule);

		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_GOODS_RULE_QUERY);
		return Msg.ok("保存成功");
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<ActivityGoodsRule> list(ActivityGoodsRule entity, PageDto page) {
		PageInfo<ActivityGoodsRule> entitys = activityGoodsRuleService.selectPageAndCount(entity, page.getPage(), page.getLimit());
		return Data.ok(entitys);
	}

	/**
	 * 供应商列表
	 *@author ltg
	 *@date 2018/6/20 16:16
	 * @params:[entity, page]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.mssqlshq.Shopper>
	 */
	@RequestMapping(value = "/suplist", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<Shopper> suplist(Shopper entity, PageDto page) {

		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		Page<Shopper> entitys = shopperMapper.queryByShopper(entity);
		return Data.ok(entitys);
	}

	/**
	 *@author ltg
	 *@date 2018/6/22 11:35
	 * @params:[entity, page]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.shq520new.DistrFirstCategoty>
	 */
	@RequestMapping(value = "/distrFirstCategotyList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<DistrFirstCategoty> distrFirstCategotyList(DistrFirstCategoty entity, PageDto page) {

		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		Page<DistrFirstCategoty> entitys = distrFirstCategotyMapper.queryDistrFirstCategotyList(entity);
		return Data.ok(entitys);
	}

	/**
	 *@author ltg
	 *@date 2018/6/22 11:35
	 * @params:[entity, page]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.shq520new.DistrFourCategoty>
	 */
	@RequestMapping(value = "/distrDistrFourCategotyList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<DistrFourCategoty> distrFirstCategotyList(DistrFourCategoty entity, PageDto page) {

		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		Page<DistrFourCategoty> entitys = distrFourCategotyMapper.queryDistrFourCategotyList(entity);
		return Data.ok(entitys);
	}


	/**
	 *@author ltg
	 *@date 2018/6/22 11:35
	 * @params:[entity, page]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.shq520new.DistrSecondCategoty>
	 */
	@RequestMapping(value = "/distrDistrSecondCategotyList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<DistrSecondCategoty> distrFirstCategotyList(DistrSecondCategoty entity, PageDto page) {

		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		Page<DistrSecondCategoty> entitys = distrSecondCategotyMapper.queryDistrSecondCategotyList(entity);
		return Data.ok(entitys);
	}

	/**
	 *@author ltg
	 *@date 2018/6/22 11:35
	 * @params:[entity, page]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.shq520new.DistrThirdCategoty>
	 */
	@RequestMapping(value = "/distrDistrThirdCategotyList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<DistrThirdCategoty> distrDistrThirdCategotyList(DistrThirdCategoty entity, PageDto page) {

		entity.setPageNumKey(page.getPage());
		entity.setPageSizeKey(page.getLimit());
		Page<DistrThirdCategoty> entitys =distrThirdCategotyMapper.queryDistrThirdCategotyList(entity);
		return Data.ok(entitys);
	}

	/**
	 *@author ltg
	 *@date 2018/6/4 11:17
	 * @params:[entity, isCode]
	 * @return: java.util.List<com.shq.oper.model.dto.SelectValueData<java.lang.Long>>
	 */
	@GetMapping(value = "/selectValue")
	public List<SelectValueData<Long>> dict(HttpServletRequest request, ActivityGoodsRule activityGoodsRule) {
		if(!StringUtils.isEmpty(activityGoodsRule.getDepend())) {
			activityGoodsRule.setFromSys(activityGoodsRule.getDepend());
		}else {
			activityGoodsRule.setFromSys(CouponsFromSysEnums.SYS_OPERATE_CENTER.getCode());
		}
		
		List<ActivityGoodsRule> activitys=activityGoodsRuleService.select(activityGoodsRule);
		List<SelectValueData<Long>> list=new ArrayList<>();
		activitys.forEach(
				p->{
					if (!p.getIsDisabled()) {
						SelectValueData<Long> selectValueData=new SelectValueData<>(p.getName(),p.getId());
						list.add(selectValueData);
					}
				}
		);
		return list;
	}

	/**
	 * 获取规则与商品关系列表
	 *@author ltg
	 *@date 2018/6/4 16:56
	 * @params:[entity, page]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.ActivityGoodsRuleGoodsProduct>
	 */
	@RequestMapping(value = "/activityGoodsRelationList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<ActivityGoodsRuleGoodsProduct> activityGoodsRelationList(ActivityGoodsRuleGoodsProduct entity, PageDto page) {

		PageInfo<ActivityGoodsRuleGoodsProduct> entitys = activityGoodsRuleGoodsProductService.selectPageAndCount(entity, page.getPage(), page.getLimit());
		return Data.ok(entitys);
	}

	/**
	 *@author ltg
	 *@date 2018/6/13 14:07
	 * @params:[request, productPriceList]
	 * @return: com.shq.oper.model.dto.Msg<java.lang.String>
	 */
	@RequestMapping(value = "/saveProductPrice", method = { RequestMethod.GET, RequestMethod.POST })
	public Msg<String> saveProductPrice(HttpServletRequest request,  @RequestBody List<ProductPrice> productPriceList) {

		if (productPriceList==null||productPriceList.size()<=0)
			return  Msg.ok("没有要保存的数据");

		String adminName=this.getAdmin(request).getName();
		productPriceService.saveProductPriceList(adminName,productPriceList);
		return  Msg.ok("保存完成");
	}


	/**
	 * 规则详情列表
	 *@author ltg
	 *@date 2018/6/7 16:29
	 * @params:[entity, page]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.primarydb.ActivityGoodsRuleDetails>
	 */
	@RequestMapping(value = "/detailsList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<ActivityGoodsRuleDetails> detailsList(@RequestParam  long  ruleid,ActivityGoodsRuleDetails activityGoodsRuleDetails, PageDto page) {
		ActivityGoodsRuleDetails details=new ActivityGoodsRuleDetails();
		details.setActivityGoodsRuleId(ruleid);
		details.setRefSignValue(activityGoodsRuleDetails.getRefSignValue());
		details.setRefSignName(activityGoodsRuleDetails.getRefSignName());
		PageInfo<ActivityGoodsRuleDetails> entitys = activityGoodsRuleDetailsService.selectPageAndCount(details, page.getPage(), page.getLimit());
		return Data.ok(entitys);
	}

	/**
	 * 保存规则详情
	 *@author ltg
	 *@date 2018/6/7 18:29
	 * @params:[ruleid, page]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.primarydb.ActivityGoodsRuleDetails>
	 */
	@RequestMapping(value = "/saveDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public  Msg<String> saveDetails(HttpServletRequest request,@RequestParam  long  ruleId,String code) {

		if (StringUtils.isEmpty(code)){
			return Msg.error("code值不能为空");
		}
		LocalDateTime dtNow = LocalDateTime.now();
		try {
			String adminStr=this.getAdmin(request).getName() + "-" + this.getAdmin(request).getId();
			return activityGoodsRuleDetailsService.insertDetails(adminStr,ruleId,code,dtNow, 1000);

		} catch (Exception e) {
			log.error("规则明细保存失败", e);
			return Msg.ok("规则明细保存失败");
		}
	}

	/**
	 *@author ltg
	 *@date 2018/6/8 10:16
	 * @params:[request, code, ruleId]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.primarydb.Product>
	 */
	@RequestMapping("/searchGoods")
	public Data<DistributionProduct> searchGoods(HttpServletRequest request, String code) {

		if(StringUtils.isEmpty(code))
			return Data.error("商品Code不能为空");
		return productService.searchGoods(code);

	}

	/**
	 *@author ltg
	 *@date 2018/6/9 10:15
	 * @params:[detailId]
	 * @return: com.shq.oper.model.dto.Msg<java.lang.String>
	 */
	@RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
	public Msg<String> delete(long detailId) {
		activityGoodsRuleDetailsService.deleteByPk(detailId);
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_GOODS_RULE_QUERY);
		return Msg.ok("删除成功");
	}

	/**
	 *@author ltg
	 *@date 2018/6/12 12:44
	 * @params:[request, ruleId, seq, detailId, shopname]
	 * @return: com.shq.oper.model.dto.Msg<java.lang.String>
	 */
	@RequestMapping(value = "/saveSupplierDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public  Msg<String> saveDetails(HttpServletRequest request,@RequestParam  long  ruleId,Integer seq) {

		if (StringUtils.isEmpty(seq)){
			return Msg.error("请输入seq");
		}

		String adminStr=this.getAdmin(request).getName() + "-" + this.getAdmin(request).getId();
		try {
			return activityGoodsRuleDetailsService.saveSupplierDetails(ruleId,seq,adminStr);

		} catch (Exception e) {
			log.error("规则明细保存失败", e);
			return Msg.ok("规则明细保存失败");
		}
	}

	/**
	 *@author ltg
	 *@date 2018/6/13 14:07
	 * @params:[request, seq]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.mssqlshq.Shopper>
	 */
	@RequestMapping("/SearchSupplier")
	public Data<Shopper> SearchSupplier(HttpServletRequest request, Integer seq ) {

		if (StringUtils.isEmpty(seq)) {
			return Data.error("请输入seq");
		}
		try {

			Shopper shopperparam=new Shopper();
			shopperparam.setSeq(seq);
			Shopper shopper=shopperMapper.queryBySupplier(shopperparam);
			List<Shopper> list=new ArrayList<>();
			list.add(shopper);
			return new Data<Shopper>("true",list,1,null);

		} catch (Exception e) {
			log.error("查询供应商异常",e);
			return Data.error("查询供应商失败,");
		}
	}


	/**
	 * 批量插入供应商的规则明细
	 *@author ltg
	 *@date 2018/6/20 19:54
	 * @params:[request, ruleId, shopperStr]
	 * @return: com.shq.oper.model.dto.Msg<java.lang.String>
	 */
	@RequestMapping(value = "/saveSuprDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public  Msg<String> saveSuprDetails(HttpServletRequest request,@RequestParam  long  ruleId,String shopperStr) {

		List<Shopper> shopperList=JsonUtils.parseList(shopperStr,Shopper.class);
		if (shopperList==null||shopperList.size()<=0){
			return  Msg.error("参数错误");
		}
		String adminStr=this.getAdmin(request).getName() + "-" + this.getAdmin(request).getId();
		return activityGoodsRuleDetailsService.bathSaveSupDetails(adminStr, ruleId, shopperList);
	}

	/**
	 * 批量插入类目的规则明细
	 *@author ltg
	 *@date 2018/6/22 14:11
	 * @params:[request, ruleId, categorystr]
	 * @return: com.shq.oper.model.dto.Msg<java.lang.String>
	 */
	@RequestMapping(value = "/saveCategoryDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public  Msg<String> saveCategoryDetails(HttpServletRequest request,@RequestParam  long  ruleId,String categorystr) {

		List<DistrFirstCategoty> distrFirstCategotylist=JsonUtils.parseList(categorystr,DistrFirstCategoty.class);
		if (distrFirstCategotylist==null||distrFirstCategotylist.size()<=0){
			return  Msg.error("参数错误");
		}
		String adminStr=this.getAdmin(request).getName() + "-" + this.getAdmin(request).getId();
		return activityGoodsRuleDetailsService.bathSaveCategoryDetails(adminStr,ruleId,distrFirstCategotylist);
	}

	/**
	 * 查询类目明细
	 *@author ltg
	 *@date 2018/6/22 15:34
	 * @params:[request, id, type]
	 * @return: com.shq.oper.model.dto.Data<java.lang.Object>
	 */

	@RequestMapping("/SearchCategory")
	public Data<Object> SearchCategory(HttpServletRequest request, Integer id ,Integer type) {

		if (StringUtils.isEmpty(id)) {
			return Data.error("请输入id");
		}
		Object obj=null;
		ActivityGoodsRuleTypeEnum enumTem=ActivityGoodsRuleTypeEnum.getByCode(type+"");
		switch (enumTem){
			case FIRST_CATEGORY:
				DistrFirstCategoty distrFirstCategoty=new DistrFirstCategoty();
				distrFirstCategoty.setId(id);
				obj=distrFirstCategotyMapper.selectByPrimaryKey(distrFirstCategoty);
				break;
			case TWO_CATEGORY:
				DistrSecondCategoty distrSecondCategoty=new DistrSecondCategoty();
				distrSecondCategoty.setId(id);
				obj=distrSecondCategotyMapper.selectByPrimaryKey(distrSecondCategoty);
				break;
			case THREE_CATEGORY:
				DistrThirdCategoty distrThirdCategoty=new DistrThirdCategoty();
				distrThirdCategoty.setId(id);
				obj=distrThirdCategotyMapper.selectByPrimaryKey(distrThirdCategoty);
				break;
			case FOUR_CATEGORY:
				DistrFourCategoty distrFourCategoty=new DistrFourCategoty();
				distrFourCategoty.setId(id);
				obj=distrFourCategotyMapper.selectByPrimaryKey(distrFourCategoty);
				break;
			default:
				break;
		}

		if (obj==null){
			return Data.error("查询供类目失败,");
		}
		List<Object> list=new ArrayList<>();
		list.add(obj);
		return new Data<Object>("true",list,1,null);

	}


	/**
	 * 获取品类规则绑定的优惠券
	 *@author ltg
	 *@date 2018/7/26 19:02
	 * @params:[categoryRuleId, pageDto]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.primarydb.Coupons>
	 */
    @RequestMapping(value = "/couponsList", method = { RequestMethod.GET, RequestMethod.POST })
    public Data<Coupons> list(String  categoryRuleId, PageDto pageDto) {

        Coupons coupons=new Coupons();
        coupons.setCategoryRuleId(new BigDecimal(categoryRuleId));

        com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
        Page<Coupons> entitys = couponsMapper.queryCouponsAndCouponsCategoryRuleAndCouponsTypeRule(coupons);
        return Data.ok(entitys);
    }

}