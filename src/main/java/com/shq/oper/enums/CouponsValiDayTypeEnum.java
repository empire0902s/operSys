package com.shq.oper.enums;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.groovy.util.ListHashMap;

/**
 * 优惠券有效期类型
 */
public enum CouponsValiDayTypeEnum {

	/** "指定时间", 0 */
	USE_DATE_TIME("指定时间", 0), 
	
	/** "领取后天数", 1 */
	USE_AFTER_DAYS("领取后天数", 1), // 满减
	;
	private String key;
	private Integer code;

	public Integer getCode() {
		return code;
	}

	private CouponsValiDayTypeEnum(String key, Integer code) {
		this.key = key;
		this.code = code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	private CouponsValiDayTypeEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	final static Map<String, Object> map = new LinkedHashMap<String, Object>();

	public static Map<String, Object> getMap() {
		return map;
	}

	public void setKey(String key) {
		this.key = key;
	}

	static {
		for (CouponsValiDayTypeEnum couponsType : CouponsValiDayTypeEnum.values()) {
			System.out.println(couponsType.getKey());
			System.out.println(couponsType.getCode());
			map.put(couponsType.getKey(), couponsType.getCode());
		}
	}

	public static CouponsValiDayTypeEnum getNameByCode(String codeStr) {
		CouponsValiDayTypeEnum enums = null;
		for (CouponsValiDayTypeEnum tmp : CouponsValiDayTypeEnum.values()) {
			if (tmp.getCode().toString().equals(codeStr)) {
				enums = tmp;
				break;
			}
		}
		return enums;
	}
}
