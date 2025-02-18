package com.shq.oper.model.dto.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 接口接受参数
 */
@ApiModel(value="发圈接口参数Dto",description="发圈接口参数Dto")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class BaseCirCleParamDto implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(hidden=true)
	private String paramsStr;	//原参数

	@ApiModelProperty(value = "接口对应Code{8001:分页获取发圈数据列表}", name="code",example="8001",allowableValues="8001")	
	private String code;	//ReceiveCodeEnums

	@ApiModelProperty(value = "请求来源{101:分销 ,102:订货}", name="source",example="101",allowableValues="101,102")	
	private String source;	//101分销 ，102订货
	
	@ApiModelProperty(value = "请求来源设备类型{H5,PC,WX,WXAPP,ANDROID,IOS}", name="deviceType",example="H5",allowableValues="H5,PC,WX,WXAPP,ANDROID,IOS")	
	private String  deviceType;	//H5,PC,WX,WXAPP,ANDROID,IOS

	@ApiModelProperty(value = "请求发起时间", name="recTime",example="2018-05-05 15:15:15")		
	private String recTime;		//发起时间	

	@ApiModelProperty(value = "请求参数详情根据对应Code，设置不同参数", name="requestData")
	private Object requestData;	//参数详情
	

	@JSONField(serialize=false)  
	@ApiModelProperty(hidden=true)
	public List<String> getSourceList() {
		List<String> list = new ArrayList<String>();
		list.add("101");
		list.add("102");
		return list;
	}
	
	@JSONField(serialize=false)  
	@ApiModelProperty(hidden=true)
	public List<String> getDeviceTypeList() {
		List<String> list = new ArrayList<String>();
		list.add("H5");
		list.add("PC");
		list.add("WX");
		list.add("WXAPP");
		list.add("ANDROID");
		list.add("IOS");
		return list;
	}
}