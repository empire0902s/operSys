package com.shq.oper.enums.api;

import java.util.HashMap;
import java.util.Map;

public enum ActivityReceiveCodeEnums {

	ACTIVITY_RECEIVE_CODE_ENUMS("品牌广场秒杀活动", "2001"),
	COUPONS_RECEIVE_CODE_ENUMS("品牌广场优惠券活动", "2002"),
	GET_RECEIVE_CODE_ENUMS("品牌广场获取活动列表", "2003"),
	GET_COUPONS_CODE_ENUMS("品牌广场获取活动下优惠券列表", "2004"),
	GET_COUPONS_GOODS_CODE_ENUMS("品牌广场获取优惠券商品范围", "2005"),
	SAVE_COUPONS_CATEGORY_CODE_ENUMS("品牌广场添加优惠券品类规则", "2006"),
	UPDATE_COUPONS_CODE_ENUMS("品牌广场编辑优惠券", "2007"),
	UPDATE_ACTIVITY_CODE_ENUMS("品牌广场编辑活动", "2008"),
	ENABLE_COUPONS_CODE_ENUMS("品牌广场批量启用优惠劵", "2009"),
	REMOVE_COUPONS_CATEGORY_CODE_ENUMS("品牌广场批量删除优惠券品类规则", "2010"),
	Add_COUPONS_BY_ACTIVITY_CODE_ENUMS("品牌广场根据活动添加优惠券", "2011"),
	;
	private String name;
	private String code;

	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	private ActivityReceiveCodeEnums(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	private static Map<String, ActivityReceiveCodeEnums> map = new HashMap<>();
	
	public static Map<String, ActivityReceiveCodeEnums> getMap() {
		return map;
	}
	public static void setMap(Map<String, ActivityReceiveCodeEnums> map) {
		ActivityReceiveCodeEnums.map = map;
	}
	static {
		for(ActivityReceiveCodeEnums ems : ActivityReceiveCodeEnums.values()) {
			map.put(ems.getCode(), ems);
		}
	}
	public static ActivityReceiveCodeEnums getByCode(String code) {
		return map.get(code);
	}
	
}
