package com.shq.oper.controller.coupons;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.mapper.primarydb.CouponsMapper;
import com.shq.oper.model.domain.primarydb.Coupons;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.impl.primarydb.AdminServiceImpl;
import com.shq.oper.service.primarydb.CouponsService;
import com.shq.oper.util.CacheKeyConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author ljz
 * @date 2018年5月3日
 */
@RestController
@Slf4j
@RequestMapping("/coupons/examine")
public class CouponsExamineController extends BaseController {

	
	@Autowired
	SystemProperties systemProperties;

	@Autowired
	CouponsService couponsService;
	@Autowired
	CouponsMapper couponsMapper;



	/**
	 * 角色页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}

	/**
	 * 未审核通过的优惠券数据
	 * 
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<Coupons> list(Coupons entity, PageDto pageDto) {
		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		Page<Coupons> entitys = couponsMapper.queryCouponsAndCouponsCategoryRuleAndCouponsTypeRuleForCheck(entity);
		return Data.ok(entitys);
	}

	/**
	 * 优惠券的批量审核通过
	 * 
	 * @param couponsIds
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/changeFinanStatus")
	public Msg<String> changeFinanStatus(Coupons coupons, String couponsIds, HttpServletRequest request) {
		LocalDateTime dtNow = LocalDateTime.now();
		coupons.setFinanAuditor(this.getCreateOrUpdateAdminName(request));
		coupons.setFinanAuditTime(dtNow);
		Msg<String> msg = couponsService.batchUpdateFinanStatusToOk(couponsIds, coupons);
		
		//清除Coupons-query- 的缓存
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONS_QUERY);
		
		return msg;
	}

}