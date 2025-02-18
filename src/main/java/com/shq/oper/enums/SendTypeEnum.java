package com.shq.oper.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 发放方式
 * 
 * @author ljz
 * @date 2018年4月25日
 */
public enum SendTypeEnum {

	SYSTEM_AUTO("系统自动发放", 0), // 系统自动发放
	USER_VOLUNTARY("用户主动领取", 1),// 用户主动领取
	;
	private String key;
	private Integer type;

	private SendTypeEnum(String key, Integer type) {
		this.key = key;
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	final static Map<String, Object> map = new HashMap<>();

	public static Map<String, Object> getMap() {
		return map;
	}

	static {
		for (SendTypeEnum sndType : SendTypeEnum.values()) {
			map.put(sndType.getKey(), sndType.getType());
		}
	}

}
