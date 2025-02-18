package com.shq.oper.controller.coupons;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.model.domain.primarydb.Area;
import com.shq.oper.model.domain.primarydb.CouponsAreaRule;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.service.primarydb.AreaService;
import com.shq.oper.service.primarydb.CouponsAreaRuleService;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/coupons/couponsAreaRule")
public class CouponsAreaRuleController extends BaseController {

	@Autowired
	private CouponsAreaRuleService couponsAreaRuleService;
	
	@Autowired
	private AreaService areaService;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<CouponsAreaRule> list(CouponsAreaRule entity, PageDto page) {
		PageInfo<CouponsAreaRule> entitys = couponsAreaRuleService.selectPageAndCount(entity, page.getPage(), page.getLimit());		
		return Data.ok(entitys);
	}

	
	@RequestMapping("/couponsAreaRuleDetails")
	public ModelAndView couponsAreaRuleDetails(HttpServletRequest request,CouponsAreaRule couponsAreaRule) throws Exception {
		CouponsAreaRule couponsAreaRuleDetails = couponsAreaRuleService.selectOne(couponsAreaRule);
		request.setAttribute("couponsAreaRuleDetails", couponsAreaRuleDetails);
		return toPage(request);
	}
	
	@RequestMapping("/save")
	public Msg<String> save(HttpServletRequest request, CouponsAreaRule couponsAreaRule) {
		try {

			if (ObjectUtils.isEmpty(couponsAreaRule.getId())) {
				couponsAreaRule.setCreateTime(LocalDateTime.now());
				couponsAreaRule.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
				couponsAreaRule.setUpdateTime(LocalDateTime.now());
				couponsAreaRule.setLevRangeDetail(couponsAreaRule.getLevRangeDetail().replace(" ", ","));
				couponsAreaRuleService.insert(couponsAreaRule);
			} else {
				couponsAreaRule.setLevRangeDetail(couponsAreaRule.getLevRangeDetail().replace(" ", ","));
				couponsAreaRuleService.update(couponsAreaRule);
			}
			return Msg.ok("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Msg.error("保存失败");
		}
	}

	
	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, CouponsAreaRule couponsAreaRule) {
		LocalDateTime dtNow = LocalDateTime.now();
		couponsAreaRule.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		couponsAreaRule.setUpdateTime(dtNow);
		couponsAreaRule.setIsDisabled(false);
		couponsAreaRuleService.disabledById(couponsAreaRule);
		return Msg.ok("保存成功");
	}

	
	@RequestMapping(value = "/selectArea",method = RequestMethod.POST)
	@ResponseBody
	public String selectAtea(String sLev,String name) {
		Area areas = new Area();
		areas.setLev(sLev);
		areas.setName(name);
		List<Area> area = areaService.selectArea(areas);
		return JsonUtils.toDefaultJson(area);
	}


}
