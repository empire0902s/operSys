package com.shq.oper.controller.order;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.mongo.MongoDistributionOrdersDao;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.primarydb.OrderEvaluate;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.ImageData;
import com.shq.oper.model.dto.ImageUploadResultData;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.OrderEvaluateImgsService;
import com.shq.oper.service.primarydb.OrderEvaluateLikesService;
import com.shq.oper.service.primarydb.OrderEvaluateService;
import com.shq.oper.util.HttpClientUtil;

/**
 * 
 * @author ljz
 *
 */
@Controller
@RestController
@RequestMapping("/order/manualevaluate")
public class ManualOrderEvaluationManagementController extends BaseController {
	@Autowired
	private OrderEvaluateService orderEvaluateService;

	@Autowired
	private OrderEvaluateLikesService orderEvaluateLikesService;

	@Autowired
	private OrderEvaluateImgsService orderEvaluateImgsService;

	@Autowired
	private ShopperMapper shopperMapper;
	@Autowired
	private MongoDistributionOrdersDao mongoDistributionOrdersDao;
	
	@Autowired
	private DistributionProductMapper distributionProductMapper;
	
	@Autowired
	private ShqApi ShqApi;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}



	@RequestMapping("/manualEvaluateSave")
	public @ResponseBody Msg<String> manualEvaluateSave(HttpServletRequest request, OrderEvaluate orderEvaluate) {
		
		if(orderEvaluate.getEvaluateTime().isAfter(LocalDateTime.now())) {
			return Msg.error("评论时间不得大于当前时间");
		}
		orderEvaluate.setUpdateTime(LocalDateTime.now());
		orderEvaluate.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		
		Msg<String> result=orderEvaluateService.manualEvaluateSave(orderEvaluate);

		return result;
	}

	@RequestMapping("/orderReply")
	public @ResponseBody Msg<String> orderReply(HttpServletRequest request, OrderEvaluate orderEvaluate) {
		OrderEvaluate result;
		try {
			OrderEvaluate tmp = new OrderEvaluate();
			tmp.setId(orderEvaluate.getId());
			result = orderEvaluateService.selectOne(tmp);
			if (StringUtils.isEmpty(result)) {
				return Msg.error("编号为[" + orderEvaluate.getId() + "]的评价订单不存在");
			}
			//如未审核，则默认设置为审核通过
			if (result.getAuditStats() == 0) {
				//默认设置为审核通过
				result.setAuditStats((short)1);
				result.setAuditAdmin(this.getCreateOrUpdateAdminName(request));
				result.setAuditTime(LocalDateTime.now());
			}
			result.setUpdateTime(LocalDateTime.now());
			result.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
			result.setAuditReply(orderEvaluate.getAuditReply());
			orderEvaluateService.update(result);
			return Msg.ok("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Msg.error("后台系统有误，请联系管理员");
		}

	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<OrderEvaluate> list(OrderEvaluate entity,PageDto page) {
		com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
		Page<OrderEvaluate> entitys = orderEvaluateService.queryManualEvaluateList(entity);
		return Data.ok(entitys);
	}

	@RequestMapping("/disabled")
	public Msg<String> disabled(HttpServletRequest request, OrderEvaluate entity) {
		OrderEvaluate orderEvaluate = orderEvaluateService.selectOne(entity);
		if(!StringUtils.isEmpty(orderEvaluate)) {
			LocalDateTime dtNow = LocalDateTime.now();
			orderEvaluate.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
			orderEvaluate.setUpdateTime(dtNow);
			orderEvaluate.setAuditStats((short) 2);
			orderEvaluateService.update(orderEvaluate);
			return Msg.ok("禁用成功");
		}else {
			return Msg.error("禁用失败");
		}
	}
	@RequestMapping("/enableBy")
	public Msg<String> enableById(HttpServletRequest request, OrderEvaluate entity) {
		OrderEvaluate orderEvaluate = orderEvaluateService.selectOne(entity);
		if(!StringUtils.isEmpty(orderEvaluate)) {
			LocalDateTime dtNow = LocalDateTime.now();
			orderEvaluate.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
			orderEvaluate.setUpdateTime(dtNow);
			orderEvaluate.setAuditStats((short) 1);
			orderEvaluateService.update(orderEvaluate);
			return Msg.ok("启用成功");
		}else {
			return Msg.error("启用失败");
		}
	}
	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	@RequestMapping("/findByCode")
	public Msg<DistributionProduct> findByCode(HttpServletRequest request,DistributionProduct entity){
		DistributionProduct resultTmp = distributionProductMapper.selectOne(entity);
		if(StringUtils.isEmpty(resultTmp)) {
			return Msg.error("未查到对应商品",null);
		}
		return Msg.ok(resultTmp);
	}
	
}