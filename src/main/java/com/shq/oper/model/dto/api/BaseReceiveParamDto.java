package com.shq.oper.model.dto.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 接口接受参数
 */
@ApiModel(value="API接口接收参数对象",description="API接口接收参数对象")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class BaseReceiveParamDto implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(hidden=true)
	private String paramsStr;	//原参数

	@ApiModelProperty(value = "接口对应Code{1001:用户领取优惠券,1002:用户优惠券卡包,1003:订单页可用优惠券,1004:优惠券锁定,1005:优惠券结算,1006:优惠券还原,1007:领券中心活动列表,1008:活动下关联优惠券列表,1009:商品优惠券列表}", name="code",example="0008",allowableValues="1001,1002,1003,1004,1005,1006,1007,1008,1009")	
	private String code;	//ReceiveCodeEnums

	@ApiModelProperty(value = "请求来源{101:分销 ,102:订货}", name="source",example="101",allowableValues="101,102")	
	private String source;	//101分销 ，102订货
	
	@ApiModelProperty(value = "请求来源设备类型{H5,PC,WX,WXAPP,ANDROID,IOS}", name="deviceType",example="H5",allowableValues="H5,PC,WX,WXAPP,ANDROID,IOS")	
	private String  deviceType;	//H5,PC,WX,WXAPP,ANDROID,IOS

	@ApiModelProperty(value = "请求发起时间", name="recTime",example="2018-05-05 15:15:15")		
	private String recTime;		//发起时间	

	@ApiModelProperty(value = "请求参数详情根据对应Code，设置不同参数", name="requestData",
			example="{\"1001\":{\"actBatch\":\"520GiftPackeg(活动批次号[String])\",\"couponsId\":\"(优惠券标识不能为空[Long])\",\"userSeq\":\"(用户标识[String])\",\"userPhone\":\"(用户手机号[String])\"},\"1002\":{\"userSeq\":\"(用户标识[String])\",\"couponsStatus\":\"(优惠券状态可为空1未使用,2锁定中,3已使用,4已过期[Integer])\",\"page\":1,\"limit\":10},\"1003\":{\"goodsCodeList\":\"(商品Code,Code,Code[String])\",\"orderMoney\":\"100(订单金额[BigDecimal])\",\"userSeq\":\"(用户标识[String])\",\"page\":1,\"limit\":10},\"1004\":{\"goodsCodeList\":\"(商品Code,Code,Code[String])\",\"userSeq\":\"(用户标识[String])\",\"useOrderNo\":\"(订单号[String])\",\"couponsUserId\":\"(用户优惠券标识[Lon]g)\",\"useOrderMoney\":\"100(订单金额[BigDecimal])\",\"useSpendMoney\":\"8.88(订单优惠金额[BigDecima]l)\"},\"1005\":{\"userSeq\":\"(用户标识[String])\",\"useOrderNo\":\"(订单号[String])\",\"couponsUserId\":\"(用户优惠券标识[Lon]g)\"},\"1006\":{\"userSeq\":\"(用户标识[String])\",\"useOrderNo\":\"(订单号[String])\",\"couponsUserId\":\"(用户优惠券标识[Lon]g)\"},\"1007\":{\"actBatch\":\"520GiftPackeg\",\"page\":1,\"limit\":10},\"1008\":{\"actBatch\":\"520GiftPackeg\",\"page\":1,\"limit\":10},\"1009\":{\"goodsCode\":\"(商品code[单个商品Code])\",\"userSeq\":\"(用户标识[String])\",\"page\":1,\"limit\":10}}")	
	private Map<String,Object> requestData;	//参数详情
	
	@ApiModelProperty(hidden=true)
	private Map<String,Object> requestDataMap;	//参数详情
	
	@JSONField(serialize=false)  
	@ApiModelProperty(hidden=true)
	public List<String> getSourceList() {
		List<String> list = new ArrayList<String>();
		list.add("101");
		list.add("102");
		list.add("103");
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