package com.shq.oper.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券状态
 * 
 * @author ljz
 * @date 2018年4月28日
 */
public enum CouponsStatusEnum {

	UNUSERED("未使用", 1), // 未使用
	LOCKED("锁定中", 2), // 锁定中
	USED("已使用", 3), // 已使用
	EXPIRED("已过期", 4),// 已过期
	;
	private String key;
	private Integer code;

	public Integer getCode() {
		return code;
	}

	private CouponsStatusEnum(String key, Integer code) {
		this.key = key;
		this.code = code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	private CouponsStatusEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	final static Map<String, Object> map = new HashMap<>();
	final static Map<String, CouponsStatusEnum> mapEnum = new HashMap<>();

	public static Map<String, CouponsStatusEnum> getMapCodeEnum() {
		return mapEnum;
	}
	public static Map<String, Object> getMap() {
		return map;
	}

	public void setKey(String key) {
		this.key = key;
	}

	static {
		for (CouponsStatusEnum couponsType : CouponsStatusEnum.values()) {
			map.put(couponsType.getKey(), couponsType.getCode());
			mapEnum.put(couponsType.getCode().toString(), couponsType);
		}
	}

	public static CouponsStatusEnum getNameByCode(String codeStr) {
		CouponsStatusEnum name = null;
		for(CouponsStatusEnum enums : CouponsStatusEnum.values()) {
			if(enums.getCode().toString().equals(codeStr)) {
				name =  enums;
				break;
			}
		}
		return name;
	}
	
}
