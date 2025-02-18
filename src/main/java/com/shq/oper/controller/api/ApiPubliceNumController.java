package com.shq.oper.controller.api;

import com.github.pagehelper.Page;
import com.shq.oper.dao.mongo.ApiRequestDataLogMongo;
import com.shq.oper.enums.api.CirCleReceiveCodeEnums;
import com.shq.oper.enums.api.PubliceNumEnums;
import com.shq.oper.enums.api.ResponseResultCodeEnums;
import com.shq.oper.mapper.primarydb.CircleMapper;
import com.shq.oper.mapper.primarydb.PubliceNumMapper;
import com.shq.oper.model.domain.mongo.ApiRequestDataLog;
import com.shq.oper.model.domain.primarydb.Circle;
import com.shq.oper.model.domain.primarydb.PubliceNum;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.BaseCirCleParamDto;
import com.shq.oper.model.dto.api.BaseResponseResultDto;
import com.shq.oper.model.dto.api.req.ReqCirCleListDataDto;
import com.shq.oper.model.dto.api.req.ReqCircleDataDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Api(value = "微信公众号接口文档", tags = { "微信公众号数据接口" })
@RestController
@Slf4j
@RequestMapping("/api/")
public class ApiPubliceNumController {

	@Autowired
	private ApiRequestDataLogMongo apiRequestDataLogMongo;

	@Autowired
	private PubliceNumMapper publiceNumMapper;

	
	@ApiOperation(value = "微信公众号数据列表接口", notes = "微信公众号数据接口", httpMethod = "POST")
	@RequestMapping(value = "/publiceNumApi", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

			PubliceNumEnums enumDtos = PubliceNumEnums.getByCode(params.getCode());
			if (enumDtos == null) {
				resultDto.setMsg("请求参数Code[" + params.getCode() + "]有误");
				return resultDto;
			}
			switch (enumDtos) {
			case PUBLICE_NUM_ENUMS_LIST://
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

				PubliceNum publiceNum=new PubliceNum();
				publiceNum.setIsDisabled(false);

				Page<PubliceNum> entitys=publiceNumMapper.queryAllPubliceNum(publiceNum);

				Map<String, Object> dataMaps_8001 = new HashMap<>();
				dataMaps_8001.put("publiceNumList", entitys);
				resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.SUCCESS_CODE);
				resultDto.setResult(dataMaps_8001);
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
