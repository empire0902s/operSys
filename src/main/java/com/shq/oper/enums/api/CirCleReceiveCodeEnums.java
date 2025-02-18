package com.shq.oper.enums.api;

import java.util.HashMap;
import java.util.Map;

public enum CirCleReceiveCodeEnums {

	CIRClE_LIST("发圈数据列表", "8001"),
	CIRCLE_UPDATE("发圈数据修改","8002"),
	CIRCLE_INFO("发圈数据修改","8003"),
	;
	private String name;
	private String code;
	
	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	private CirCleReceiveCodeEnums(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	private static Map<String, CirCleReceiveCodeEnums> map = new HashMap<>();
	
	public static Map<String, CirCleReceiveCodeEnums> getMap() {
		return map;
	}
	public static void setMap(Map<String, CirCleReceiveCodeEnums> map) {
		CirCleReceiveCodeEnums.map = map;
	}
	static {
		for(CirCleReceiveCodeEnums ems : CirCleReceiveCodeEnums.values()) {
			map.put(ems.getCode(), ems);
		}
	}
	public static CirCleReceiveCodeEnums getByCode(String code) {
		return map.get(code);
	}
	
}
