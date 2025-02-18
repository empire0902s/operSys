package com.shq.oper.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用范围
 * 
 * @author ljz
 * @date 2018年4月24日
 */
public enum UseRangeTypeEnum {

	ACTIVITY("活动", "ACTIVITY"), // 活动
	PRODUCT("商品类", "PRODUCT"),// 商品类
	;
	private String key;
	private String code;

	public String getCode() {
		return code;
	}

	private UseRangeTypeEnum(String key, String code) {
		this.key = key;
		this.code = code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private UseRangeTypeEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	final static Map<String, Object> map = new HashMap<>();

	public static Map<String, Object> getMap() {
		return map;
	}

	static {
		for (UseRangeTypeEnum useRangeType : UseRangeTypeEnum.values()) {
			map.put(useRangeType.getKey(), useRangeType.getCode());
		}
	}

}
