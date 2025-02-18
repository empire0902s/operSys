package com.shq.oper.controller.api;

import com.shq.oper.dao.mongo.ApiRequestDataLogMongo;
import com.shq.oper.enums.api.*;
import com.shq.oper.model.domain.mongo.ApiRequestDataLog;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.api.*;
import com.shq.oper.service.primarydb.*;
import com.shq.oper.util.CommRequestUtil;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.DateUtils.DateFormat;

import io.swagger.annotations.ApiOperation;

import com.shq.oper.util.JsonUtils;
import com.shq.oper.util.MemoryViewUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@RestController
@Slf4j
@RequestMapping("/api/")
public class ApiMessageController {

	@Autowired
	private ApiRequestDataLogMongo apiRequestDataLogMongo;

	@Autowired
	private MessageSendService messageSendService;

	@ApiOperation(value = "短息及app推送", notes = "短息及app推送接口", httpMethod = "POST")
	@RequestMapping(value = "/messageApi", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody BaseResponseResultDto messageApi(HttpServletRequest request,
			@RequestBody BaseRequestParamDto params) {

		String refIp = CommRequestUtil.getRemortIP(request);
		log.debug("===formIp===" + refIp + "===>" + MemoryViewUtil.showMemoryStr());
		BaseResponseResultDto resultDto = new BaseResponseResultDto(ResponseResultCodeEnums.FAIL_CODE);

		try {

			MessageSendEnum enumDtos = MessageSendEnum.getByCode(params.getCode());
			if (enumDtos == null) {
				resultDto.setMsg("请求参数Code[" + params.getCode() + "]有误");
				return resultDto;
			}

			switch (enumDtos) {
			case AIZHIJIA_CODE_ENUMS:
				Msg<String> msgBack = messageSendService.sendMessageByApi(params);
				prosessReturnData(resultDto,msgBack);
				break;

			case SHENGHUOQUAN_CODE_ENUMS:
				Msg<String> msgBack1 =  messageSendService.sendMessageByApi(params);
				prosessReturnData(resultDto,msgBack1);
				break;

			case SYS_520_WHOLESALE_NETWORK:
				Msg<String> msgBack3 =  messageSendService.sendMessageByApi(params);
				prosessReturnData(resultDto,msgBack3);
				break;

			case MESSAGE_ORDER_SEND_CODE_ENUMS:
				Msg<String> msgBack4 =  messageSendService.pushMessageByApi(params);
				prosessReturnData(resultDto,msgBack4);
				break;
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
	private void prosessReturnData(BaseResponseResultDto resultDto, Msg<String> msgBack1) {
		if (msgBack1.isOk()) {
			resultDto.setCode(ResponseResultCodeEnums.SUCCESS_CODE.getCode() + "");
			resultDto.setMsg(msgBack1.getMsg());
			resultDto.setResult(msgBack1.getValue());
			resultDto.setResponseTime(DateUtils.to(LocalDateTime.now(), DateFormat.LONG_DATE_PATTERN_LINE_SLASH));
		} else {
			resultDto.setMsg(msgBack1.getMsg());
			resultDto.setResponseTime(DateUtils.to(LocalDateTime.now(), DateFormat.LONG_DATE_PATTERN_LINE_SLASH));
		}
	}

}
