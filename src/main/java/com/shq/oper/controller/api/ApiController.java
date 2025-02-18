package com.shq.oper.controller.api;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.shq.oper.dao.mongo.ApiRequestDataLogMongo;
import com.shq.oper.enums.CouponsStatusEnum;
import com.shq.oper.enums.CouponsTypeEnum;
import com.shq.oper.enums.api.*;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.DeductibleIntroduceDetailMapper;
import com.shq.oper.mapper.shq520new.DistributionOrdersMapper;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.ApiRequestDataLog;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.Activity;
import com.shq.oper.model.domain.primarydb.Coupons;
import com.shq.oper.model.domain.primarydb.deductible.Deductible;
import com.shq.oper.model.domain.primarydb.deductible.DeductibleIntroduce;
import com.shq.oper.model.domain.primarydb.deductible.DeductibleIntroduceDetail;
import com.shq.oper.model.domain.shq520new.DistributionOrders;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.BaseBrandActivityCouponsParamDto;
import com.shq.oper.model.dto.api.BaseOrderEvaluateParamDto;
import com.shq.oper.model.dto.api.BaseReceiveParamDto;
import com.shq.oper.model.dto.api.BaseResponseResultDto;
import com.shq.oper.model.dto.api.req.*;
import com.shq.oper.model.dto.api.res.*;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.service.primarydb.*;
import com.shq.oper.util.*;
import com.shq.oper.util.DateUtils.DateFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "运营系统接口文档", tags = { "运营系统统一接口" })
@RestController
@Slf4j
@RequestMapping("/api/")
public class ApiController {

	@Autowired
	private ApiRequestDataLogMongo apiRequestDataLogMongo;

	@Autowired
	private CouponsUserService couponsUserService;

	@Autowired
	private OrderEvaluateService orderEvaluateService;

	@Autowired
	private OrderEvaluateLikesService orderEvaluateLikesService;

	@Autowired
	private CouponsService couponsService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ShopperMapper shopperMapper;

	@Autowired
	private DistributionProductMapper distributionProduct;
	@Autowired
	private DistributionOrdersMapper distributionOrdersMapper;

	@Autowired
	private SystemProperties systemProperties;

	@Autowired
	private GuestBrowsingLogService guestBrowsingLogService;

	@Autowired
	private DeductibleService deductibleService;

	@Autowired
	private DeductibleIntroduceService deductibleIntroduceService;

	@Autowired
	private DeductibleIntroduceDetailMapper deductibleIntroduceDetailMapper;
	
	@Autowired
	private MessageSendService messageSendService;

