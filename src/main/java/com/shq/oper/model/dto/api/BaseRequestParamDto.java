package com.shq.oper.model.dto.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.shq.oper.model.dto.api.req.ReqBrandActivityListDataDto;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.JsonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 请求接口接受参数
 */
@lombok.Data
public class BaseRequestParamDto implements Serializable{
	private static final long serialVersionUID = 1L;

	private String paramsStr;	//原参数

	private String code;	//ReceiveCodeEnums

	private String source;	//101分销 ，102订货

	private String  deviceType;	//H5,PC,WX,WXAPP,ANDROID,IOS

	private String recTime;		//发起时间	

	private Object requestData;	//参数详情

}