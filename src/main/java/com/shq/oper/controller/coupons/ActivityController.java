package com.shq.oper.controller.coupons;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.Page;
import com.shq.oper.mapper.primarydb.CouponsMapper;
import com.shq.oper.model.domain.primarydb.Coupons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.enums.api.CouponsFromSysEnums;
import com.shq.oper.model.domain.primarydb.Activity;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.SelectValueData;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.impl.primarydb.AdminServiceImpl;
import com.shq.oper.service.primarydb.ActivityService;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@RestController
@Slf4j
@RequestMapping("/coupons/activity")
public class ActivityController extends BaseController {
	@Autowired
	private ActivityService activityService;
	@Autowired
	SystemProperties systemProperties;

	@Autowired
	private CouponsMapper couponsMapper;


	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		Map<String, Object> operSysList = CouponsFromSysEnums.getObjectMap();
		request.setAttribute("operSysList", operSysList);
		return toPage(request);
		
	}

	@RequestMapping("/coupons")
	public ModelAndView couponsindex(HttpServletRequest request) {
		return toPage(request);
	}


	@RequestMapping("/activityDetails")
	public ModelAndView activityDetails(HttpServletRequest request, Activity activity) throws Exception {
		Activity activityDetails = activityService.selectOne(activity);
		request.setAttribute("activityDetails", activityDetails);
		return toPage(request);
	}

	@RequestMapping("/save")
	public @ResponseBody Msg<String> save(HttpServletRequest request, Activity activity) {
		if (activity.getSendTimeStart().isAfter(activity.getSendTimeEnd())){
			return  Msg.error("抱歉，活动开始时间不能大于活动结束时间");
		}
		
		LocalDateTime dtNow = LocalDateTime.now();
		activity.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		activity.setUpdateTime(dtNow);
		if (ObjectUtils.isEmpty(activity.getId())) {
//			Activity selectOne = activityService.queryByBatch(activity.getBatch());
//			if(selectOne != null) {
//				return Msg.error("活动批次["+activity.getBatch()+"]已存在，请修改后重新保存");
//			}
//            String batch=DateUtils.to(dtNow,DateUtils.DateFormat.LONG_DATE_PATTERN_WITH_MILSEC_NONE);
//            activity.setBatch(batch);
			activity.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
			activity.setCreateTime(dtNow);
			activityService.insert(activity);
		} else {
			activityService.update(activity);
			//清除 Activity--query-- 的缓存
			SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_ACTIVITY_QUERY);
		}
		return Msg.ok("保存成功");
	}

	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, Activity activity) {
		LocalDateTime dtNow = LocalDateTime.now();
		activity.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		activity.setUpdateTime(dtNow);
		activity.setIsDisabled(true);

		return activityService.disabled(activity);
	}

	@RequestMapping("/enableBy")
	public Msg<String> enableById(HttpServletRequest request, Activity activity) {
		LocalDateTime dtNow = LocalDateTime.now();
		activity.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		activity.setUpdateTime(dtNow);
		activityService.enableById(activity);
		//清除 Activity--query-- 的缓存
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_ACTIVITY_QUERY);
		return Msg.ok("保存成功");
	}
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<Activity> list(Activity entity, PageDto page) {
		PageInfo<Activity> entitys = activityService.selectPageAndCount(entity, page.getPage(), page.getLimit());
		return Data.ok(entitys);
	}

	/**
	 *系统来源 下拉框数据 
	 **/
	@GetMapping(value = "/fromsysDict")
	public List<SelectValueData<String>> fromsysDict(HttpServletRequest request,Activity entity) {
		Map<String, Object> operSysMap = CouponsFromSysEnums.getObjectMap();
		List<SelectValueData<String>> list=new ArrayList<>();
		for(String key : operSysMap.keySet() ) {
			list.add(new SelectValueData<String>(key, operSysMap.get(key).toString()));
		}
		return list;
	}

	/**
	 *@author ltg
	 *@date 2018/5/7 11:17
	 * @params:[entity, isCode]
	 * @return: java.util.List<com.shq.oper.model.dto.SelectValueData<java.lang.Long>>
	 */
	@GetMapping(value = "/selectValue")
	public List<SelectValueData<Long>> selectValue(HttpServletRequest request,Activity entity) {
		if(!StringUtils.isEmpty(entity.getDepend())) {
			entity.setFromSys(entity.getDepend());
		}else {
			entity.setFromSys(CouponsFromSysEnums.SYS_OPERATE_CENTER.getCode());
		}
		List<Activity> activitys=activityService.select(entity);
		List<SelectValueData<Long>> list=new ArrayList<>();
		LocalDateTime dtNow = LocalDateTime.now();
		activitys.forEach(
				p->{
					//禁用 和 时间过期的不现实
					if (!p.getIsDisabled() &&  dtNow.isBefore(p.getSendTimeEnd()) ) {
						SelectValueData<Long> selectValueData=new SelectValueData<>(p.getName(),p.getId());
						list.add(selectValueData);
					}
				}
		);
		request.setAttribute("activitys", list);
		return list;
	}

	/**
	 *@author ltg
	 *@date 2018/5/7 14:21
	 * @params:[aid]
	 * @return: com.shq.oper.model.domain.Activity
	 */
	@GetMapping(value = "/single")
	public Activity getSingle(HttpServletRequest request,long aid){

		Activity activity=new Activity();
		activity.setId(aid);
		Activity result= activityService.selectOne(activity);
		return result;

	}

	@RequestMapping(value = "/couponlist", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<Coupons> list(String  activityId, PageDto pageDto) {

		Coupons coupons=new Coupons();
		coupons.setActivityId(activityId);

		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		Page<Coupons> entitys = couponsMapper.queryCouponsAndCouponsCategoryRuleAndCouponsTypeRule(coupons);
		return Data.ok(entitys);
	}


}