	private final static int PAGE_LIMIT_MAX = 50;


	
	/**
	 * @author ltg
	 * @date 2018/7/21 17:53
	 * @params:[request, params]
	 * @return: com.shq.oper.model.dto.api.BaseResponseResultDto
	 */
	@ApiOperation(value = "品牌广场活动优惠券接口", notes = "品牌广场活动优惠券接口", httpMethod = "POST") //
	@RequestMapping(value = "/brandApi", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody BaseResponseResultDto brandSquareActivityApi(HttpServletRequest request,
			@RequestBody BaseBrandActivityCouponsParamDto params) {

		String refIp = CommRequestUtil.getRemortIP(request);
		log.debug("===formIp===" + refIp + "===>" + MemoryViewUtil.showMemoryStr());
		BaseResponseResultDto resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.FAIL_CODE);
		ReqBrandActivityAddDto dto = null;

		try {
			dto = JsonUtils.parse(JsonUtils.toDefaultJson(params.getRequestData()), ReqBrandActivityAddDto.class);

			ActivityReceiveCodeEnums enumDtos = ActivityReceiveCodeEnums.getByCode(params.getCode());
			if (enumDtos == null) {
				resultDto.setMsg("请求参数Code[" + params.getCode() + "]有误");
				return resultDto;
			}

			switch (enumDtos) {
			case ACTIVITY_RECEIVE_CODE_ENUMS:

				Msg<String> msgBack = activityService.addBrandActivityByApi(dto);
				prosessReturnData(resultDto, msgBack);
				break;

			case COUPONS_RECEIVE_CODE_ENUMS:

				Msg<ResCouponsReturnDto> msgBack1 = activityService.addBrandActivityCouponTypeByApi(dto);
				prosessReturnData(resultDto, msgBack1);
				break;

			case GET_RECEIVE_CODE_ENUMS:
				ReqBrandActivityListDataDto reqDto = JsonUtils.parse(JsonUtils.toDefaultJson(params.getRequestData()),
						ReqBrandActivityListDataDto.class);

				if (StringUtil.isEmpty(reqDto.getFromSys())){
					resultDto.setMsg("请输入系统来源");
					break;
				}else {
					CouponsFromSysEnums fromSysEnums=CouponsFromSysEnums.getByCode(reqDto.getFromSys());
					if (fromSysEnums==null){
						resultDto.setMsg("系统来源["+dto.getFromSys()+"]不正确");
						break;
					}
				}
				if(StringUtil.isEmpty(dto.getFromSysCode())){
					resultDto.setMsg("请输入系统来源code");
					break;
				}
				if (StringUtils.isEmpty(reqDto.getPage())) {
					resultDto.setMsg("查询页码数不能为空");
					break;
				}
				if (StringUtils.isEmpty(reqDto.getLimit())) {
					resultDto.setMsg("查询页码大小不能为空");
					break;
				}
				if (reqDto.getLimit() > PAGE_LIMIT_MAX) {
					resultDto.setMsg("一次最多只能查询" + PAGE_LIMIT_MAX + "条记录");
					break;
				}

				PageDto pageDto = new PageDto(reqDto.getPage(), reqDto.getLimit());
				Page<ResBrandActivityListDto> pageResult = activityService.queryBrandListByApi(reqDto, pageDto);

				setReturn(resultDto, pageResult);
				break;

			case GET_COUPONS_CODE_ENUMS:
				ReqBrandActivityListDataDto reqActDto = JsonUtils
						.parse(JsonUtils.toDefaultJson(params.getRequestData()), ReqBrandActivityListDataDto.class);

				// if (StringUtils.isEmpty(reqActDto.getFromSys())){
				// resultDto.setMsg("系统来源不能为空");
				// break;
				// }
				// if (StringUtils.isEmpty(reqActDto.getFromSysCode())){
				// resultDto.setMsg("系统来源code不能为空");
				// break;
				// }
				if (StringUtils.isEmpty(reqActDto.getPage())) {
					resultDto.setMsg("查询页码数不能为空");
					break;
				}
				if (StringUtils.isEmpty(reqActDto.getLimit())) {
					resultDto.setMsg("查询页码大小不能为空");
					break;
				}
				if (reqActDto.getLimit() > PAGE_LIMIT_MAX) {
					resultDto.setMsg("一次最多只能查询" + PAGE_LIMIT_MAX + "条记录");
					break;
				}
				PageDto pageDto1 = new PageDto(reqActDto.getPage(), reqActDto.getLimit());
				Page<ResBrandCouponsListDto> coupResult = couponsService.queryCouponsBrandByActive(reqActDto, pageDto1);

				setReturn(resultDto, coupResult);
				break;

			case GET_COUPONS_GOODS_CODE_ENUMS:
				ReqGoodsListDataDto reqGoodsListDataDto = JsonUtils
						.parse(JsonUtils.toDefaultJson(params.getRequestData()), ReqGoodsListDataDto.class);
				// if (StringUtils.isEmpty(reqGoodsListDataDto.getFromSys())){
				// resultDto.setMsg("系统来源不能为空");
				// break;
				// }
				// if (StringUtils.isEmpty(reqGoodsListDataDto.getFromSysCode())){
				// resultDto.setMsg("系统来源code不能为空");
				// break;
				// }
				if (StringUtils.isEmpty(reqGoodsListDataDto.getId())) {
					resultDto.setMsg("优惠券id不能为空");
					break;
				}
				PageDto pageDto2 = new PageDto(reqGoodsListDataDto.getPage(), reqGoodsListDataDto.getLimit());
				Page<ResBrandCouponsGoodsListDto> goodsResult = couponsService.queryGoodsBrandByApi(reqGoodsListDataDto,
						pageDto2);
				setReturn(resultDto, goodsResult);
				break;

			case SAVE_COUPONS_CATEGORY_CODE_ENUMS:
				ReqBrandCouponsCategoryAddDto reqCoupCategoryAddDto = JsonUtils
						.parse(JsonUtils.toDefaultJson(params.getRequestData()), ReqBrandCouponsCategoryAddDto.class);
				if (StringUtils.isEmpty(reqCoupCategoryAddDto.getId())) {
					resultDto.setMsg("优惠券id不能为空");
					break;
				}

				if (StringUtil.isEmpty(reqCoupCategoryAddDto.getFromSys())){
					resultDto.setMsg("请输入系统来源");
					break;
				}else {
					CouponsFromSysEnums fromSysEnums=CouponsFromSysEnums.getByCode(reqCoupCategoryAddDto.getFromSys());
					if (fromSysEnums==null){
						resultDto.setMsg("系统来源["+dto.getFromSys()+"]不正确");
						break;
					}
				}
				if(StringUtil.isEmpty(dto.getFromSysCode())){
					resultDto.setMsg("请输入系统来源code");
					break;
				}

				if (StringUtils.isEmpty(reqCoupCategoryAddDto.getCodes())) {
					resultDto.setMsg("商品code不能为空");
					break;
				}
				Msg<String> msgBack3 = couponsService.BrandAddCategoryRule(reqCoupCategoryAddDto);
				prosessReturnData(resultDto, msgBack3);
				break;

			case UPDATE_COUPONS_CODE_ENUMS:
				Coupons coupons = JsonUtils.parse(JsonUtils.toDefaultJson(params.getRequestData()), Coupons.class);
				if (StringUtils.isEmpty(coupons.getId())) {
					resultDto.setMsg("优惠券id不能为空");
					break;
				}
				Msg<String> returnMsg = couponsService.BrandUpdateCoupons(coupons);
				prosessReturnData(resultDto, returnMsg);
				break;

			case UPDATE_ACTIVITY_CODE_ENUMS:
				Activity activity = JsonUtils.parse(JsonUtils.toDefaultJson(params.getRequestData()), Activity.class);
				if (StringUtils.isEmpty(activity.getId())) {
					resultDto.setMsg("活动id不能为空");
					break;
				}
				Msg<String> updateReturn = activityService.updateBrandActivity(activity);
				prosessReturnData(resultDto, updateReturn);
				break;

			case ENABLE_COUPONS_CODE_ENUMS:
				ReqBrandCouponsBatchEnbleDto reqEnbleDto = JsonUtils.parse(JsonUtils.toDefaultJson(params.getRequestData()), ReqBrandCouponsBatchEnbleDto.class);
				if (StringUtil.isEmpty(reqEnbleDto.getIds())) {
					resultDto.setMsg("优惠券id输入有误");
					break;
				}
				Msg<String> enblereturn = couponsService.BrandBatchEnbleCoupons(reqEnbleDto);
				prosessReturnData(resultDto, enblereturn);
				break;

			case REMOVE_COUPONS_CATEGORY_CODE_ENUMS:
				ReqRemoveGoodsListDataDto2010 dataDto2010 =JsonUtils.parse(JsonUtils.toDefaultJson(params.getRequestData()), ReqRemoveGoodsListDataDto2010.class);

				if (StringUtil.isEmpty(dataDto2010.getFromSys())){
					resultDto.setMsg("请输入系统来源");
					break;
				}else {
					CouponsFromSysEnums fromSysEnums=CouponsFromSysEnums.getByCode(dataDto2010.getFromSys());
					if (fromSysEnums==null){
						resultDto.setMsg("系统来源["+dto.getFromSys()+"]不正确");
						break;
					}
				}

				if (StringUtils.isEmpty(dataDto2010.getCouponsId())){
					resultDto.setMsg("请输入优惠券id");
					break;
				}

				if (dataDto2010.getCodes()==null||dataDto2010.getCodes().size()<=0){
					resultDto.setMsg("请输入商品code");
					break;
				}

				Msg<String> return2010 = couponsService.BrandBatchRemoveCouponsCateRule(dataDto2010);
				prosessReturnData(resultDto, return2010);
				break;

			case Add_COUPONS_BY_ACTIVITY_CODE_ENUMS:
				ReqBrandCouponsAddByActid2011Dto dataDto2011 =JsonUtils.parse(JsonUtils.toDefaultJson(params.getRequestData()), ReqBrandCouponsAddByActid2011Dto.class);

				if (StringUtil.isEmpty(dataDto2011.getActivityId())){
					resultDto.setMsg("请输入活动id");
					break;
				}

				if (StringUtil.isEmpty(dataDto2011.getFromSys())){
					resultDto.setMsg("请输入系统来源");
					break;
				}else {
					CouponsFromSysEnums fromSysEnums=CouponsFromSysEnums.getByCode(dataDto2011.getFromSys());
					if (fromSysEnums==null){
						resultDto.setMsg("系统来源["+dataDto2011.getFromSys()+"]不正确");
						break;
					}
				}
				if(StringUtil.isEmpty(dataDto2011.getFromSysCode())){
					resultDto.setMsg("请输入系统来源code");
					break;
				}

				Msg<ResCouponsReturnDto> returnDtoMsg=couponsService.BrandAddCouponsByActivityId(dataDto2011);
				prosessReturnData(resultDto, returnDtoMsg);

			}

			return resultDto;

		} catch (Exception e) {
			String err = "调用接口出错," + e.getLocalizedMessage();
			log.error(err, e);
			resultDto.setMsg(err);
		} finally {
			resultDto.setResponseTime(DateUtils.now());// 设置成功时间

			String action = request.getRequestURI();
			String referer = request.getHeader("Referer");

			ApiRequestDataLog apiLog = new ApiRequestDataLog();
			apiLog.setAction(action);
			apiLog.setRefAction(referer);
			apiLog.setRefIp(refIp);
			apiLog.setCreateTime(LocalDateTime.now());
			apiLog.setActionCode(params.getCode());
			apiLog.setActionSource(params.getSource());
			apiLog.setActionDeviceType(params.getDeviceType());
			apiLog.setActionPara(JsonUtils.toJson(params));
			apiLog.setReturnData(JsonUtils.toJson(resultDto));
			apiRequestDataLogMongo.insert(apiLog);
			if (!StringUtils.isEmpty(resultDto.getMsg()) && resultDto.getMsg().startsWith("调用接口出错,")) {
				resultDto.setMsg("获取后台信息服务出错[" + apiLog.getId() + "]");
			}
		}

		log.debug("---resultDto---" + params.getCode() + "==" + resultDto.getMsg());
		return resultDto;

	}

