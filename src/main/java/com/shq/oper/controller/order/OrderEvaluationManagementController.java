package com.shq.oper.controller.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.mongo.MongoDistributionOrdersDao;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.shq520new.DistributionOrdersDtlMapper;
import com.shq.oper.mapper.shq520new.DistributionProductImgMapper;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.MongoDistributionOrders;
import com.shq.oper.model.domain.mongo.OrderDetail;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.OrderEvaluate;
import com.shq.oper.model.domain.primarydb.OrderEvaluateImgs;
import com.shq.oper.model.domain.primarydb.OrderEvaluateLikes;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.OrderEvaluateImgsService;
import com.shq.oper.service.primarydb.OrderEvaluateLikesService;
import com.shq.oper.service.primarydb.OrderEvaluateService;

/**
 * 
 * @author ljz
 *
 */
@Controller
@RestController
@RequestMapping("/order/evaluate")
public class OrderEvaluationManagementController extends BaseController {
	@Autowired
	private OrderEvaluateService orderEvaluateService;

	@Autowired
	private OrderEvaluateLikesService orderEvaluateLikesService;

	@Autowired
	private OrderEvaluateImgsService orderEvaluateImgsService;

	@Autowired
	private DistributionProductMapper distributionProductMapper;

	@Autowired
	private DistributionProductImgMapper distributionProductImgMapper;

	@Autowired
	private DistributionOrdersDtlMapper distributionOrdersDtlMapper;
	@Autowired
	private ShopperMapper shopperMapper;
	@Autowired
	private MongoDistributionOrdersDao mongoDistributionOrdersDao;
	
