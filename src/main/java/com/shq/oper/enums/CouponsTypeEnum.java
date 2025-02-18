package com.shq.oper.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠方式类型
 * 
 * @author ljz
 * @date 2018年4月24日
 */
public enum CouponsTypeEnum {

	RANGEREDUCE("满减", "1"), // 满减
	ERECTREDUCE("立减", "2"),   //立减
	DISCOUNT("折扣", "3"),// 折扣
//	CASH("代金券", "4"),//
//	DEDUCTIBLE("抵扣券", "5"),//
	;
	private String key;
	private String code;

	public String getCode() {
		return code;
	}

	public static String getCode(String key) {

		return strmap.get(key);
	}

	private CouponsTypeEnum(String key, String code) {
		this.key = key;
		this.code = code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private CouponsTypeEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	final static Map<String, Object> map = new HashMap<>();
	final static Map<String, String> strmap = new HashMap<>();

	public static Map<String, Object> getMap() {
		return map;
	}

	public void setKey(String key) {
		this.key = key;
	}

	static {
		for (CouponsTypeEnum couponsType : CouponsTypeEnum.values()) {
			map.put(couponsType.getKey(), couponsType.getCode());
			strmap.put(couponsType.getKey(),couponsType.getCode());
		}
	}

	
	public static CouponsTypeEnum getNameByCode(String codeStr) {
		CouponsTypeEnum enums = null;
		for(CouponsTypeEnum tmp : CouponsTypeEnum.values()) {
			if(tmp.getCode().toString().equals(codeStr)) {
				enums =  tmp;
				break;
			}
		}
	return enums;
	}

}