	@ApiOperation(value = "订单评价接口", notes = "订单评价接口", httpMethod = "POST")
	@RequestMapping(value = "/orderEvaluateApi", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody BaseResponseResultDto orderEvaluateApi(HttpServletRequest request,
			@RequestBody BaseOrderEvaluateParamDto params) {
		log.debug(MemoryViewUtil.showMemoryStr());
		String refIp = CommRequestUtil.getRemortIP(request);

		BaseResponseResultDto resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.FAIL_CODE);
		try {
			if (StringUtils.isEmpty(params)) {
				resultDto.setMsg("请求参数为空。");
				return resultDto;
			}
			log.debug("========后台日志==preHandle==log===[" + refIp + "]用户request来自：" + request.getRequestURI() + "==="
					+ params);

			BaseResponseResultDto valiDto = validOrderEvaluateParams(params);
			if (valiDto != null) {
				return valiDto;
			}
			OrderReceiveCodeEnums enumDtos = OrderReceiveCodeEnums.getByCode(params.getCode());
			if (enumDtos == null) {
				resultDto.setMsg("请求参数Code[" + params.getCode() + "]有误");
				return resultDto;
			}
			Object obj = params.getRequestData();
			switch (enumDtos) {
			case ORDER_EVALUATE_ADD:// 评价订单新增
				ReqOrderEvaluateAddDto reqOrderEvaluateAddDto = JsonUtils.parse(JsonUtils.toDefaultJson(obj),
						ReqOrderEvaluateAddDto.class);
				Msg<String> msg_3001 = orderEvaluateService.addOrderEvaluateByApi(reqOrderEvaluateAddDto);
				if (msg_3001.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
				}

				resultDto.setMsg(msg_3001.getMsg());
				break;
			case ORDER_EVALUATE_LIKES_ADD:// 评价订单点赞新增
				ReqOrderEvaluateLikesAddDto reqOrderEvaluateLikesAddDto = JsonUtils.parse(JsonUtils.toDefaultJson(obj),
						ReqOrderEvaluateLikesAddDto.class);
				Msg<String> msg_3002 = orderEvaluateLikesService
						.addOrderEvaluateLikesByApi(reqOrderEvaluateLikesAddDto);
				if (msg_3002.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
				}
				resultDto.setMsg(msg_3002.getMsg());
				break;
			case ORDER_EVALUATE_LIST:
				ReqOrderEvaluateListDataDto orderEvaluateListDataDto = JsonUtils.parse(JsonUtils.toDefaultJson(obj),
						ReqOrderEvaluateListDataDto.class);
				if (!StringUtils.isEmpty(orderEvaluateListDataDto.getAuditType())
					&&!"0".equals(orderEvaluateListDataDto.getAuditType())
					&&!"1".equals(orderEvaluateListDataDto.getAuditType())
					&&!"2".equals(orderEvaluateListDataDto.getAuditType())) {
						resultDto.setMsg("auditType[" + orderEvaluateListDataDto.getAuditType() + "]值无效,auditType可选值为【0,1,2】或为空");
						break;
				}
/*				if (!StringUtils.isEmpty(orderEvaluateListDataDto.getUserSeq())) {
					boolean flag = isNumeric(orderEvaluateListDataDto.getUserSeq().trim());
					if(!flag) {
						resultDto.setMsg("SEQ【" + orderEvaluateListDataDto.getUserSeq() + "】为非法字符串,用户SEQ必须为纯数字或纯数字字符串");
						break;
					}
					Shopper tmpShopper = new Shopper();
					tmpShopper.setSeq(Integer.parseInt(orderEvaluateListDataDto.getUserSeq()));
					Shopper shopper = shopperMapper.queryBySeqOrMobile(tmpShopper);
					if (StringUtils.isEmpty(shopper)) {
						resultDto.setMsg("SEQ为【" + orderEvaluateListDataDto.getUserSeq() + "】的用户不存在");
						break;
					}
				}*/
				if (!StringUtils.isEmpty(orderEvaluateListDataDto.getGoodsCode())) {
					DistributionProduct tmp = new DistributionProduct();
					tmp.setProductCode(orderEvaluateListDataDto.getGoodsCode());
					DistributionProduct result = distributionProduct.selectOne(tmp);
					if (StringUtils.isEmpty(result)) {
						resultDto.setMsg("CODE为【" + orderEvaluateListDataDto.getGoodsCode() + "】的商品不存在");
						break;
					}
				}
	/*			if (!StringUtils.isEmpty(orderEvaluateListDataDto.getOrderNo())) {
					DistributionOrders tmp = new DistributionOrders();
					tmp.setOrderno(orderEvaluateListDataDto.getOrderNo());
					DistributionOrders result = distributionOrdersMapper.selectOne(tmp);
					if (StringUtils.isEmpty(result)) {
						resultDto.setMsg("订单编号为[" + orderEvaluateListDataDto.getOrderNo() + "]的订单不存在");
						break;
					}
				}*/
				if (StringUtils.isEmpty(orderEvaluateListDataDto.getOrderNo())
						&& StringUtils.isEmpty(orderEvaluateListDataDto.getUserSeq())
						&& StringUtils.isEmpty(orderEvaluateListDataDto.getGoodsCode())) {
					resultDto.setMsg("订单编号,商品货号和评论用户SEQ不能同时为空");
					break;
				}

				if (StringUtils.isEmpty(orderEvaluateListDataDto.getPage())) {
					resultDto.setMsg("查询页码数不能为空");
					break;
				}
				if (StringUtils.isEmpty(orderEvaluateListDataDto.getLimit())) {
					resultDto.setMsg("查询页码大小不能为空");
					break;
				}
				if (orderEvaluateListDataDto.getLimit() > PAGE_LIMIT_MAX) {
					resultDto.setMsg("一次最多只能查询" + PAGE_LIMIT_MAX + "条记录");
					break;
				}
				//如果传入auditType为空，将其值修改为0后传进service
				if (StringUtils.isEmpty(orderEvaluateListDataDto.getAuditType())) {
					orderEvaluateListDataDto.setAuditType("0");
				}
				Page<ResOrderEvaluateListDataDto> pageList_orderEvaluate = orderEvaluateService
						.queryOrderEvaluateListByOrderNo(orderEvaluateListDataDto, initQueryPageDtp(
								orderEvaluateListDataDto.getPage(), orderEvaluateListDataDto.getLimit()));
				Map<String, Object> dataMaps_3003 = new HashMap<>();
				dataMaps_3003.put("pages", PageListToPageDto(pageList_orderEvaluate));
				dataMaps_3003.put("datas", pageList_orderEvaluate.getResult());
				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				resultDto.setResult(dataMaps_3003);
				break;
			default:
				resultDto.setMsg("未知方法");
				break;
			}
		} catch (Exception e) {
			String err = "调用接口出错," + e.getLocalizedMessage();
			log.error(err, e);
			resultDto.setMsg(err);
		} finally {
			resultDto.setResponseTime(DateUtils.now());// 设置成功时间

			String action = request.getRequestURI();
			String referer = request.getHeader("Referer");

			ApiRequestDataLog apiLog = new ApiRequestDataLog();
			apiLog.setAction(action);
			apiLog.setRefAction(referer);
			apiLog.setRefIp(refIp);
			apiLog.setCreateTime(LocalDateTime.now());
			apiLog.setActionCode(params.getCode());
			apiLog.setActionSource(params.getSource());
			apiLog.setActionDeviceType(params.getDeviceType());
			apiLog.setActionPara(JsonUtils.toJson(params));
			apiLog.setReturnData(JsonUtils.toJson(resultDto));
			apiRequestDataLogMongo.insert(apiLog);
			if (!StringUtils.isEmpty(resultDto.getMsg()) && resultDto.getMsg().startsWith("调用接口出错,")) {
				resultDto.setMsg("获取后台信息服务出错[" + apiLog.getId() + "]");
			}
		}

		log.debug("---resultDto---" + params.getCode() + "==" + resultDto.getMsg());
		return resultDto;
	}

	private <T> void setReturn(BaseResponseResultDto resultDto, Page<T> pageResult) {
		Map<String, Object> dataMaps = new HashMap<>();
		dataMaps.put("pages", PageListToPageDto(pageResult));
		dataMaps.put("datas", pageResult.getResult());
		resultDto.setCode(ResponseResultCodeEnums.SUCCESS_CODE.getCode() + "");
		resultDto.setMsg("操作成功");
		resultDto.setResult(dataMaps);
	}

	private <T>  void prosessReturnData(BaseResponseResultDto resultDto, Msg<T> msgBack1) {
		if (msgBack1.isOk()) {
			resultDto.setCode(ResponseResultCodeEnums.SUCCESS_CODE.getCode() + "");
			resultDto.setMsg(msgBack1.getMsg());
			resultDto.setResult(msgBack1.getValue());
			resultDto.setResponseTime(DateUtils.to(LocalDateTime.now(), DateFormat.LONG_DATE_PATTERN_LINE_SLASH));
		} else {
			if (msgBack1.getCode()!=null){
				resultDto.setCode(msgBack1.getCode()+"");
			}
			resultDto.setMsg(msgBack1.getMsg());
			resultDto.setResponseTime(DateUtils.to(LocalDateTime.now(), DateFormat.LONG_DATE_PATTERN_LINE_SLASH));
		}
	}

