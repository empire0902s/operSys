package com.shq.oper.controller.api;

import com.shq.oper.dao.MongoDao;
import com.shq.oper.dao.mongo.ApiRequestDataLogMongo;
import com.shq.oper.enums.api.ActivityReceiveCodeEnums;
import com.shq.oper.enums.api.BaiduPositionReportEnum;
import com.shq.oper.enums.api.ResponseResultCodeEnums;
import com.shq.oper.model.domain.mongo.ApiRequestDataLog;
import com.shq.oper.model.domain.mongo.AppLocationData;
import com.shq.oper.model.dto.api.BaseRequestParamDto;
import com.shq.oper.model.dto.api.BaseResponseResultDto;
import com.shq.oper.model.dto.api.baidu.ResBaiduGetAddrReturnDto;
import com.shq.oper.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Api(value = "app接口", tags = { "提供给app的接口" })
@RestController
@Slf4j
@RequestMapping("/api/")
public class ApiAppDataController {
    @Autowired
    private MongoDao<AppLocationData> mongoDao;

    @Autowired
    private ApiRequestDataLogMongo apiRequestDataLogMongo;

  //applocation 位置收集接口
    @ApiOperation(value = "app接口", notes = "app地址收集接口", httpMethod = "POST") //
    @RequestMapping(value = "position/report", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody BaseResponseResultDto positionReport(HttpServletRequest request,
                                     @RequestBody BaseRequestParamDto requestParamDto) {


        String refIp = CommRequestUtil.getRemortIP(request);
        log.debug("===formIp===" + refIp + "===>" + MemoryViewUtil.showMemoryStr());

        BaseResponseResultDto baseResponseResultDto=new BaseResponseResultDto(ResponseResultCodeEnums.FAIL_CODE);
        try{
            if (!requestParamDto.getDeviceType().equals("ANDROID")&&!requestParamDto.getDeviceType().equals("IOS")){
                baseResponseResultDto.setMsg("只允许安卓和IOS");
                return baseResponseResultDto;
            }
            BaiduPositionReportEnum enumDtos = BaiduPositionReportEnum.getByCode(requestParamDto.getCode());
            if (enumDtos == null) {
                baseResponseResultDto.setMsg("请求参数Code[" + requestParamDto.getCode() + "]有误");
                return baseResponseResultDto;
            }

            AppLocationData  appLocationData=JsonUtils.parse(JsonUtils.toDefaultJson(requestParamDto.getRequestData()),AppLocationData.class);
            if (StringUtils.isEmpty(appLocationData.getBaiduLatitude())){
                 baseResponseResultDto.setMsg("请输入纬度");
                 return baseResponseResultDto;
            }
            if (StringUtils.isEmpty(appLocationData.getBaiduLongitude())){
                baseResponseResultDto.setMsg("请输入经度");
                return baseResponseResultDto;
            }

            if (!StringUtils.isEmpty(requestParamDto.getDeviceType())){
                appLocationData.setDeviceType(requestParamDto.getDeviceType());
            }
            appLocationData.setLoadIp(refIp);

            appLocationData.setCreateTime(LocalDateTime.now().toString());

            baseResponseResultDto.setCode("200");
            baseResponseResultDto.setMsg("操作成功");
            try{
            	String strurl=" http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=%s,%s&output=json&pois=1&ak=EmLj55R3CSaF7Tikx0dc9vQsHAGeG2ko";
            	strurl=String.format(strurl,appLocationData.getBaiduLatitude(),appLocationData.getBaiduLongitude());
                Object o=HttpClientUtil.doGetUrl(strurl);
                String str=o.toString();
                
                str=str.substring(str.indexOf("(")+1,str.lastIndexOf(")"));
                ResBaiduGetAddrReturnDto resBaiduGetAddrReturnDto=JsonUtils.parse(str,ResBaiduGetAddrReturnDto.class);
                if (null!=resBaiduGetAddrReturnDto&&resBaiduGetAddrReturnDto.getStatus()==0){
                    appLocationData.setCurrLocation(resBaiduGetAddrReturnDto.getResult().getFormatted_address());
                }
            }catch (Exception e){
            	appLocationData.setCurrLocation("定位失败");
            }
            
            mongoDao.insert(appLocationData);
            return baseResponseResultDto;
            
        }catch (Exception e){
            baseResponseResultDto.setMsg("内部错误");

        }finally {

            baseResponseResultDto.setResponseTime(DateUtils.now());// 设置成功时间

            String action = request.getRequestURI();
            String referer = request.getHeader("Referer");

            ApiRequestDataLog apiLog = new ApiRequestDataLog();
            apiLog.setAction(action);
            apiLog.setRefAction(referer);
            apiLog.setRefIp(refIp);
            apiLog.setCreateTime(LocalDateTime.now());
            apiLog.setActionCode(requestParamDto.getCode());
            apiLog.setActionSource(requestParamDto.getSource());
            apiLog.setActionDeviceType(requestParamDto.getDeviceType());
            apiLog.setActionPara(JsonUtils.toJson(requestParamDto));
            apiLog.setReturnData(JsonUtils.toJson(baseResponseResultDto));
            apiRequestDataLogMongo.insert(apiLog);
            if (!StringUtils.isEmpty(baseResponseResultDto.getMsg()) && baseResponseResultDto.getMsg().startsWith("调用接口出错,")) {
                baseResponseResultDto.setMsg("获取后台信息服务出错[" + apiLog.getId() + "]");
            }
        }
        return  baseResponseResultDto;
    }


}
