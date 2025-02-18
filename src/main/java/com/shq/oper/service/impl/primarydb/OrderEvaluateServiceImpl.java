package com.shq.oper.service.impl.primarydb;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

import com.shq.oper.model.domain.mongo.HomePageBanner;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.OrderEvaluateImgsMapper;
import com.shq.oper.mapper.primarydb.OrderEvaluateMapper;
import com.shq.oper.mapper.shq520new.DistributionOrdersMapper;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.OrderEvaluate;
import com.shq.oper.model.domain.primarydb.OrderEvaluateImgs;
import com.shq.oper.model.domain.shq520new.DistributionOrders;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.req.ReqOrderEvaluateAddDto;
import com.shq.oper.model.dto.api.req.ReqOrderEvaluateListDataDto;
import com.shq.oper.model.dto.api.res.ResOrderEvaluateListDataDto;
import com.shq.oper.service.primarydb.OrderEvaluateService;
import com.shq.oper.service.primarydb.ShopperService;
import com.shq.oper.util.Constant;

import lombok.extern.slf4j.Slf4j;

@Service("orderEvaluateService")
@Slf4j
public class OrderEvaluateServiceImpl extends BaseServiceImpl<OrderEvaluate, Long> implements OrderEvaluateService {

	@Autowired
	private OrderEvaluateMapper orderEvaluateMapper;
	@Autowired
	private OrderEvaluateImgsMapper orderEvaluateImgsMapper;
	@Autowired
	private DistributionProductMapper distributionProductMapper;
	@Autowired
	private ShopperMapper shopperMapper;
	@Autowired
	private DistributionOrdersMapper distributionOrdersMapper;
	@Autowired
	private ShopperService shopperService;