	// @ApiOperation(value = "用户日志接口", notes = "用户日志收集", httpMethod = "GET") //
	@RequestMapping(value = "/wlog.gif", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void wlog(HttpServletRequest request, HttpServletResponse response) {
		try {
			String action = request.getRequestURI();
			String referer = request.getHeader("Referer");
			String remortIP = CommRequestUtil.getRemortIP(request);
			Map<String, String> map = CommRequestUtil.getRequestMap(request);

			System.out.println((MemoryViewUtil.showMemoryStr()));
			log.debug(remortIP + "==" + referer + "---wlog.gif----" + request.getHeader("Origin") + "====" + action);
			if (!systemProperties.getWeblogTest()) {// 正式环境。保存到DB
				guestBrowsingLogService.save(map, remortIP);
			}

			ServletOutputStream responseOutputStream = response.getOutputStream();
			// 输出图象到页面
			BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			ImageIO.write(image, "gif", responseOutputStream);
			// 以下关闭输入流！
			responseOutputStream.flush();
			responseOutputStream.close();
		} catch (IOException e) {
			log.error("wlog.gif", e);
		}
	}

	@ApiOperation(value = "通用请求接口入口", notes = "该对外接口通过code参数区分方法", httpMethod = "POST") //
	@RequestMapping(value = "/extApi", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody BaseResponseResultDto extApi(HttpServletRequest request,
			@ApiParam @RequestBody BaseReceiveParamDto params) {
		log.debug(MemoryViewUtil.showMemoryStr());
		String refIp = CommRequestUtil.getRemortIP(request);

		BaseResponseResultDto resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.FAIL_CODE);
		try {
			if (StringUtils.isEmpty(params)) {
				resultDto.setMsg("请求参数为空。");
				return resultDto;
			}
			log.debug("========后台日志==preHandle==log===[" + refIp + "]用户request来自：" + request.getRequestURI() + "==="+ params);

			BaseResponseResultDto valiDto = validParams(params);
			if (valiDto != null) {
				return valiDto;
			}
			ReceiveCodeEnums enumDtos = ReceiveCodeEnums.getByCode(params.getCode());
			if (enumDtos == null) {
				resultDto.setMsg("请求参数Code[" + params.getCode() + "]有误");
				return resultDto;
			}

			// 参数校验

			Object obj = params.getRequestData().get(params.getCode());// 具体参数详情
			switch (enumDtos) {
			case COUPONS_ONE_BUTTON_TO_RECEIVE_ALL: // 一键领取新人活动所有优惠券
				ReqUserReciveCouponsApiDataDto dto_1013 = JsonUtils.parse(JsonUtils.toDefaultJson(obj), ReqUserReciveCouponsApiDataDto.class);
				if (StringUtils.isEmpty(dto_1013.getActBatch())) {
					resultDto.setMsg("活动批次号不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1013.getUserSeq())) {
					resultDto.setMsg("用户seq不能为空");
					break;
				}
				Msg<String> msg_1013 = couponsUserService.receiveNewPeopleCoupons(dto_1013);
				if (msg_1013.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
				}
				resultDto.setMsg(msg_1013.getMsg());
				break;
			case COUPONS_HAVE_GET_OR_NOT: // 是否新人用户判断
				CouponsCheckGetOrNotParamDto dto_1012 = JsonUtils.parse(JsonUtils.toDefaultJson(obj), CouponsCheckGetOrNotParamDto.class);
				if (StringUtils.isEmpty(dto_1012.getUserSeq())) {
					resultDto.setMsg("用户seq不能为空");
					break;
				}
				boolean flag = couponsUserService.checkCouponsGetOrNotByUser(dto_1012.getUserSeq());
				Msg<String>msg_1012;
				Map<String, Object> dataMaps_1012 = new HashMap<>();
				if(flag) {
					msg_1012=Msg.ok("该用户已领取过优惠券","");
					dataMaps_1012.put("isGetCoupons", true);
				}else {
					msg_1012=Msg.ok("该用户未领取过优惠券","");
					dataMaps_1012.put("isGetCoupons", false);
				}
				if (msg_1012.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
				}
				resultDto.setMsg(msg_1012.getMsg());
				resultDto.setResult(dataMaps_1012);
				break;
			case COUPONS_NEW_PEOPLE_ACTIVITY_LIST: // 新人活动列表
				ReqActivityListDataDto dto_1011 = JsonUtils.parse(JsonUtils.toDefaultJson(obj), ReqActivityListDataDto.class);

				if (StringUtils.isEmpty(dto_1011.getPage())) {
					resultDto.setMsg("查询页码数不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1011.getLimit())) {
					resultDto.setMsg("查询页码大小不能为空");
					break;
				}
				if (dto_1011.getLimit() > PAGE_LIMIT_MAX) {
					resultDto.setMsg("一次最多只能查询" + PAGE_LIMIT_MAX + "条记录");
					break;
				}
				
				if (StringUtils.isEmpty(dto_1011.getFromSys())) {//来源为空则默认运营系统
					dto_1011.setFromSys(CouponsFromSysEnums.SYS_OPERATE_CENTER.getCode());
					dto_1011.setFromSysCode(Constant.COUPONS_FROM_SYS_CODE_DEFAULT);
				}else {
					CouponsFromSysEnums fromSysEnums = CouponsFromSysEnums.getByCode(dto_1011.getFromSys());
					if(fromSysEnums == null) {
						resultDto.setMsg("系统来源["+dto_1011.getFromSys()+"]不正确");
						break;
					}if(!fromSysEnums.getCode().equals(CouponsFromSysEnums.SYS_OPERATE_CENTER.getCode())) {//其他系统 系统来源Code不能为空
						if (StringUtils.isEmpty(dto_1011.getFromSysCode())) {
							resultDto.setMsg("系统来源Code["+dto_1011.getFromSysCode()+"]不能为空");
							break;
						}
					}
				}
				Page<ResActivityListDataDto> pageList_1011 = activityService.queryNewPeopleActivityListByApi(dto_1011, initQueryPageDtp(dto_1011.getPage(), dto_1011.getLimit()));

				formatActivityDetail(pageList_1011); // 处理返回前对象数据
				Map<String, Object> dataMaps_1011 = new HashMap<>();
				dataMaps_1011.put("pages", PageListToPageDto(pageList_1011));
				dataMaps_1011.put("datas", pageList_1011.getResult());
				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				Map<String, Object> resultMap_10011 = new HashMap<>();
				resultMap_10011.put(ReceiveCodeEnums.COUPONS_ACTIVITY_LIST.getCode(), dataMaps_1011);
				resultDto.setResult(dataMaps_1011);
				break;
			case COUPONS_BRAND_COUPONS_LIST:// 品牌商店优惠券列表
				ReqBrandCouponsListDataDto dto_1010 = JsonUtils.parse(JsonUtils.toDefaultJson(obj), ReqBrandCouponsListDataDto.class);
				if (StringUtils.isEmpty(dto_1010.getFromSys())) {//来源为空则默认运营系统
					dto_1010.setFromSys(CouponsFromSysEnums.SYS_OPERATE_CENTER.getCode());
					dto_1010.setFromSysCode(Constant.COUPONS_FROM_SYS_CODE_DEFAULT);
				}else {
					CouponsFromSysEnums fromSysEnums = CouponsFromSysEnums.getByCode(dto_1010.getFromSys());
					if(fromSysEnums == null) {
						resultDto.setMsg("系统来源["+dto_1010.getFromSys()+"]不正确");
						break;
					}if(!fromSysEnums.getCode().equals(CouponsFromSysEnums.SYS_OPERATE_CENTER.getCode())) {//其他 系统来源Code不能为空
						if (StringUtils.isEmpty(dto_1010.getFromSysCode())) {
							resultDto.setMsg("系统来源Code["+dto_1010.getFromSysCode()+"]不能为空");
							break;
						}
					}
				}
				
				if (StringUtils.isEmpty(dto_1010.getPage())) {
					resultDto.setMsg("查询页码数不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1010.getLimit())) {
					resultDto.setMsg("查询页码大小不能为空");
					break;
				}
				if (dto_1010.getLimit() > PAGE_LIMIT_MAX) {
					resultDto.setMsg("一次最多只能查询" + PAGE_LIMIT_MAX + "条记录");
					break;
				}
				Page<ResActivityCouponsListDataDto> pageList_1010 = couponsService.queryCouponsListByApiBrand(dto_1010, initQueryPageDtp(dto_1010.getPage(), dto_1010.getLimit()));
				
				Page<ResUserCouponsPackDataDto> userCouponsPagesData_1010 = null;
				// 用户对每个优惠券Id 领取次数<key:couponsId,value:领取次数>
				Map<String, Integer> userReciveNumsMap_1010 = new HashMap<String, Integer>();
				if (!StringUtils.isEmpty(dto_1010.getUserSeq())) {
					userCouponsPagesData_1010 = couponsUserService.queryBySeqAllUsableUseCache(dto_1010.getUserSeq());
					userCouponsPagesData_1010.forEach(u -> {
						userReciveNumsMap_1010.merge(u.getCouponsId(), 1, (value, newValue) -> value + 1);
					});
				}
				formatCouponsDetail(pageList_1010, userReciveNumsMap_1010); // 处理返回前对象数据

				Map<String, Object> dataMaps_1010 = new HashMap<>();
				dataMaps_1010.put("pages", PageListToPageDto(pageList_1010));
				dataMaps_1010.put("datas", pageList_1010.getResult());
				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				Map<String, Object> resultMap_1010 = new HashMap<>();
				resultMap_1010.put(ReceiveCodeEnums.COUPONS_BRAND_COUPONS_LIST.getCode(), dataMaps_1010);
				resultDto.setResult(JsonUtils.toDefaultJsonEmptyWrite(resultMap_1010));
				break;
			case COUPONS_GOODS_COUPONS_LIST:// 商品优惠券列表
				ReqGoodsCouponsListDataDto dto_1009 = JsonUtils.parse(JsonUtils.toDefaultJson(obj),ReqGoodsCouponsListDataDto.class);

				if (StringUtils.isEmpty(dto_1009.getGoodsCode())) {
					resultDto.setMsg("商品code不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1009.getPage())) {
					resultDto.setMsg("查询页码数不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1009.getLimit())) {
					resultDto.setMsg("查询页码大小不能为空");
					break;
				}
				if (dto_1009.getLimit() > PAGE_LIMIT_MAX) {
					resultDto.setMsg("一次最多只能查询" + PAGE_LIMIT_MAX + "条记录");
					break;
				}
				Page<ResGoodsCouponsListDataDto> pageList_1009 = couponsUserService.queryCouponsListByGoodsCode( dto_1009, initQueryPageDtp(dto_1009.getPage(), dto_1009.getLimit()));
				Page<ResUserCouponsPackDataDto> userCouponsPagesData_1009 = null;
				// 用户对每个优惠券Id 领取次数<key:couponsId,value:领取次数>
				Map<String, Integer> userReciveNumsMap_1009 = new HashMap<String, Integer>();
				if (!StringUtils.isEmpty(dto_1009.getUserSeq())) {
					userCouponsPagesData_1009 = couponsUserService.queryBySeqAllUsableUseCache(dto_1009.getUserSeq());
					userCouponsPagesData_1009.forEach(u -> {
						userReciveNumsMap_1009.merge(u.getCouponsId(), 1, (value, newValue) -> value + 1);
					});
				}
				formatGoodsCouponsDetail(pageList_1009, userReciveNumsMap_1009); // 处理返回前对象数据

				Map<String, Object> dataMaps_1009 = new HashMap<>();
				dataMaps_1009.put("pages", PageListToPageDto(pageList_1009));
				dataMaps_1009.put("datas", pageList_1009.getResult());
				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				Map<String, Object> resultMap_1009 = new HashMap<>();
				resultMap_1009.put(ReceiveCodeEnums.COUPONS_GOODS_COUPONS_LIST.getCode(), dataMaps_1009);
				resultDto.setResult(JsonUtils.toDefaultJsonEmptyWrite(resultMap_1009));
				break;
			case COUPONS_ACTIVITY_COUPONS_LIST: // 活动优惠券列表
				ReqActivityCouponsListDataDto dto_1008 = JsonUtils.parse(JsonUtils.toDefaultJson(obj), ReqActivityCouponsListDataDto.class);
				if (StringUtils.isEmpty(dto_1008.getActBatch())) {
					resultDto.setMsg("活动批次号不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1008.getPage())) {
					resultDto.setMsg("查询页码数不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1008.getLimit())) {
					resultDto.setMsg("查询页码大小不能为空");
					break;
				}
				if (dto_1008.getLimit() > PAGE_LIMIT_MAX) {
					resultDto.setMsg("一次最多只能查询" + PAGE_LIMIT_MAX + "条记录");
					break;
				}
				Page<ResActivityCouponsListDataDto> pageList_1008 = couponsService.queryCouponsListByApi(dto_1008, initQueryPageDtp(dto_1008.getPage(), dto_1008.getLimit()));

				Page<ResUserCouponsPackDataDto> userCouponsPages = null;
				// 用户对每个优惠券Id 领取次数<key:couponsId,value:领取次数>
				Map<String, Integer> mapUserReciveNums = new HashMap<String, Integer>();
				if (!StringUtils.isEmpty(dto_1008.getUserSeq())) {
					userCouponsPages = couponsUserService.queryBySeqAllUsableUseCache(dto_1008.getUserSeq());
					userCouponsPages.forEach(u -> {
						mapUserReciveNums.merge(u.getCouponsId(), 1, (value, newValue) -> value + 1);
					});
				}
				formatCouponsDetail(pageList_1008, mapUserReciveNums); // 处理返回前对象数据

				Map<String, Object> dataMaps_1008 = new HashMap<>();
				dataMaps_1008.put("pages", PageListToPageDto(pageList_1008));
				dataMaps_1008.put("datas", pageList_1008.getResult());
				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				Map<String, Object> resultMap_1008 = new HashMap<>();
				resultMap_1008.put(ReceiveCodeEnums.COUPONS_ACTIVITY_COUPONS_LIST.getCode(), dataMaps_1008);
				resultDto.setResult(JsonUtils.toDefaultJsonEmptyWrite(resultMap_1008));
				break;
			case COUPONS_ACTIVITY_LIST: // 活动列表
				ReqActivityListDataDto dto_1007 = JsonUtils.parse(JsonUtils.toDefaultJson(obj), ReqActivityListDataDto.class);

				if (StringUtils.isEmpty(dto_1007.getPage())) {
					resultDto.setMsg("查询页码数不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1007.getLimit())) {
					resultDto.setMsg("查询页码大小不能为空");
					break;
				}
				if (dto_1007.getLimit() > PAGE_LIMIT_MAX) {
					resultDto.setMsg("一次最多只能查询" + PAGE_LIMIT_MAX + "条记录");
					break;
				}
				
				if (StringUtils.isEmpty(dto_1007.getFromSys())) {//来源为空则默认运营系统
					dto_1007.setFromSys(CouponsFromSysEnums.SYS_OPERATE_CENTER.getCode());
					dto_1007.setFromSysCode(Constant.COUPONS_FROM_SYS_CODE_DEFAULT);
				}else {
					CouponsFromSysEnums fromSysEnums = CouponsFromSysEnums.getByCode(dto_1007.getFromSys());
					if(fromSysEnums == null) {
						resultDto.setMsg("系统来源["+dto_1007.getFromSys()+"]不正确");
						break;
					}if(!fromSysEnums.getCode().equals(CouponsFromSysEnums.SYS_OPERATE_CENTER.getCode())) {//其他系统 系统来源Code不能为空
						if (StringUtils.isEmpty(dto_1007.getFromSysCode())) {
							resultDto.setMsg("系统来源Code["+dto_1007.getFromSysCode()+"]不能为空");
							break;
						}
					}
				}
				//设置过滤，不查询新人活动
				dto_1007.setUserRoleRule(0);
				Page<ResActivityListDataDto> pageList_1007 = activityService.queryListByApi(dto_1007, initQueryPageDtp(dto_1007.getPage(), dto_1007.getLimit()));

				formatActivityDetail(pageList_1007); // 处理返回前对象数据
				Map<String, Object> dataMaps_1007 = new HashMap<>();
				dataMaps_1007.put("pages", PageListToPageDto(pageList_1007));
				dataMaps_1007.put("datas", pageList_1007.getResult());
				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				Map<String, Object> resultMap_1007 = new HashMap<>();
				resultMap_1007.put(ReceiveCodeEnums.COUPONS_ACTIVITY_LIST.getCode(), dataMaps_1007);
				resultDto.setResult(JsonUtils.toDefaultJsonEmptyWrite(resultMap_1007));
				break;
			case COUPONS_ORDER_RESTORE: // 优惠券还原
				// 用户使用优惠券结算订单
				ReqUserCouponsOrderLock dto_1006 = JsonUtils.parse(JsonUtils.toDefaultJson(obj), ReqUserCouponsOrderLock.class);
				Msg<String> msg_1006 = couponsUserService.updateCouponsUserOrderByLockCannel(dto_1006);
				if (msg_1006.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
				}
				resultDto.setMsg(msg_1006.getMsg());
				break;
			case COUPONS_ORDER_PAYMENT: // 优惠券支付结算
				// 用户使用优惠券结算订单
				ReqUserCouponsOrderLock dto_1005 = JsonUtils.parse(JsonUtils.toDefaultJson(obj), ReqUserCouponsOrderLock.class);
				Msg<String> msg_1005 = couponsUserService.updateCouponsUserOrderByLockSuccess(dto_1005);
				if (msg_1005.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
				}
				resultDto.setMsg(msg_1005.getMsg());
				break;
			case COUPONS_ORDER_USER: // 优惠券锁定
				// 用户使用优惠券结算订单
				ReqUserCouponsOrderUseDto dto_1004 = JsonUtils.parse(JsonUtils.toDefaultJson(obj),
						ReqUserCouponsOrderUseDto.class);
				Msg<String> msg_1004 = couponsUserService.updateCouponsUserOrderUsed(dto_1004);
				if (msg_1004.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
				}
				resultDto.setMsg(msg_1004.getMsg());
				break;
			case COUPONS_ORDER_LIST: // 订单页优惠券列表
				ReqUserCouponsOrderPrevDto dto_1003 = JsonUtils.parse(JsonUtils.toDefaultJson(obj),
						ReqUserCouponsOrderPrevDto.class);
				if (StringUtils.isEmpty(dto_1003.getUserSeq())) {
					resultDto.setMsg("用户标识不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1003.getGoodsCodeList())) {
					resultDto.setMsg("商品Code不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1003.getOrderMoney())) {
					resultDto.setMsg("订单金额不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1003.getLimit())) {
					resultDto.setMsg("查询页码大小不能为空");
					break;
				}
				if (dto_1003.getLimit() > PAGE_LIMIT_MAX) {
					resultDto.setMsg("一次最多只能查询" + PAGE_LIMIT_MAX + "条记录");
					break;
				}
				Page<ResUserCouponsPackDataDto> pageList_1003 = couponsUserService.queryByQuickPurchaseOrder(dto_1003, initQueryPageDtp(dto_1003.getPage(), dto_1003.getLimit()));
				formatCouponsUserDetail(pageList_1003); // 处理返回前对象数据
				Map<String, Object> dataMaps_1003 = new HashMap<>();
				dataMaps_1003.put("pages", PageListToPageDto(pageList_1003));
				dataMaps_1003.put("datas", pageList_1003.getResult());
				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				Map<String, Object> resultMap_1003 = new HashMap<>();
				resultMap_1003.put(ReceiveCodeEnums.COUPONS_ORDER_LIST.getCode(), dataMaps_1003);
				resultDto.setResult(JsonUtils.toDefaultJsonEmptyWrite(resultMap_1003));
				break;
			case COUPONS_USER_DETAIL_LIST: // 用户优惠券详情列表
				ReqUserCouponsPackDto dto_1002 = JsonUtils.parse(JsonUtils.toDefaultJson(obj), ReqUserCouponsPackDto.class);
				if (StringUtils.isEmpty(dto_1002.getUserSeq())) {
					resultDto.setMsg("用户标识不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1002.getPage())) {
					resultDto.setMsg("查询页码数不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1002.getLimit())) {
					resultDto.setMsg("查询页码大小不能为空");
					break;
				}
				if (dto_1002.getLimit() > PAGE_LIMIT_MAX) {
					resultDto.setMsg("一次最多只能查询" + PAGE_LIMIT_MAX + "条记录");
					break;
				}
				Page<ResUserCouponsPackDataDto> pageList_1002 = couponsUserService.queryByQuickPurchase(dto_1002, initQueryPageDtp(dto_1002.getPage(), dto_1002.getLimit()));

				formatCouponsUserDetail(pageList_1002); // 处理返回前对象数据
				Map<String, Object> dataMaps_1002 = new HashMap<>();
				dataMaps_1002.put("pages", PageListToPageDto(pageList_1002));
				dataMaps_1002.put("datas", pageList_1002.getResult());
				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				Map<String, Object> resultMap_1002 = new HashMap<>();
				resultMap_1002.put(ReceiveCodeEnums.COUPONS_USER_DETAIL_LIST.getCode(), dataMaps_1002);
				resultDto.setResult(JsonUtils.toDefaultJsonEmptyWrite(resultMap_1002));
				break;
			case COUPONS_USER_RECEIVE: // 用户领取优惠券
				// 用户领取优惠券 实现。参数: actBatch,userSeq,couponsId
				ReqUserReciveCouponsApiDataDto dto_1001 = JsonUtils.parse(JsonUtils.toDefaultJson(obj), ReqUserReciveCouponsApiDataDto.class);
				Msg<String> msg = couponsUserService.receiveCoupons(dto_1001);
				if (msg.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
				}
				resultDto.setMsg(msg.getMsg());
				break;

			case COUPONS_GIFT_CODE_ENUMS://赠送用户优惠券
				ReqGiftUserCoupons dto_1014=JsonUtils.parse(JsonUtils.toDefaultJson(obj),ReqGiftUserCoupons.class);
				int i=dto_1014.getAmount().compareTo(BigDecimal.ZERO);
				if (i<=0){
					 resultDto.setMsg("赠送金额不能小于等于0");
                    break;
				}
				if (StringUtils.isEmpty(dto_1014.getUserSeq())){
					resultDto.setMsg("赠送用户seq不能为空");
                    break;
				}
				if (StringUtils.isEmpty(dto_1014.getTargetUserSeq())){
					resultDto.setMsg("赠送目标用户seq不能为空");
                    break;
				}

				Msg<String> msg_1014 = couponsUserService.giftUserCoupons(dto_1014);
				if (msg_1014.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
					resultDto.setMsg(msg_1014.getMsg());
					resultDto.setResult(msg_1014.getValue());
				}
				resultDto.setMsg(msg_1014.getMsg());
				break;

			case COUPONS_USER_OUT_CODE_ENUMS://设置优惠券过期
				ReqUserCouponsOrderLock dto_1015=JsonUtils.parse(JsonUtils.toDefaultJson(obj),ReqUserCouponsOrderLock.class);
				if (StringUtils.isEmpty(dto_1015.getCouponsUserId())){
					resultDto.setMsg("用户优惠券id不能为空");
				}
				if (StringUtils.isEmpty(dto_1015.getUserSeq())){
					resultDto.setMsg("用户seq不能为空");
				}

				Msg<String> msg_1015 = couponsUserService.updateUserCouponsStatus(dto_1015);

				if (msg_1015.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
					resultDto.setMsg(msg_1015.getValue());
				}else {
					resultDto.setMsg(msg_1015.getMsg());
				}
				break;

			case COUPONS_DABING_CODE_ENUMS:
				ReqActivityCouponsListDataDto dto_1016=JsonUtils.parse(JsonUtils.toDefaultJson(obj),ReqActivityCouponsListDataDto.class);
				if (StringUtils.isEmpty(dto_1016.getPage())) {
					resultDto.setMsg("查询页码数不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1016.getLimit())) {
					resultDto.setMsg("查询页码大小不能为空");
					break;
				}
				if (dto_1016.getLimit() > PAGE_LIMIT_MAX) {
					resultDto.setMsg("一次最多只能查询" + PAGE_LIMIT_MAX + "条记录");
					break;
				}
				Page<ResActivityCouponsListDataDto> resultPage=couponsService.queryDabingCouponsListByApi(dto_1016,initQueryPageDtp(dto_1016.getPage(), dto_1016.getLimit()));

				Map<String, Object> dataMaps_1016 = new HashMap<>();
				dataMaps_1016.put("pages", PageListToPageDto(resultPage));
				dataMaps_1016.put("datas", resultPage.getResult());
				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				Map<String, Object> resultMap_1016 = new HashMap<>();
				resultMap_1016.put(ReceiveCodeEnums.COUPONS_DABING_CODE_ENUMS.getCode(), dataMaps_1016);
				resultDto.setResult(JsonUtils.toDefaultJsonEmptyWrite(resultMap_1016));
//				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
//				resultDto.setResult(JsonUtils.toDefaultJsonEmptyWrite(resultPage));
				break;

			case COUPONS_USER_RECEIVE_ONE_ENUMS:
				ReqUserReciveCouponsApiDataDto dto_1017=JsonUtils.parse(JsonUtils.toDefaultJson(obj),ReqUserReciveCouponsApiDataDto.class);

				if (StringUtils.isEmpty(dto_1017.getUserSeq())){
					resultDto.setMsg("用户标识不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1017.getUserPhone())){
					resultDto.setMsg("用户手机号不能为空");
				}

				Msg<String> msg1017=couponsUserService.userReciveCoupons(dto_1017);
				if (msg1017.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
				}
				resultDto.setMsg(msg1017.getMsg());
				break;

			case DEDUCTIBLE_USER_VOUCHER_GIVE_ENUMS:
				ReqUserCouponsBusinessDto dto_1018=JsonUtils.parse(JsonUtils.toDefaultJson(obj),ReqUserCouponsBusinessDto.class);

				if (StringUtils.isEmpty(dto_1018.getFromSys())){
					resultDto.setMsg("系统来源不能为空");
					break;
				}

				if (StringUtils.isEmpty(dto_1018.getFromSysCode())){
					resultDto.setMsg("来源系统Code");
					break;
				}
				if (StringUtils.isEmpty(dto_1018.getBusinessParam())||dto_1018.getBusinessParam().size()<=0){
					resultDto.setMsg("来源系统Code");
					break;
				}

				Msg<String> msg_1018 = deductibleService.giftUserDeductible(dto_1018);
//				Msg<String> msg_1018 = couponsUserService.giftUserCouponsBusiness(dto_1018);
				if (msg_1018.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
					resultDto.setMsg(msg_1018.getValue());
					break;
//					resultDto.setResult(msg_1014.getValue());
				}
				resultDto.setMsg(msg_1018.getMsg());
				break;

			case DEDUCTIBLE_USER_VOUCHER_LOCK_ENUMS:
				ReqUserCouponsDeductible dto_1019=JsonUtils.parse(JsonUtils.toDefaultJson(obj),ReqUserCouponsDeductible.class);
				if (StringUtils.isEmpty(dto_1019.getUserSeq())){
					resultDto.setMsg("用户标识不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1019.getCouponsUserId())){
					resultDto.setMsg("优惠券标识不能为空");
					break;
				}
				if (StringUtils.isEmpty(dto_1019.getUseOrderNo())){
					resultDto.setMsg("订单号不能为空");
					break;
				}
				int r=dto_1019.getDeductibleAmount().compareTo(BigDecimal.ZERO); //和0，Zero比较
				if (r<=0){
					resultDto.setMsg("抵扣金额不能小于0");
					break;
				}

//				Msg<String> msg_1019=couponsUserService.useDeductibleCoupons(dto_1019);
				Msg<String> msg_1019=deductibleService.usedDeductible(dto_1019);

				if (msg_1019.isOk()) {
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);// 设置成功
				}
				resultDto.setMsg(msg_1019.getMsg());
				break;

			case DEDUCTIBLE_GET_ENUMS:
				ReqUserCouponsDeductible dto_1020=JsonUtils.parse(JsonUtils.toDefaultJson(obj),ReqUserCouponsDeductible.class);
				if (StringUtils.isEmpty(dto_1020.getUserSeq())){
					resultDto.setMsg("用户标识不能为空");
					break;
				}

				Deductible deductible= deductibleService.getDeductible(dto_1020.getUserSeq());
				resultDto= new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				resultDto.setResult(deductible);
				break;

			case DEDUCTIBLE_EMPTY_ENUMS:
				ReqUserCouponsDeductible dto_1021=JsonUtils.parse(JsonUtils.toDefaultJson(obj),ReqUserCouponsDeductible.class);
				if (StringUtils.isEmpty(dto_1021.getId())&&StringUtils.isEmpty(dto_1021.getUserSeq())){
					resultDto.setMsg("抵扣券标识或用户seq不能为空");
					break;
				}
				if (!StringUtils.isEmpty(dto_1021.getUserSeq())){
                    Deductible deductibleParam=new Deductible();
                    deductibleParam.setUserSeq(dto_1021.getUserSeq());
                    deductibleParam.setType(2);
                    Deductible deductible1=deductibleService.selectOne(deductibleParam);
                    if (null==deductible1){
                        resultDto.setMsg("没有找到该用户的抵扣券");
                        break;
                    }else {
                        dto_1021.setId(deductible1.getId());
                    }
                }
				LocalDateTime now=LocalDateTime.now();
				Deductible deductible1=new Deductible();
				deductible1.setId(dto_1021.getId());
				deductible1.setUpdateTime(now);
				deductible1.setStatus((short)5);
				int count=deductibleService.update(deductible1);
				if (count<=0){
					resultDto.setMsg("升级清空失败");
					break;
				}
				resultDto= new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				break;
			case DEDUCTIBLE_INTRODUCE_ENUMS:

				DeductibleIntroduce deductibleIntroduceParam=new DeductibleIntroduce();
				deductibleIntroduceParam.setIsDisabled(false);
				List<DeductibleIntroduce> deductibleIntroduceList=deductibleIntroduceService.select(deductibleIntroduceParam);
				DeductibleIntroduce deductibleIntroduce=new DeductibleIntroduce();
				if (null!=deductibleIntroduceList&&deductibleIntroduceList.size()>0){
					deductibleIntroduce=deductibleIntroduceList.get(0);
				}
				Page<DeductibleIntroduceDetail> entitys=deductibleIntroduceDetailMapper.selectDeductibleDetailList(deductibleIntroduce.getId());
				deductibleIntroduce.setDeductibleIntroduceDetails(entitys);
				resultDto= new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				resultDto.setResult(deductibleIntroduce);
				break;

			default:

				resultDto.setMsg("未知方法");
				break;
			}
			// 成功

		} catch (Exception e) {
			String err = "调用接口出错," + e.getLocalizedMessage();
			log.error(err, e);
			resultDto.setMsg(err);
		} finally {

			resultDto.setResponseTime(DateUtils.now());// 设置成功时间

			String action = request.getRequestURI();
			String referer = request.getHeader("Referer");

			ApiRequestDataLog apiLog = new ApiRequestDataLog();
			apiLog.setAction(action);
			apiLog.setRefAction(referer);
			apiLog.setRefIp(refIp);
			apiLog.setCreateTime(LocalDateTime.now());
			apiLog.setActionCode(params.getCode());
			apiLog.setActionSource(params.getSource());
			apiLog.setActionDeviceType(params.getDeviceType());
			apiLog.setActionPara(JsonUtils.toJson(params));
			apiLog.setReturnData(JsonUtils.toJson(resultDto));
			apiRequestDataLogMongo.insert(apiLog);
			if (!StringUtils.isEmpty(resultDto.getMsg()) && resultDto.getMsg().startsWith("调用接口出错,")) {
				resultDto.setMsg("获取后台信息服务出错[" + apiLog.getId() + "]");
			}
		}

		log.debug("---resultDto---" + params.getCode() + "==" + resultDto.getMsg());
		return resultDto;

	}

	/**
	 * 对接口返回数据 特殊处理 <br/>
	 * 用户卡包数据，以及订单页优惠券列表数据
	 **/
	private void formatCouponsUserDetail(Page<ResUserCouponsPackDataDto> pageList) {
		if (!CollectionUtils.isEmpty(pageList)) {
			pageList.stream().forEach(tmp -> {
				if (!tmp.getCouponsType().equals("4")){
					tmp.setCouponsStatusName(CouponsStatusEnum.getNameByCode(tmp.getCouponsStatus()).getKey());
					tmp.setCouponsTypeName(CouponsTypeEnum.getNameByCode(tmp.getCouponsType()).getKey());

					String strEnd = tmp.getValiDayEnd();
					int len = strEnd.length();
					if (len >= 19) {
						strEnd = strEnd.substring(0, 19);
						LocalDateTime valiDayEnd = DateUtils.parse(strEnd, DateFormat.LONG_DATE_PATTERN_LINE);
						if (LocalDateTime.now().isAfter(valiDayEnd)) { // 已经过了有效时间
							tmp.setCouponsStatus(CouponsStatusEnum.EXPIRED.getCode().toString());
							tmp.setCouponsStatusName(CouponsStatusEnum.getMapCodeEnum()
									.get(CouponsStatusEnum.EXPIRED.getCode().toString()).getKey());
						}
					}
					if (!StringUtils.isEmpty(tmp.getCouponsHrefUrl())) {
						tmp.setCouponsHrefUrl(tmp.getCouponsHrefUrl().trim());
					}
					// 过期优惠券的图片，设置为过期样式
					if (tmp.getCouponsStatus().equals(CouponsStatusEnum.EXPIRED.getCode().toString())
							|| tmp.getCouponsStatus().equals(CouponsStatusEnum.USED.getCode().toString())) {
						tmp.setContentImg(tmp.getContentdisabledImg());
					}
				}
			});
		}

	}

	/**
	 * 对接口返回数据 特殊处理 <br/>
	 * 活动数据列表
	 **/
	private void formatActivityDetail(Page<ResActivityListDataDto> pageList) {
		if (!CollectionUtils.isEmpty(pageList)) {
			pageList.stream().forEach(tmp -> {
				String strEnd = tmp.getSendTimeEndStr();
				int len = strEnd.length();
				if (len >= 19) {
					strEnd = strEnd.substring(0, 19);
					LocalDateTime valiDayEnd = DateUtils.parse(strEnd, DateFormat.LONG_DATE_PATTERN_LINE);
					if (LocalDateTime.now().isAfter(valiDayEnd)) { // 已经过了有效时间
						tmp.setIsDisabled("true");
					}
				}
			});
		}

	}

	/**
	 * 对接口返回数据 特殊处理 <br/>
	 * 商品对应的优惠券列表
	 **/
	private void formatGoodsCouponsDetail(Page<ResGoodsCouponsListDataDto> pageList,
			Map<String, Integer> mapUserReciveNums) {
		if (!CollectionUtils.isEmpty(pageList)) {
			pageList.stream().forEach(tmp -> {

				if (!StringUtils.isEmpty(tmp.getCouponsType())) {
					CouponsTypeEnum enums = CouponsTypeEnum.getNameByCode(tmp.getCouponsType().toString());
					if (enums != null) {
						tmp.setCouponsTypeName(enums.getKey());
					}
				}

				tmp.setUserReceiveStatus("1");
				tmp.setUserReceiveStatusName("领取");

				String strEnd = tmp.getSendTimeEndStr();
				int len = strEnd.length();
				if (len >= 19) {
					strEnd = strEnd.substring(0, 19);
					LocalDateTime sendTimend = DateUtils.parse(strEnd, DateFormat.LONG_DATE_PATTERN_LINE);
					if (LocalDateTime.now().isAfter(sendTimend)) { // 已经过了领取时间
						tmp.setUserReceiveStatus("3");
						tmp.setUserReceiveStatusName("已结束");
					}
				}
				// 总领取数量达到上限 0 默认无限制
				int sendNum = Integer.parseInt(tmp.getSendNum());
				if (Integer.parseInt(tmp.getReceiveNum()) >= sendNum && sendNum != 0) {
					tmp.setUserReceiveStatus("3");
					tmp.setUserReceiveStatusName("已结束");
				}

				tmp.setReceiveNumByUse("0");
				if (!CollectionUtils.isEmpty(mapUserReciveNums)) {
					Integer userReceives = mapUserReciveNums.getOrDefault(tmp.getCouponsId(), 0);
					tmp.setReceiveNumByUse(userReceives.toString());
					int receiveNumRule = Integer.parseInt(tmp.getReceiveNumRule());
					// 用户领取数量达到上限 0 默认无限制
					if (userReceives >= receiveNumRule && receiveNumRule != 0) {
						tmp.setUserReceiveStatus("2");
						tmp.setUserReceiveStatusName("已领取");
					}
				}

				// 设置优惠券类型名称
				tmp.setCouponsTypeName(CouponsTypeEnum.getNameByCode(tmp.getCouponsType()).getKey());
			});
		}

	}

	/**
	 * 对接口返回数据 特殊处理 <br/>
	 * 活动下关联优惠券类型列表
	 **/
	private void formatCouponsDetail(Page<ResActivityCouponsListDataDto> pageList,
			Map<String, Integer> mapUserReciveNums) {
		if (!CollectionUtils.isEmpty(pageList)) {
			pageList.stream().forEach(tmp -> {

				if (!StringUtils.isEmpty(tmp.getCouponsType())) {
					CouponsTypeEnum enums = CouponsTypeEnum.getNameByCode(tmp.getCouponsType().toString());
					if (enums != null) {
						tmp.setCouponsTypeName(enums.getKey());
					}
				}

				tmp.setUserReceiveStatus("1");
				tmp.setUserReceiveStatusName("领取");

				String strEnd = tmp.getSendTimeEndStr();
				int len = strEnd.length();
				if (len >= 19) {
					strEnd = strEnd.substring(0, 19);
					LocalDateTime sendTimend = DateUtils.parse(strEnd, DateFormat.LONG_DATE_PATTERN_LINE);
					if (LocalDateTime.now().isAfter(sendTimend)) { // 已经过了领取时间
						tmp.setUserReceiveStatus("3");
						tmp.setUserReceiveStatusName("已结束");
					}
				}
				// 总领取数量达到上限 0 默认无限制
				int sendNum = Integer.parseInt(tmp.getSendNum());
				if (Integer.parseInt(tmp.getReceiveNum()) >= sendNum && sendNum != 0) {
					tmp.setUserReceiveStatus("3");
					tmp.setUserReceiveStatusName("已结束");
				}

				tmp.setReceiveNumByUse("0");
				if (!CollectionUtils.isEmpty(mapUserReciveNums)) {
					Integer userReceives = mapUserReciveNums.getOrDefault(tmp.getCouponsId(), 0);
					tmp.setReceiveNumByUse(userReceives.toString());
					int receiveNumRule = Integer.parseInt(tmp.getReceiveNumRule());
					// 用户领取数量达到上限 0 默认无限制
					if (userReceives >= receiveNumRule && receiveNumRule != 0) {
						tmp.setUserReceiveStatus("2");
						tmp.setUserReceiveStatusName("已领取");
					}
				}

				if (!StringUtils.isEmpty(tmp.getCouponsHrefUrl())) {
					tmp.setCouponsHrefUrl(tmp.getCouponsHrefUrl().trim());
				}

				// 设置优惠券类型名称
				tmp.setCouponsTypeName(CouponsTypeEnum.getNameByCode(tmp.getCouponsType()).getKey());
			});
		}

	}

	private PageDto initQueryPageDtp(Integer page, Integer limit) {
		PageDto queryPage = new PageDto();
		queryPage.setPage(page);
		queryPage.setLimit(limit);
		return queryPage;
	}

	private PageDto PageListToPageDto(Page pageList) {
		PageDto pageDto = new PageDto();
		pageDto.setLimit(pageList.getPageSize());
		pageDto.setPage(pageList.getPageNum());
		pageDto.setPageAllNums(pageList.getPages());
		pageDto.setTotalNums(pageList.getTotal());
		return pageDto;
	}

	private BaseResponseResultDto validParams(BaseReceiveParamDto dto) {
		BaseResponseResultDto resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.FAIL_CODE);
		resultDto.setResponseTime(LocalDateTime.now().toString());
		if (StringUtils.isEmpty(dto)) {
			resultDto.setMsg("请求参数转换为空。");
			return resultDto;
		}
		if (StringUtils.isEmpty(dto.getCode())) {
			resultDto.setMsg("请求参数Code为空。");
			return resultDto;
		}
		if (StringUtils.isEmpty(dto.getSource())) {
			resultDto.setMsg("请求参数来源为空。");
			return resultDto;
		}
		if (StringUtils.isEmpty(dto.getDeviceType())) {
			resultDto.setMsg("请求设备类型为空。");
			return resultDto;
		}

		if (StringUtils.isEmpty(dto.getRequestData())) {
			resultDto.setMsg("请求参数详情为空。");
			return resultDto;
		}

		if (!dto.getSourceList().contains(dto.getSource())) {
			resultDto.setMsg("请求参数来源错误。");
			return resultDto;
		}

		if (!dto.getDeviceTypeList().contains(dto.getDeviceType())) {
			resultDto.setMsg("请求来源设备类型错误。");
			return resultDto;
		}

		// 验证通过返回null
		return null;
	}

	private BaseResponseResultDto validOrderEvaluateParams(BaseOrderEvaluateParamDto dto) {
		BaseResponseResultDto resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.FAIL_CODE);
		resultDto.setResponseTime(LocalDateTime.now().toString());
		if (StringUtils.isEmpty(dto)) {
			resultDto.setMsg("请求参数转换为空。");
			return resultDto;
		}
		if (StringUtils.isEmpty(dto.getCode())) {
			resultDto.setMsg("请求参数Code为空。");
			return resultDto;
		}
		if (StringUtils.isEmpty(dto.getSource())) {
			resultDto.setMsg("请求参数来源为空。");
			return resultDto;
		}
		if (StringUtils.isEmpty(dto.getDeviceType())) {
			resultDto.setMsg("请求设备类型为空。");
			return resultDto;
		}

		if (StringUtils.isEmpty(dto.getRequestData())) {
			resultDto.setMsg("请求参数详情为空。");
			return resultDto;
		}

		if (!dto.getSourceList().contains(dto.getSource())) {
			resultDto.setMsg("请求参数来源错误。");
			return resultDto;
		}

		if (!dto.getDeviceTypeList().contains(dto.getDeviceType())) {
			resultDto.setMsg("请求来源设备类型错误。");
			return resultDto;
		}

		// 验证通过返回null
		return null;
	}
	//验证纯数字字符串
	public static boolean isNumeric(String str)
	{
		for (int i = 0; i < str.length(); i++)
		{
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
