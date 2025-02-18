package com.shq.oper.controller.coupons;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.enums.CouponsTypeEnum;
import com.shq.oper.enums.CouponsTypeModelEnum;
import com.shq.oper.enums.api.CouponsFromSysEnums;
import com.shq.oper.mapper.primarydb.CouponsMapper;
import com.shq.oper.mapper.primarydb.CouponsTypeRuleMapper;
import com.shq.oper.model.domain.primarydb.Coupons;
import com.shq.oper.model.domain.primarydb.CouponsTypeRule;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.SelectValueData;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.impl.primarydb.AdminServiceImpl;
import com.shq.oper.service.primarydb.CouponsTypeRuleService;
import com.shq.oper.util.CacheKeyConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author ljz
 * @date 2018年4月27日
 */
@RestController
@Slf4j
@RequestMapping("/coupons/couponsTypeRule")
public class CouponsTypeRuleController extends BaseController {
	
	@Autowired
	private CouponsTypeRuleService couponsTypeRuleService;
	@Autowired
	private CouponsTypeRuleMapper couponsTypeRuleMapper;
	@Autowired
	private static List<CouponsTypeRule> couponsTypeList;

	@Autowired
	private CouponsMapper couponsMapper;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		Map<String, Object> couponsTypeStatus = CouponsTypeEnum.getMap();
		Map<String, Object> couponsModelStatus = CouponsTypeModelEnum.getMap();
		Map<String, Object> operSysList = CouponsFromSysEnums.getObjectMap();
		request.setAttribute("operSysList", operSysList);
		request.setAttribute("couponsTypeStatus", couponsTypeStatus);
		request.setAttribute("couponsModelStatus", couponsModelStatus);
		request.setAttribute("couponsTypeList", couponsTypeList);
		return toPage(request);
	}

	@RequestMapping("/couponsindex")
	public ModelAndView couponsindex(HttpServletRequest request) {
		return toPage(request);
	}

	/**
	 * 搜索查询的条件当前页
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<CouponsTypeRule> list(CouponsTypeRule couponsType, PageDto page) {
//		PageInfo<CouponsTypeRule> datas = couponsTypeRuleService.selectPageAndCount(couponsType, page.getPage(),
//				page.getLimit());
		com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
		Page<CouponsTypeRule> datas =couponsTypeRuleMapper.queryLikeCouponsTypeRule(couponsType);
		return Data.ok(datas);
	}

	@RequestMapping("/coupons")
	public ModelAndView adminDetails(HttpServletRequest request, CouponsTypeRule couponsType) throws Exception {
		CouponsTypeRule coupons = couponsTypeRuleService.selectOne(couponsType);
		request.setAttribute("coupons", coupons);
		return toPage(request);
	}

	/**
	 * 添加/修改
	 */
	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, CouponsTypeRule couponsType) {

		try {
			LocalDateTime now = LocalDateTime.now();
			couponsType.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
			couponsType.setUpdateTime(now);
			Integer type = couponsType.getType();
			String typeDesc = "";
			DecimalFormat dfDouble = new DecimalFormat("#.##");
			DecimalFormat dfInt = new DecimalFormat("#");
			if (String.valueOf(type).equals( CouponsTypeEnum.RANGEREDUCE.getCode() )) {
				if (!StringUtils.isEmpty(couponsType.getMinSpendMoney())
						&& !StringUtils.isEmpty(couponsType.getAmtFullReduce())) {
					if (Integer.parseInt(dfDouble.format(couponsType.getMinSpendMoney())) <= Integer.parseInt(dfDouble.format(couponsType.getAmtFullReduce())))
					{
						return Msg.error("保存失败，减少的金额必须小于满足金额");
					}
				}
				typeDesc = "满" + dfDouble.format(couponsType.getMinSpendMoney()) + "减"
						+ dfDouble.format(couponsType.getAmtFullReduce());
			}
			if (String.valueOf(type).equals( CouponsTypeEnum.ERECTREDUCE.getCode() )) {
				BigDecimal tmp=BigDecimal.ZERO;
				couponsType.setMinSpendMoney(tmp);
					typeDesc = "无门槛立减" + dfDouble.format(couponsType.getAmtSub()) + "元";
			}
			if (String.valueOf(type).equals( CouponsTypeEnum.DISCOUNT.getCode() )) {
				if (!StringUtils.isEmpty(couponsType.getAmtDiscount())) {
					if (Integer.parseInt(dfDouble.format(couponsType.getAmtDiscount())) > 100)
					{
						return Msg.error("保存失败，折扣必须在100以内");
					}
				}
				typeDesc = dfDouble.format(couponsType.getAmtDiscount().divide(new BigDecimal("10"))) + "折折扣券";
				if(couponsType.getMinSpendMoney().compareTo(BigDecimal.ZERO ) > 0) {//有最低消费限制
					typeDesc = "满" + dfDouble.format(couponsType.getMinSpendMoney()) + "打"+ dfDouble.format(couponsType.getAmtDiscount().divide(new BigDecimal("10"))) + "折券";
				}
			}
			couponsType.setTypeDesc(typeDesc);
			if (StringUtils.isEmpty(couponsType.getId())) {
				couponsType.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
				couponsType.setCreateTime(now);
				couponsTypeRuleService.insert(couponsType);
			} else {
				couponsTypeRuleService.update(couponsType);
			}
			
			//清除Coupons-query- 的缓存
			SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONS_QUERY);
			
			return Msg.ok("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Msg.error("保存失败");
		}
	}

	/**
	 * 禁用单条数据
	 */
	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, CouponsTypeRule couponsType) {
		couponsType.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		return couponsTypeRuleService.disabled(couponsType);
	}

	/**
	 * 启用单条数据
	 */
	@RequestMapping("/enableBy")
	public Msg<String> disabledFalse(HttpServletRequest request, CouponsTypeRule couponsType) {
		couponsType.setIsDisabled(false);
		couponsTypeRuleService.update(couponsType);
		//清除Coupons-query- 的缓存
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONS_QUERY);
		
		return Msg.ok("保存成功");
	}

	/**
	 * 删除单条数据
	 */
	@RequestMapping("/deleteOn")
	public Msg<String> deleteOn(HttpServletRequest request, int id) {
		int on = couponsTypeRuleMapper.delToCouponsTypeRule(id);
		if (on == 1) {
			couponsTypeRuleMapper.updToCouponsTypeRule(id);
		} else {
			return Msg.ok("删除失败");
		}
		return Msg.ok("删除成功");
	}

	/**
	 * 查询model
	 */
	@RequestMapping(value = "/listmodel", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<CouponsTypeRule> listModel(CouponsTypeRule couponsType, PageDto pageDto) {
		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		Page<CouponsTypeRule> datas = couponsTypeRuleMapper.selectToModel(couponsType);
		return Data.ok(datas);
	}

	/**
	 * 查询type
	 */
	@RequestMapping(value = "/listtype", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<CouponsTypeRule> listType(CouponsTypeRule couponsType, PageDto pageDto) {
		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		Page<CouponsTypeRule> datas = couponsTypeRuleMapper.selectToType(couponsType);
		return Data.ok(datas);
	}

	@GetMapping(value = "/selectValue")
	public List<SelectValueData<Long>> dict(CouponsTypeRule entity, boolean isCode) {
		if(!StringUtils.isEmpty(entity.getDepend())) {
			entity.setFromSys(entity.getDepend());
		}else {
			entity.setFromSys(CouponsFromSysEnums.SYS_OPERATE_CENTER.getCode());
		}
		
		List<CouponsTypeRule> datas = couponsTypeRuleMapper.selectByRowBounds(entity, RowBounds.DEFAULT);
		List<CouponsTypeRule> newlist=new ArrayList<>();
		datas.forEach(
				p->{
					if (!p.getIsDisabled()){
						newlist.add(p);
					}
				}
		);
		List<SelectValueData<Long>> list = newlist.stream().map(d -> new SelectValueData<Long>(d.getName(), d.getId()))
				.collect(Collectors.toList());
		return list;
	}

	@RequestMapping(value = "/couponsList", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<Coupons> list(String  couponsRuleId, PageDto pageDto) {

		Coupons coupons=new Coupons();
		coupons.setCouponsRuleId(couponsRuleId);

		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		Page<Coupons> entitys = couponsMapper.queryCouponsAndCouponsCategoryRuleAndCouponsTypeRule(coupons);
		return Data.ok(entitys);
	}


}