	@Autowired
	private ShqApi ShqApi;

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		return toPage(request);
	}
	@RequestMapping("/test")
	public ModelAndView test(HttpServletRequest request) {
		return toPage(request);
	}
	@RequestMapping("/treetest")
	public ModelAndView treetest(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping("/add")
	public ModelAndView add(HttpServletRequest request) {
		return toPage(request);
	}

	@RequestMapping("/data")
	public String data(HttpServletRequest request) {
		String date="{\r\n" + 
				"  \"code\": 0,\r\n" + 
				"  \"msg\": \"ok\",\r\n" + 
				"  \"data\": [\r\n" + 
				"    {\r\n" + 
				"      \"id\": 1,\r\n" + 
				"      \"name\": \"xx\",\r\n" + 
				"      \"sex\": \"male\",\r\n" + 
				"      \"pid\": -1\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"id\": 2,\r\n" + 
				"      \"name\": \"xx\",\r\n" + 
				"      \"sex\": \"male\",\r\n" + 
				"      \"pid\": 1\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"id\": 3,\r\n" + 
				"      \"name\": \"xx\",\r\n" + 
				"      \"sex\": \"male\",\r\n" + 
				"      \"pid\": 1\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"id\": 4,\r\n" + 
				"      \"name\": \"xx\",\r\n" + 
				"      \"sex\": \"male\",\r\n" + 
				"      \"pid\": 1\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"id\": 5,\r\n" + 
				"      \"name\": \"xx\",\r\n" + 
				"      \"sex\": \"male\",\r\n" + 
				"      \"pid\": -1\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"id\": 6,\r\n" + 
				"      \"name\": \"xx\",\r\n" + 
				"      \"sex\": \"male\",\r\n" + 
				"      \"pid\": 5\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"id\": 7,\r\n" + 
				"      \"name\": \"xx\",\r\n" + 
				"      \"sex\": \"male\",\r\n" + 
				"      \"pid\": 5\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"id\": 8,\r\n" + 
				"      \"name\": \"xx\",\r\n" + 
				"      \"sex\": \"male\",\r\n" + 
				"      \"pid\": 6\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"id\": 9,\r\n" + 
				"      \"name\": \"xx\",\r\n" + 
				"      \"sex\": \"male\",\r\n" + 
				"      \"pid\": 6\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"id\": 10,\r\n" + 
				"      \"name\": \"xx\",\r\n" + 
				"      \"sex\": \"male\",\r\n" + 
				"      \"pid\": 9\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"id\": 11,\r\n" + 
				"      \"name\": \"xx\",\r\n" + 
				"      \"sex\": \"male\",\r\n" + 
				"      \"pid\": 9\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"count\": 11\r\n" + 
				"}";
		return date;
	}
	
	@RequestMapping("/details")
	public ModelAndView details(HttpServletRequest request, OrderEvaluate orderEvaluate) {

		OrderEvaluate orderEvaluateResult = orderEvaluateService.selectOne(orderEvaluate);
		OrderEvaluateLikes tmp = new OrderEvaluateLikes();
		tmp.setOrderEvaluateId(orderEvaluateResult.getId());
		// 点赞数
		int likesCounts = orderEvaluateLikesService.queryLikesCount(tmp);
		// 评论图片
		OrderEvaluateImgs tmpOrderEvaluateImgs = new OrderEvaluateImgs();
		tmpOrderEvaluateImgs.setOrderEvaluateId(orderEvaluateResult.getId());
		List<OrderEvaluateImgs> imgsResult = orderEvaluateImgsService.select(tmpOrderEvaluateImgs);

		//查询商品
		DistributionProduct tmpProduct=new DistributionProduct();
		tmpProduct.setProductCode(orderEvaluateResult.getGoodsCode());
		DistributionProduct product = new DistributionProduct();
		List<DistributionProduct> distributionProductList = distributionProductMapper.select(tmpProduct);
		if(distributionProductList.size()>0) {
			 product =distributionProductList.get(0);
		}
		 boolean productFlag=true;
		 if(StringUtils.isEmpty(product)) {
			 productFlag=false;
		 }
		// 查询订单
		List<MongoDistributionOrders> MongoDistributionOrdersList = new ArrayList<>();
		 MongoDistributionOrders tmpOrdersDtl = new MongoDistributionOrders();
		 Query query=new Query();
		 if(!StringUtils.isEmpty(orderEvaluateResult.getOrderNo())) {
			 query.addCriteria(Criteria.where("orderno").is(orderEvaluateResult.getOrderNo()));
			 PageInfo<MongoDistributionOrders> info = mongoDistributionOrdersDao.findByQueryPage(query, new PageDto(), tmpOrdersDtl);
			 MongoDistributionOrdersList=info.getList();
		 }
	
		// 查询商品价格与名称
		 OrderDetail distributionOrdersDtlResult=new OrderDetail();
		 if(MongoDistributionOrdersList.size()>0) {
			 MongoDistributionOrders distributionOrders = MongoDistributionOrdersList.get(0);
			 if(distributionOrders.getListOrderDetails().size()>0) {
				 for (OrderDetail orderDetail : distributionOrders.getListOrderDetails()) {
					 if(!StringUtils.isEmpty(orderDetail.getOrderSku())&&!StringUtils.isEmpty(orderDetail.getGoodsCode())
						&&!StringUtils.isEmpty(orderEvaluateResult.getGoodsSku())&&!StringUtils.isEmpty(orderEvaluateResult.getGoodsCode())) {
								if(orderEvaluateResult.getGoodsSku().equals(orderDetail.getOrderSku())
										&&orderEvaluateResult.getGoodsCode().equals(orderDetail.getGoodsCode())) {
										distributionOrdersDtlResult=orderDetail;
									} 
					 }
				}
			 }
		 }
		String imgPath = "";
		if(!StringUtils.isEmpty(distributionOrdersDtlResult.getGoodsImgUrl())) {
			imgPath=distributionOrdersDtlResult.getGoodsImgUrl();
		}
		
		// 查询评价用户
		Shopper tmpShopper = new Shopper();
		Shopper shopperResult = new Shopper();
		if (!isNumeric(orderEvaluateResult.getUserSeq().trim())) {
			shopperResult = null;
		} else {
			tmpShopper.setSeq(Integer.parseInt(orderEvaluateResult.getUserSeq()));
			shopperResult = shopperMapper.selectOne(tmpShopper);
		}
		if(!StringUtils.isEmpty(orderEvaluateResult.getEvaluateContent())) {
			orderEvaluateResult.setEvaluateContent(orderEvaluateResult.getEvaluateContent().replaceAll("\n","\\\\n"));
		}
		request.setAttribute("orderEvaluateResult", orderEvaluateResult);
		request.setAttribute("imgsResult", imgsResult);
		request.setAttribute("likesCounts", likesCounts);
		request.setAttribute("distributionOrdersDtlResult", distributionOrdersDtlResult);
		request.setAttribute("MongoDistributionOrdersList", MongoDistributionOrdersList);
		request.setAttribute("shopperResult", shopperResult);
		request.setAttribute("imgPath", imgPath);
		request.setAttribute("product", product);
		request.setAttribute("productFlag", productFlag);
		return toPage(request);
	}

	@RequestMapping("/reply")
	public ModelAndView reply(HttpServletRequest request, OrderEvaluate orderEvaluate) {
		OrderEvaluate orderEvaluateResult = orderEvaluateService.selectOne(orderEvaluate);
		DistributionProduct tmpProduct = new DistributionProduct();
		tmpProduct.setProductCode(orderEvaluateResult.getGoodsCode());
		DistributionProduct product = distributionProductMapper.selectOne(tmpProduct);
		request.setAttribute("orderEvaluateResult", orderEvaluateResult);
		request.setAttribute("product", product);
		return toPage(request);
	}

	@RequestMapping("/save")
	public @ResponseBody Msg<String> save(HttpServletRequest request, OrderEvaluate orderEvaluate) {
		orderEvaluate.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		orderEvaluate.setUpdateTime(LocalDateTime.now());
		Msg<String> result = orderEvaluateService.save(orderEvaluate);
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
	public Data<OrderEvaluate> list(OrderEvaluate entity,String mobile, PageDto page) {
		if(!StringUtils.isEmpty(mobile)) {
			Shopper tmp=new Shopper();
			tmp.setMobile(mobile);
			Shopper selectOne = shopperMapper.selectOne(tmp);
			if(!StringUtils.isEmpty(selectOne)) {
				if(!StringUtils.isEmpty(selectOne.getSeq())) {
					entity.setUserSeq(selectOne.getSeq().toString());
				}
			}else {
				entity.setUserSeq(mobile);
			}
		}
		Page<OrderEvaluate> entitys = orderEvaluateService.queryOrderEvaluateOrderByIsTop(entity,page);
		return Data.ok(entitys);
	}

	@RequestMapping(value = "/examine")
	public Msg<String> examine(OrderEvaluate orderEvaluate, String orderEvaluateIds, HttpServletRequest request) {
		LocalDateTime dtNow = LocalDateTime.now();
		orderEvaluate.setAuditAdmin(this.getCreateOrUpdateAdminName(request));
		orderEvaluate.setAuditTime(dtNow);
		Msg<String> msg = orderEvaluateService.orderEvaluateExamine(orderEvaluateIds, orderEvaluate);

		return msg;
	}
	@RequestMapping("/unExamineOrWaiteForExamine")
	public @ResponseBody Msg<String> unExamine(HttpServletRequest request, OrderEvaluate orderEvaluate) {
		OrderEvaluate result;
		try {
			OrderEvaluate tmp = new OrderEvaluate();
			tmp.setId(orderEvaluate.getId());
			result = orderEvaluateService.selectOne(tmp);
			if (StringUtils.isEmpty(result)) {
				return Msg.error("编号为[" + orderEvaluate.getId() + "]的评价订单不存在");
			}
			//将该评论设置    auditStats:3 审核不通过,0:待审核
			result.setAuditStats(orderEvaluate.getAuditStats());
			result.setUpdateTime(LocalDateTime.now());
			result.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
			if(orderEvaluate.getAuditStats()==3) {
				result.setAuditDesc(orderEvaluate.getAuditDesc());
				result.setAuditAdmin(this.getCreateOrUpdateAdminName(request));
				result.setAuditTime(LocalDateTime.now());
				orderEvaluateService.update(result);
			}else {
				result.setAuditAdmin(null);
				result.setAuditTime(null);
				result.setAuditDesc(null);
				orderEvaluateService.updateOrderEvaluateAuditStats(result);
			}
			return Msg.ok("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Msg.error("后台系统有误，请联系管理员");
		}

	}


	@RequestMapping(value = "/orderExamine")
	public Msg<String> orderExamine(OrderEvaluate orderEvaluate, HttpServletRequest request) {
		LocalDateTime dtNow = LocalDateTime.now();
		orderEvaluate.setAuditAdmin(this.getCreateOrUpdateAdminName(request));
		orderEvaluate.setAuditTime(dtNow);
		Msg<String> msg = orderEvaluateService.orderEvaluateExamineByOne(orderEvaluate);
		return msg;
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
	
	@RequestMapping("/queryOrderEvaluateImgs")
	public Msg<List<OrderEvaluateImgs>> queryOrderEvaluateImgs(HttpServletRequest request, String orderEvaluateId) {
		OrderEvaluateImgs entity=new OrderEvaluateImgs();
		entity.setOrderEvaluateId(Long.parseLong(orderEvaluateId));
		List<OrderEvaluateImgs> result = orderEvaluateImgsService.select(entity);
		if(result.size()>0) {
			return Msg.ok(result);
		}else {
			return Msg.error("", null);
		}
	}
	
	@RequestMapping("/delete")
	public Msg<String> delete(HttpServletRequest request, OrderEvaluate orderEvaluate) {
		if(StringUtils.isEmpty(orderEvaluate.getId())) {
			return Msg.error("数据有误,请稍后重试");
		}
		Msg<String>result=orderEvaluateService.deleteOrderEvaluateAndOrderEvaluateImg(orderEvaluate);
		return Msg.ok("操作成功");
	}
	
	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

}