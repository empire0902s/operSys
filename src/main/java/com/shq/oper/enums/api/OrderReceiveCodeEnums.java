package com.shq.oper.enums.api;

import java.util.HashMap;
import java.util.Map;

public enum OrderReceiveCodeEnums {

	ORDER_EVALUATE_ADD("订单评价新增", "3001"),
	ORDER_EVALUATE_LIKES_ADD("订单评价点赞新增", "3002"),
	ORDER_EVALUATE_LIST("订单评价列表", "3003"),
	;
	private String name;
	private String code;
	
	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	private OrderReceiveCodeEnums(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	private static Map<String, OrderReceiveCodeEnums> map = new HashMap<>();
	
	public static Map<String, OrderReceiveCodeEnums> getMap() {
		return map;
	}
	public static void setMap(Map<String, OrderReceiveCodeEnums> map) {
		OrderReceiveCodeEnums.map = map;
	}
	static {
		for(OrderReceiveCodeEnums ems : OrderReceiveCodeEnums.values()) {
			map.put(ems.getCode(), ems);
		}
	}
	public static OrderReceiveCodeEnums getByCode(String code) {
		return map.get(code);
	}
	
}
