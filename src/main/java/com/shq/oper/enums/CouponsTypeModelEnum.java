package com.shq.oper.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券模式(浮动，固定)
 * 
 * @author ljz
 * @date 2018年4月24日
 */
public enum CouponsTypeModelEnum {

	FLOATINGAMOUNT("浮动金额", "2"), // 浮动金额
	FIXEDAMOUNT("固定金额", "1"),   // 固定金额
	;
	private String key;
	private String code;

	public String getCode() {
		return code;
	}

	private CouponsTypeModelEnum(String key, String code) {
		this.key = key;
		this.code = code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private CouponsTypeModelEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	final static Map<String, Object> map = new HashMap<>();

	public static Map<String, Object> getMap() {
		return map;
	}

	public void setKey(String key) {
		this.key = key;
	}

	static {
		for (CouponsTypeModelEnum couponsType : CouponsTypeModelEnum.values()) {
			map.put(couponsType.getKey(), couponsType.getCode());
		}
	}
	
	public static CouponsTypeModelEnum getNameByCode(String codeStr) {
		CouponsTypeModelEnum enums = null;
		for (CouponsTypeModelEnum tmp : CouponsTypeModelEnum.values()) {
			if (tmp.getCode().toString().equals(codeStr)) {
				enums = tmp;
				break;
			}
		}
		return enums;
	}

}