	@Override
	public Msg<String> save(OrderEvaluate orderEvaluate) {
		try {
			OrderEvaluateImgs imgs = new OrderEvaluateImgs();
			orderEvaluateMapper.insertSelective(orderEvaluate);
			// 给订单评论添加一个默认的评论图片，以便测试使用
			imgs.setImgUrl("http://pics.sc.chinaz.com/files/pic/pic9/201702/zzpic1371.jpg");
			imgs.setOrderEvaluateId(orderEvaluate.getId());
			orderEvaluateImgsMapper.insertSelective(imgs);
			return Msg.ok("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Msg.error("后台服务器错误");
		}
	}

	@Override
	public Msg<String> orderEvaluateExamine(String orderEvaluateIds, OrderEvaluate orderEvaluate) {
		List<OrderEvaluate> orderEvaluateList = orderEvaluateMapper.selectByIds(orderEvaluateIds);
		// 商品code为空的评价订单集合
		List<String> errorCodeNums = new ArrayList<>();
		// 商品code为空的评价订单集合
		List<String> errorSeqNums = new ArrayList<>();
		// 商品code为空的评价订单集合
		List<String> errorOrderNoNums = new ArrayList<>();
		// 商品code Map
		Map<String, String> goodsCodeMap = new HashMap<>();
		// 用户 Map
		Map<String, String> userSeqMap = new HashMap<>();
		// 商品code Map
		Map<String, String> orderNoMap = new HashMap<>();
		for (OrderEvaluate tmpOrderEvaluate : orderEvaluateList) {
			if (StringUtils.isEmpty(tmpOrderEvaluate.getGoodsCode())) {
				errorCodeNums.add(tmpOrderEvaluate.getId().toString());
			} else {
				goodsCodeMap.put(tmpOrderEvaluate.getId().toString(), tmpOrderEvaluate.getGoodsCode());
				tmpOrderEvaluate.setAuditStats((short) 1);
				tmpOrderEvaluate.setAuditAdmin(orderEvaluate.getAuditAdmin());
				tmpOrderEvaluate.setAuditTime(orderEvaluate.getAuditTime());
				tmpOrderEvaluate.setAuditDesc(orderEvaluate.getAuditDesc());
			}
		}
		if (goodsCodeMap.size() > 0) {
			List<DistributionProduct> resultList = new ArrayList<>();
			for (Entry<String, String> entry : goodsCodeMap.entrySet()) {
				String key = entry.getKey();
				DistributionProduct tmp = new DistributionProduct();
				tmp.setProductCode(entry.getValue());
				DistributionProduct selectOne = distributionProductMapper.selectOne(tmp);
				if (StringUtils.isEmpty(selectOne)) {
					errorCodeNums.add(key);
				}
			}
		}
		// 是否存在商品code为空或者无此商品code的商品的评价订单
		if (errorCodeNums.size() > 0) {
			String errorStr = errorCodeNums.get(0);
			for (int i = 1; i < errorCodeNums.size(); i++) {
				errorStr = errorStr + "," + errorCodeNums.get(i);
			}
			return Msg.error("审核失败," + "编号为[" + errorStr + "]的评价订单中的商品不存在");
		}
		try {
			orderEvaluateMapper.orderEvaluateExamine(orderEvaluateList);
			return Msg.ok("审核成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Msg.error("审核失败");
		}
	}

	@Transactional
	@Override
	public Msg<String> addOrderEvaluateByApi(ReqOrderEvaluateAddDto reqOrderEvaluateAddDto) {

		LocalDateTime dtNow = LocalDateTime.now();
		// 判断是否是追加评论
		if (!StringUtils.isEmpty(reqOrderEvaluateAddDto.getId())) {
			OrderEvaluate tmpOrderEvaluate = new OrderEvaluate();
			tmpOrderEvaluate.setId(Long.parseLong(reqOrderEvaluateAddDto.getId()));
			OrderEvaluate tmpOne = orderEvaluateMapper.selectOne(tmpOrderEvaluate);
			if (StringUtils.isEmpty(tmpOne)) {
				return Msg.error("该评论不存在，无法追加评价", "");
			}
			tmpOne.setAppendEvaluateId(tmpOne.getId());
			tmpOne.setUpdateTime(dtNow);
			tmpOne.setEvaluateTime(dtNow);
			tmpOne.setEvaluatieType("0");
			// 设置追加评论内容
			tmpOne.setEvaluateContent(reqOrderEvaluateAddDto.getEvaluateContent());
			tmpOne.setId(null);
			int insertSelective = orderEvaluateMapper.insertSelective(tmpOne);
			if (!StringUtils.isEmpty(reqOrderEvaluateAddDto.getImgsPathList())
					&& reqOrderEvaluateAddDto.getImgsPathList().size() > 0) {
				List<String> imgsPathList = reqOrderEvaluateAddDto.getImgsPathList();
				List<OrderEvaluateImgs> recordList = new ArrayList<>();
				for (String str : imgsPathList) {
					OrderEvaluateImgs tmp = new OrderEvaluateImgs();
					tmp.setOrderEvaluateId(tmpOne.getId());
					tmp.setImgUrl(str);
					recordList.add(tmp);
				}
				orderEvaluateImgsMapper.insertList(recordList);
			}
			return Msg.ok("操作成功", "");
		}
		// 验证数据完整性
		if ("null".equals(reqOrderEvaluateAddDto.getEvaluateContent())||StringUtils.isEmpty(reqOrderEvaluateAddDto.getEvaluateContent())) {
			return Msg.error("评价内容不能为空");
		}
		if ("null".equals(reqOrderEvaluateAddDto.getOrderNo())||StringUtils.isEmpty(reqOrderEvaluateAddDto.getOrderNo())) {
			return Msg.error("评价订单编号不能为空");
		}
		if ("null".equals(reqOrderEvaluateAddDto.getGoodsCode())||StringUtils.isEmpty(reqOrderEvaluateAddDto.getGoodsCode())) {
			return Msg.error("评价订单商品货号CODE不能为空");
		}
		if ("null".equals(reqOrderEvaluateAddDto.getUserSeq())||StringUtils.isEmpty(reqOrderEvaluateAddDto.getUserSeq())) {
			return Msg.error("评价订单用户SEQ不能为空");
		}
		if ("null".equals(reqOrderEvaluateAddDto.getSku())||StringUtils.isEmpty(reqOrderEvaluateAddDto.getSku())) {
			return Msg.error("商品sku不能为空");
		}
		if (!reqOrderEvaluateAddDto.getUserSeq().matches("^[0-9]*$") ) {
			return Msg.error("用户SEQ有误。");
		}
		OrderEvaluate orderEvaluate = new OrderEvaluate();
		orderEvaluate.setOrderNo(reqOrderEvaluateAddDto.getOrderNo());
		orderEvaluate.setUserSeq(reqOrderEvaluateAddDto.getUserSeq());
		orderEvaluate.setGoodsCode(reqOrderEvaluateAddDto.getGoodsCode());
		orderEvaluate.setGoodsSku(reqOrderEvaluateAddDto.getSku());
		List<OrderEvaluate> select = orderEvaluateMapper.select(orderEvaluate);
		if (select.size() > 0) {
			log.info("===重复添加orderNo:" + reqOrderEvaluateAddDto.getOrderNo() + " goodsCode:"
					+ reqOrderEvaluateAddDto.getGoodsCode() + "userSeq:" + reqOrderEvaluateAddDto.getUserSeq()
					+ "的评论数据");
			return Msg.error("您已经对[" + reqOrderEvaluateAddDto.getOrderNo() + "]订单的"+"商品SKU["+reqOrderEvaluateAddDto.getSku()+"]评价过了，请勿重新评价");
		}

		orderEvaluate.setUpdateTime(dtNow);
		orderEvaluate.setEvaluateContent(reqOrderEvaluateAddDto.getEvaluateContent());
		orderEvaluate.setEvaluateTime(dtNow);
		orderEvaluate.setEvaluateLev(reqOrderEvaluateAddDto.getEvaluateLev());
		orderEvaluate.setIsAnonymous(reqOrderEvaluateAddDto.getIsAnonymous());
		orderEvaluate.setIsTop(reqOrderEvaluateAddDto.getIsTop());
		orderEvaluate.setUpdateAdmin(reqOrderEvaluateAddDto.getUserSeq());

		if (StringUtils.isEmpty(reqOrderEvaluateAddDto.getImgsPathList())
				|| reqOrderEvaluateAddDto.getImgsPathList().size() <= 0) {
			orderEvaluateMapper.insertSelective(orderEvaluate);
			return Msg.ok("操作成功", "");
		} else {
			orderEvaluateMapper.insertSelective(orderEvaluate);
			List<String> imgsPathList = reqOrderEvaluateAddDto.getImgsPathList();
			List<OrderEvaluateImgs> recordList = new ArrayList<>();
			for (String str : imgsPathList) {
				OrderEvaluateImgs tmp = new OrderEvaluateImgs();
				tmp.setOrderEvaluateId(orderEvaluate.getId());
				tmp.setImgUrl(str);
				recordList.add(tmp);
			}
			orderEvaluateImgsMapper.insertList(recordList);
			return Msg.ok("操作成功", "");
		}
	}

	
	@Cacheable(value = Constant.CACHEKEY_SECOND_15, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ORDER_EVALUATE +#orderEvaluateListDataDto", unless = "#result eq null")
	@Override
	public Page<ResOrderEvaluateListDataDto> queryOrderEvaluateListByOrderNo(
			ReqOrderEvaluateListDataDto orderEvaluateListDataDto, PageDto pageDto) {
		
		Page<ResOrderEvaluateListDataDto> queryOrderEvaluateListDataList = new Page<>();
		// 判断是否传入auditType，没有则默认查询已审核评论
		//auditType=1 查询所有订单(不包括被禁用的测试评价订单) ,auditStats="0" and auditStats="1" and auditStats="3";
		if ("1".equals(orderEvaluateListDataDto.getAuditType())) {
			orderEvaluateListDataDto.setAuditStats("2");
		}
		// auditType=2查询未审核通过(审核状态为0)订单
		else if ("2".equals(orderEvaluateListDataDto.getAuditType())) {
			orderEvaluateListDataDto.setAuditStats("0");
		}
		// auditType=0默认审核已通过订单(审核状态为1)
		else if ("0".equals(orderEvaluateListDataDto.getAuditType())) {
			orderEvaluateListDataDto.setAuditStats("1");
		}
		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		queryOrderEvaluateListDataList = orderEvaluateMapper.queryOrderEvaluateListData(orderEvaluateListDataDto);
		if (queryOrderEvaluateListDataList.getResult().size() > 0) {
			for (ResOrderEvaluateListDataDto resOrderEvaluateListDataDto : queryOrderEvaluateListDataList.getResult()) {
				OrderEvaluateImgs tmpImgs = new OrderEvaluateImgs();
				tmpImgs.setOrderEvaluateId(Long.parseLong(resOrderEvaluateListDataDto.getId()));
				List<OrderEvaluateImgs> imgsList = orderEvaluateImgsMapper.select(tmpImgs);
				resOrderEvaluateListDataDto.setImgsList(imgsList);
				//
				String tmpStr = "";
				// 判断是否匿名
				Short isAnonymous = resOrderEvaluateListDataDto.getIsAnonymous();
				if (isAnonymous == 1) {// 匿名
					if (!StringUtils.isEmpty(resOrderEvaluateListDataDto.getUserSeq())) {
						tmpStr = "游客-" + Calendar.getInstance().get(Calendar.YEAR) + "-"
								+ resOrderEvaluateListDataDto.getUserSeq();
						resOrderEvaluateListDataDto.setMobile(tmpStr);
						resOrderEvaluateListDataDto.setNickName(tmpStr);	
					}
				} else if (isAnonymous == 0) {
					if (!StringUtils.isEmpty(resOrderEvaluateListDataDto.getUserSeq())) {
						Shopper tmpShopper = new Shopper();
						tmpShopper.setSeq(Integer.parseInt(resOrderEvaluateListDataDto.getUserSeq()));
						Shopper shopper = shopperMapper.queryBySeqOrMobile(tmpShopper);
						if (!StringUtils.isEmpty(shopper)) {
							if (!StringUtils.isEmpty(shopper.getMobile()) && shopper.getMobile().length() <= 7) {
								tmpStr = shopper.getMobile().replaceAll("\\d", "*");
								resOrderEvaluateListDataDto.setMobile(shopper.getMobile());
								resOrderEvaluateListDataDto.setNickName(tmpStr);
							} else if (!StringUtils.isEmpty(shopper.getMobile()) && shopper.getMobile().length() > 7) {
								tmpStr = shopper.getMobile().replaceAll("(?<=[\\d]{3})\\d(?=[\\d]{4})", "*");
								resOrderEvaluateListDataDto.setMobile(shopper.getMobile());
								resOrderEvaluateListDataDto.setNickName(tmpStr);
							}
						}
					}
				}
			}

			Collections.sort(queryOrderEvaluateListDataList, new Comparator<ResOrderEvaluateListDataDto>() {
				@Override
				public int compare(ResOrderEvaluateListDataDto o2, ResOrderEvaluateListDataDto o1) {
					return -o2.getEvaluateTime().compareTo(o1.getEvaluateTime());
				}
			});
			return queryOrderEvaluateListDataList;
		} else {
			return new Page<>();
		}
	}

	/**
	 * 单条评价订单审核
	 */
	@Override
	public Msg<String> orderEvaluateExamineByOne(OrderEvaluate orderEvaluate) {
		OrderEvaluate tmp = new OrderEvaluate();
		tmp.setId(orderEvaluate.getId());
		OrderEvaluate orderEvaluateResult = orderEvaluateMapper.selectOne(tmp);

		if (!StringUtils.isEmpty(orderEvaluateResult)) {
			// 验证数据完整性
			if (StringUtils.isEmpty(orderEvaluateResult.getEvaluateContent())) {
				return Msg.error("评价内容不能为空");
			}
			if (!StringUtils.isEmpty(orderEvaluateResult.getUserSeq())) {
				boolean flag = isNumeric(orderEvaluateResult.getUserSeq().trim());
				if (!flag) {
					return Msg.error("审核失败,评价用户SEQ必须是纯数字,评价订单[" + orderEvaluate.getId() + "]的评价用户SEQ["
							+ orderEvaluateResult.getUserSeq() + "]为非法字符串");
				}
				Shopper tmpShopper = new Shopper();
				tmpShopper.setSeq(Integer.parseInt(orderEvaluateResult.getUserSeq()));
				Shopper selectOne = shopperMapper.selectOne(tmpShopper);
				if (StringUtils.isEmpty(selectOne)) {
					return Msg.error("审核失败,评价SEQ为[" + orderEvaluate.getUserSeq() + "]的用户不存在" + ",该评价订单编号为["
							+ orderEvaluate.getId() + "]");
				}
			} else {
				return Msg.error("审核失败,编号为[" + orderEvaluate.getId() + "]的评价用户SEQ为空");
			}
			if (!StringUtils.isEmpty(orderEvaluateResult.getOrderNo())) {
				DistributionOrders tmpOrders = new DistributionOrders();
				tmpOrders.setOrderno(orderEvaluateResult.getOrderNo());
				DistributionOrders ordersResult = distributionOrdersMapper.selectOne(tmpOrders);
				if (StringUtils.isEmpty(ordersResult)) {
					return Msg.error("审核失败,不存在订单编号为[" + orderEvaluateResult.getOrderNo() + "]的订单" + ",该评价订单编号为["
							+ orderEvaluate.getId() + "]");
				}
			} else {
				return Msg.error("审核失败,编号为[" + orderEvaluate.getId() + "]的订单编号为空");
			}
			if (!StringUtils.isEmpty(orderEvaluateResult.getGoodsCode())) {
				DistributionProduct tmpProduct = new DistributionProduct();
				tmpProduct.setProductCode(orderEvaluateResult.getGoodsCode());
				DistributionProduct productResult = distributionProductMapper.selectOne(tmpProduct);
				if (StringUtils.isEmpty(productResult)) {
					return Msg.error("审核失败,不存在商品CODE为[" + orderEvaluate.getUserSeq() + "]的商品" + ",该评价订单编号为["
							+ orderEvaluate.getId() + "]");
				}
			} else {
				return Msg.error("审核失败,编号为[" + orderEvaluate.getId() + "]的订单编号不存在");
			}
		} else {
			return Msg.error("审核失败,编号为[" + orderEvaluate.getId() + "]的评价订单不存在");
		}
		try {
			orderEvaluateResult.setAuditTime(LocalDateTime.now());
			orderEvaluateResult.setUpdateAdmin(orderEvaluate.getAuditAdmin());
			orderEvaluateResult.setAuditAdmin(orderEvaluate.getAuditAdmin());
			orderEvaluateResult.setAuditDesc(orderEvaluate.getAuditDesc());
			orderEvaluateResult.setAuditStats((short) 1);
			orderEvaluateMapper.updateByPrimaryKeySelective(orderEvaluateResult);
			return Msg.ok("审核通过并保存成功");
		} catch (Exception e) {
			return Msg.error("保存失败");
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

	@Transactional
	@Override
	public Page<OrderEvaluate> queryOrderEvaluateOrderByIsTop(OrderEvaluate entity,PageDto page) {
		PageHelper.startPage(page.getPage(), page.getLimit());
		Page<OrderEvaluate> result = orderEvaluateMapper.queryOrderEvaluateOrderByIsTop(entity);
		List<OrderEvaluate> resultList = result.getResult();
		Set<String>set=new HashSet<>();
		Map<String, Shopper> shopperMap =new HashMap<>();
		if(resultList.size()>0) {
			for (OrderEvaluate orderEvaluate : resultList) {
				set.add(orderEvaluate.getUserSeq());
			}
		}
	
		if(set.size()>0) {
			shopperMap = shopperService.queryShopperBySeqSet(set);
		}
		if(shopperMap.size()>0) {
			for (OrderEvaluate orderEvaluate : result.getResult()) {
				Shopper shopper = shopperMap.get(orderEvaluate.getUserSeq());
				if(!StringUtils.isEmpty(shopper)) {
					if(!StringUtils.isEmpty(shopper.getMobile())) {
						orderEvaluate.setSelMobile(shopper.getMobile());
					}
				}
			}
		}
		return result;
	}

	@Transactional
	@Override
	public Msg<String> manualEvaluateSave(OrderEvaluate orderEvaluate) {
		//最后编辑的人就是最终审核人
		orderEvaluate.setAuditAdmin(orderEvaluate.getUpdateAdmin());
		List<String> evaluatieImgsList = removeNull(orderEvaluate.getEvaluatieImgs());
		List<OrderEvaluateImgs> evaluatieImgsEntityList=new ArrayList<>(); 
		if(evaluatieImgsList.size()>4) {
			return Msg.error("评论图片不能超过四张");
		}
		
		if(StringUtils.isEmpty(orderEvaluate.getId())) {
			orderEvaluate.setAuditStats((short)1);
			orderEvaluate.setAuditTime(LocalDateTime.now());
			orderEvaluateMapper.insertSelective(orderEvaluate);
			if(evaluatieImgsList.size()>0) {
				for (String evaluatieImg : evaluatieImgsList) {
					OrderEvaluateImgs imgs = new OrderEvaluateImgs();
					imgs.setImgUrl(evaluatieImg);
					imgs.setOrderEvaluateId(orderEvaluate.getId());
					evaluatieImgsEntityList.add(imgs);
				}
				if(evaluatieImgsEntityList.size()>0) {
					Collections.reverse(evaluatieImgsEntityList);
					orderEvaluateImgsMapper.insertList(evaluatieImgsEntityList);
				}
			}
		}else {
			orderEvaluateMapper.updateByPrimaryKeySelective(orderEvaluate);
			//清空该评论的所有评论图片
			OrderEvaluateImgs imgs = new OrderEvaluateImgs();
			imgs.setOrderEvaluateId(orderEvaluate.getId());
			orderEvaluateImgsMapper.delete(imgs);
			if(evaluatieImgsList.size()>0) {
				//重新插入评论图片
				if(evaluatieImgsList.size()>0) {
					for (String evaluatieImg : evaluatieImgsList) {
						OrderEvaluateImgs imgsTmp = new OrderEvaluateImgs();
						imgsTmp.setImgUrl(evaluatieImg);
						imgsTmp.setOrderEvaluateId(orderEvaluate.getId());
						evaluatieImgsEntityList.add(imgsTmp);
					}
					if(evaluatieImgsEntityList.size()>0) {
						Collections.reverse(evaluatieImgsEntityList);
						orderEvaluateImgsMapper.insertList(evaluatieImgsEntityList);
					}
				}
				
			}
			return Msg.ok("保存成功");	
		}
		return Msg.ok("保存成功");	
	}
	
	@Transactional
	@Override
	public Page<OrderEvaluate> queryManualEvaluateList(OrderEvaluate entity) {
		return orderEvaluateMapper.queryManualEvaluateList(entity);
	}

	@Transactional
	@Override
	public Msg<String> deleteOrderEvaluateAndOrderEvaluateImg(OrderEvaluate orderEvaluate) {
		orderEvaluateMapper.delete(orderEvaluate);
		OrderEvaluateImgs imgs = new OrderEvaluateImgs();
		imgs.setOrderEvaluateId(orderEvaluate.getId());
		orderEvaluateImgsMapper.delete(imgs);
		return Msg.ok("操作成功");
	}
	public static <T> List<T> removeNull(List<? extends T> oldList) {

	    oldList.removeAll(Collections.singleton(null)); 
	    return (List<T>) oldList;  
	}

	@Override
	public int updateOrderEvaluateAuditStats(OrderEvaluate result) {
		return orderEvaluateMapper.updateOrderEvaluateAuditStats(result);
	}
}