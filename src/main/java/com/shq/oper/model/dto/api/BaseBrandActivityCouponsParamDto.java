package com.shq.oper.model.dto.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.shq.oper.model.dto.api.req.ReqBrandActivityListDataDto;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.JsonUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 接口接受参数
 */
@ApiModel(value="品牌活动优惠券参数Dto",description="品牌活动优惠券参数Dto")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class BaseBrandActivityCouponsParamDto implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(hidden=true)
	private String paramsStr;	//原参数

	@ApiModelProperty(value = "接口对应Code{2001:新增秒杀活动,2002:新增优惠券活动}", name="code",example="0008",allowableValues="2001,2002")	
	private String code;	//ReceiveCodeEnums

	@ApiModelProperty(value = "请求来源{101:分销 ,102:订货,201:品牌广场}", name="source",example="201",allowableValues="101,102")	
	private String source;	//101分销 ，102订货
	
	@ApiModelProperty(value = "请求来源设备类型{H5,PC,WX,WXAPP,ANDROID,IOS}", name="deviceType",example="PC",allowableValues="H5,PC,WX,WXAPP,ANDROID,IOS")	
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
	
	
	public static void main(String[] args) {
		Map<String,Object> map = new HashMap<>();
		map.put("code", "2003");
		map.put("source", "201");
		map.put("deviceType", "PC");
		map.put("recTime", DateUtils.now());
//		ReqBrandActivityAddDto dto = new ReqBrandActivityAddDto();
//		List<ReqBrandCouponsDto> brandCouponsList = new ArrayList<ReqBrandCouponsDto>();
//		brandCouponsList.add(new ReqBrandCouponsDto());
//		brandCouponsList.add(new ReqBrandCouponsDto());
//		dto.setBrandCouponsList(brandCouponsList);
//		map.put("requestData", dto);
//		ReqBrandActivityAddDto dto = new ReqBrandActivityAddDto();
//		List<ReqBrandCouponsDto> brandCouponsList = new ArrayList<ReqBrandCouponsDto>();
//		brandCouponsList.add(new ReqBrandCouponsDto());
//		brandCouponsList.add(new ReqBrandCouponsDto());
//		dto.setBrandCouponsList(brandCouponsList);
		map.put("requestData", new ReqBrandActivityListDataDto());
		System.out.println(JsonUtils.toDefaultJsonEmptyWrite(map));
		
	}
}