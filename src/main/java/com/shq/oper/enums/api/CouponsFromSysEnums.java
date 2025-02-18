package com.shq.oper.enums.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 优惠券来源
 *
 */
public enum CouponsFromSysEnums {

	SYS_OPERATE_CENTER("运营系统", "operSys"),
	SYS_BRAND_SQUARE("品牌广场", "brandSquare"),
	SYS_520SHQ("520生活圈", "520shq"),
	SYS_520_WHOLESALE_NETWORK("520批发网","520WholesaleNetwork"),
	SYS_AIZHIJIA("爱之家", "homeOfLove"),
	SYS_AIZHIJIA_SHOPING("爱之家商城", "homeOfLoveMall"),
	;
	private String name;
	private String code;

	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	private CouponsFromSysEnums(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	private static Map<String, CouponsFromSysEnums> map = new HashMap<>();
	 final static Map<String, Object> objectMap = new LinkedHashMap<String, Object>();
	 
	static {
		for(CouponsFromSysEnums ems : CouponsFromSysEnums.values()) {
			map.put(ems.getCode(), ems);
			 objectMap.put(ems.getName(),ems.getCode());
		}
	}
	public static CouponsFromSysEnums getByCode(String code) {
		return map.get(code);
	}
	
	
    public static Map<String, CouponsFromSysEnums> getMap() {
        return map;
    }
    public static Map<String, Object> getObjectMap() {
        return objectMap;
    }

}
