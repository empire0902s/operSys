package com.shq.oper.enums.api;

import java.util.HashMap;
import java.util.Map;

public enum ReceiveCodeEnums {

	COUPONS_USER_RECEIVE("用户领取优惠券", "1001"), 
	COUPONS_USER_DETAIL_LIST("用户优惠券详情列表", "1002"),
	COUPONS_ORDER_LIST("订单页优惠券列表", "1003"),
	COUPONS_ORDER_USER("优惠券锁定", "1004"),
	COUPONS_ORDER_PAYMENT("优惠券结算", "1005"),
	COUPONS_ORDER_RESTORE("优惠券还原", "1006"),
	COUPONS_ACTIVITY_LIST("活动列表", "1007"),
	COUPONS_ACTIVITY_COUPONS_LIST("优惠券类型列表", "1008"),
	COUPONS_GOODS_COUPONS_LIST("商品优惠券列表", "1009"),
	COUPONS_BRAND_COUPONS_LIST("品牌商铺优惠券列表", "1010"),
	COUPONS_NEW_PEOPLE_ACTIVITY_LIST("新人活动列表", "1011"),
	COUPONS_HAVE_GET_OR_NOT("是否新人用户判断", "1012"),
	COUPONS_ONE_BUTTON_TO_RECEIVE_ALL ("一键领取新人活动所有优惠券", "1013"),
	COUPONS_GIFT_CODE_ENUMS("赠送优惠券","1014"),
	COUPONS_USER_OUT_CODE_ENUMS("赠送优惠券过期设置","1015"),
	COUPONS_DABING_CODE_ENUMS("获取大兵专用优惠券","1016"),
	COUPONS_USER_RECEIVE_ONE_ENUMS("大兵一键领取优惠券","1017"),
	DEDUCTIBLE_USER_VOUCHER_GIVE_ENUMS("百业惠盟注册赠送优惠券","1018"),
	DEDUCTIBLE_USER_VOUCHER_LOCK_ENUMS("抵扣券使用及还原","1019"),
	DEDUCTIBLE_GET_ENUMS("获取获取抵扣券","1020"),
	DEDUCTIBLE_EMPTY_ENUMS("抵扣券升级清空","1021"),
	DEDUCTIBLE_INTRODUCE_ENUMS("抵扣券介绍","1022"),
	;
	private String name;
	private String code;
	
	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	private ReceiveCodeEnums(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	private static Map<String, ReceiveCodeEnums> map = new HashMap<>();
	
	public static Map<String, ReceiveCodeEnums> getMap() {
		return map;
	}
	public static void setMap(Map<String, ReceiveCodeEnums> map) {
		ReceiveCodeEnums.map = map;
	}
	static {
		for(ReceiveCodeEnums ems : ReceiveCodeEnums.values()) {
			map.put(ems.getCode(), ems);
		}
	}
	public static ReceiveCodeEnums getByCode(String code) {
		return map.get(code);
	}
	
}
