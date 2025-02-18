package com.shq.oper.controller.api;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.Page;
import com.shq.oper.mapper.primarydb.CircleMapper;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.req.ReqCircleDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.shq.oper.dao.mongo.ApiRequestDataLogMongo;
import com.shq.oper.enums.api.CirCleReceiveCodeEnums;
import com.shq.oper.enums.api.ResponseResultCodeEnums;
import com.shq.oper.model.domain.mongo.ApiRequestDataLog;
import com.shq.oper.model.domain.primarydb.Circle;
import com.shq.oper.model.dto.api.BaseCirCleParamDto;
import com.shq.oper.model.dto.api.BaseResponseResultDto;
import com.shq.oper.model.dto.api.req.ReqCirCleListDataDto;
import com.shq.oper.service.primarydb.ActivityGoodsRuleDetailsService;
import com.shq.oper.service.primarydb.BannerService;
import com.shq.oper.service.primarydb.CircleService;
import com.shq.oper.service.primarydb.PriceReductionGoodsService;
import com.shq.oper.util.CommRequestUtil;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.JsonUtils;
import com.shq.oper.util.MemoryViewUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author ljz
 *
 */
@Api(value = "发圈数据接口文档", tags = { "发圈数据接口" })
@RestController
@Slf4j
@RequestMapping("/api/")
public class ApiCirCleController {
	
	
	@Autowired
	private ApiRequestDataLogMongo apiRequestDataLogMongo;
	
	@Autowired
	private BannerService bannerService;
	@Autowired
	private PriceReductionGoodsService priceReductionGoodsService;
	
	@Autowired
	private ActivityGoodsRuleDetailsService activityGoodsRuleDetailsService;

	@Autowired
	private CircleMapper circleMapper;
	@Autowired
	private CircleService circleService;
	
	@ApiOperation(value = "爱之家发圈数据列表接口", notes = "爱之家发圈数据接口", httpMethod = "POST")
	@RequestMapping(value = "/cirCleApi", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody BaseResponseResultDto cirCleApi(HttpServletRequest request,
			@RequestBody BaseCirCleParamDto params) {
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

			CirCleReceiveCodeEnums enumDtos = CirCleReceiveCodeEnums.getByCode(params.getCode());
			if (enumDtos == null) {
				resultDto.setMsg("请求参数Code[" + params.getCode() + "]有误");
				return resultDto;
			}
			switch (enumDtos) {
			case CIRClE_LIST://
				ReqCirCleListDataDto reqDto = JsonUtils.parse(JsonUtils.toDefaultJson(params.getRequestData()),
						ReqCirCleListDataDto.class);
				if (StringUtils.isEmpty(reqDto.getPage())) {
					resultDto.setMsg("查询页码数不能为空");
					break;
				}
				if (StringUtils.isEmpty(reqDto.getLimit())) {
					resultDto.setMsg("查询页码大小不能为空");
					break;
				}
//				if (StringUtils.isEmpty(reqDto.getType())){
//					resultDto.setMsg("请输入类型");
//					break;
//				}
				
				Circle entity=new Circle();
				entity.setType(reqDto.getType());
				PageDto page=new PageDto();
				page.setPage(reqDto.getPage());
				page.setLimit(reqDto.getLimit());
				Page<Circle> entitys=circleService.selectCirclePage(entity,page);

				Map<String, Object> dataMaps_8001 = new HashMap<>();
				dataMaps_8001.put("cirCleDataList", entitys);
				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				resultDto.setResult(dataMaps_8001);
				break;

			case CIRCLE_UPDATE:
				ReqCircleDataDto circle= JsonUtils.parse(JsonUtils.toDefaultJson(params.getRequestData()), ReqCircleDataDto.class);
				if (null==circle){
					resultDto.setMsg("参数错误");
					break;
				}
				if (StringUtils.isEmpty(circle.getId())){
					resultDto.setMsg("请输入id");
					break;
				}

				Msg<String> msg=circleService.updateSetCircle(circle);

				if (msg.isOk()){
					resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				}
				break;

			case CIRCLE_INFO:
				Circle circle1= JsonUtils.parse(JsonUtils.toDefaultJson(params.getRequestData()), Circle.class);
				if (StringUtils.isEmpty(circle1.getId())){
					resultDto.setMsg("请输入id");
					break;
				}
				circle1=circleService.selectByPk(circle1.getId());
//				circle1= circleService.selectOne(circle1);

				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				resultDto.setResult(circle1);

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
	
	
	
	
	

}
