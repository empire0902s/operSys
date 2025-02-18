package com.shq.oper.controller.coupons;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.shq.oper.model.domain.primarydb.CouponsCategoryRule;
import com.shq.oper.model.domain.primarydb.Resource;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.mapper.primarydb.CouponsCategoryRuleMapper;
import com.shq.oper.mapper.primarydb.ResourceMaper;
import com.shq.oper.model.dto.SelectValueData;
import com.shq.oper.service.primarydb.CouponsCategoryRuleService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ltg
 * @date 2018/4/18 11:40
 */
@RestController
@Slf4j
@RequestMapping("/coupons/category")
public class CouponsCategoryRuleController extends BaseController {

	@Autowired
	private CouponsCategoryRuleService couponsCategoryRuleService;
	@Autowired
	private CouponsCategoryRuleMapper couponsCategoryRuleMapper;

	@Autowired
	private ResourceMaper resourceMaper;


	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}
	@RequestMapping("/scope")
	public ModelAndView index1(HttpServletRequest request) {
		return toPage(request);
	}
	@GetMapping(value = "/selectValue")
	public List<SelectValueData<Long>> dict(CouponsCategoryRule entity, boolean isCode) {
		List<CouponsCategoryRule> datas =couponsCategoryRuleMapper.selectByRowBounds(entity, RowBounds.DEFAULT);
		List<SelectValueData<Long>> list = 
				datas.stream()
				.map(d -> new SelectValueData<Long>(d.getCategoryName(),d.getId()
						)
					).collect(Collectors.toList()); 
		return list;
	}

	/**
	 *@author ltg
	 *@date 2018/4/26 14:10
	 * @params:[request, couponsCategoryRule]
	 * @return: com.shq.oper.model.dto.Msg<java.lang.String>
	 */
	@RequestMapping(value = "/save", method = {RequestMethod.GET, RequestMethod.POST})
	public Msg<String> save(HttpServletRequest request, CouponsCategoryRule couponsCategoryRule){
		LocalDateTime dtNow = LocalDateTime.now();
		if (ObjectUtils.isEmpty(couponsCategoryRule.getId())){
			couponsCategoryRule.setCreateTime(dtNow);
			couponsCategoryRule.setUpdateTime(dtNow);
			couponsCategoryRule.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
			couponsCategoryRule.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
			couponsCategoryRuleService.insert(couponsCategoryRule);
		}else {
			couponsCategoryRule.setUpdateTime(dtNow);
			couponsCategoryRule.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
			couponsCategoryRuleService.update(couponsCategoryRule);
		}
		return Msg.ok("保存成功");
	}


	/**
	 *@author ltg
	 *@date 2018/4/26 14:18
	 * @params:[request, couponsCategoryRule]
	 * @return: com.shq.oper.model.dto.Msg<java.lang.String>
	 */
	@RequestMapping(value = "/disabled", method = {RequestMethod.GET, RequestMethod.POST})
	public Msg<String> disabled(HttpServletRequest request, CouponsCategoryRule couponsCategoryRule) {
		LocalDateTime dtNow = LocalDateTime.now();
		couponsCategoryRule.setUpdateTime(dtNow);
		couponsCategoryRule.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		couponsCategoryRuleService.disabledById(couponsCategoryRule);
		return Msg.ok("保存成功");
	}

	/**
	 *@author ltg
	 *@date 2018/4/26 14:18
	 * @params:[entity, page]
	 * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.CouponsCategoryRule>
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<CouponsCategoryRule> list(CouponsCategoryRule entity, PageDto page) {
		PageInfo<CouponsCategoryRule> entitys = couponsCategoryRuleService.selectPageAndCount(entity, page.getPage(), page.getLimit());
		return Data.ok(entitys);
	}

	/**
	 * 模拟用，因为爱之家还没提供接口
	 * @return
	 */
	@RequestMapping(value = "/Details", method = {RequestMethod.GET})
	public  Data<Resource> list1() {
		Resource entity=new Resource();
		Page<Resource> entitys = resourceMaper.queryWithParent(entity);
		return Data.ok(entitys);
	}


	@RequestMapping("/details")
	public ModelAndView roleDetails(HttpServletRequest request,CouponsCategoryRule couponsCategoryRule) throws Exception {
		CouponsCategoryRule categoryRuleDetails = couponsCategoryRuleService.selectOne(couponsCategoryRule);
		request.setAttribute("categoryDetails", categoryRuleDetails);
		return toPage(request);
	}
}
