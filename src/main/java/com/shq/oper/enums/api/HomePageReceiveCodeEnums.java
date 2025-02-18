package com.shq.oper.enums.api;

import java.util.HashMap;
import java.util.Map;

public enum HomePageReceiveCodeEnums {

	ADV_APP_LIST("App启动页广告列表", "5001"),
	LOVE_HOME_INVITATION_PAGE_DATA_LIST("爱之家邀请页数据列表", "5002"),
	RESPRICE_REDUCTION_GOODS_EXAMINE("砍价商品财务审核", "5003"),
	VIP_COMMODITY_ENUM("VIP商品列表","5004"),
	;
	private String name;
	private String code;
	
	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	private HomePageReceiveCodeEnums(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	private static Map<String, HomePageReceiveCodeEnums> map = new HashMap<>();
	
	public static Map<String, HomePageReceiveCodeEnums> getMap() {
		return map;
	}
	public static void setMap(Map<String, HomePageReceiveCodeEnums> map) {
		HomePageReceiveCodeEnums.map = map;
	}
	static {
		for(HomePageReceiveCodeEnums ems : HomePageReceiveCodeEnums.values()) {
			map.put(ems.getCode(), ems);
		}
	}
	public static HomePageReceiveCodeEnums getByCode(String code) {
		return map.get(code);
	}
	
}
