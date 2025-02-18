package com.shq.oper.enums.api;

import java.util.HashMap;
import java.util.Map;

public enum PubliceNumEnums {

	PUBLICE_NUM_ENUMS_LIST("公众号数据列表", "9001"),

	;
	private String name;
	private String code;

	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	private PubliceNumEnums(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	private static Map<String, PubliceNumEnums> map = new HashMap<>();
	
	public static Map<String, PubliceNumEnums> getMap() {
		return map;
	}
	public static void setMap(Map<String, PubliceNumEnums> map) {
		PubliceNumEnums.map = map;
	}
	static {
		for(PubliceNumEnums ems : PubliceNumEnums.values()) {
			map.put(ems.getCode(), ems);
		}
	}
	public static PubliceNumEnums getByCode(String code) {
		return map.get(code);
	}
	
}
