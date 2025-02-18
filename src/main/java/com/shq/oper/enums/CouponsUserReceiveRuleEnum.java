package com.shq.oper.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用户领取优惠券规则
 */
public enum CouponsUserReceiveRuleEnum {

	/** "用户领取 总个数限制  */
	RECEIVE_ALL_NUM("receiveAllNum", 0), 
	
	/** 用户每天领取个数限制 **/
	DAY_USER_RECEIVE_NUM("dayUserReceiveNum", 1), // 满减
	;
	private String key;
	private Integer code;

	public Integer getCode() {
		return code;
	}

	private CouponsUserReceiveRuleEnum(String key, Integer code) {
		this.key = key;
		this.code = code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	private CouponsUserReceiveRuleEnum(String key) {
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
		for (CouponsUserReceiveRuleEnum couponsType : CouponsUserReceiveRuleEnum.values()) {
			System.out.println(couponsType.getKey());
			System.out.println(couponsType.getCode());
			map.put(couponsType.getKey(), couponsType.getCode());
		}
	}

	public static CouponsUserReceiveRuleEnum getNameByCode(String codeStr) {
		CouponsUserReceiveRuleEnum enums = null;
		for (CouponsUserReceiveRuleEnum tmp : CouponsUserReceiveRuleEnum.values()) {
			if (tmp.getCode().toString().equals(codeStr)) {
				enums = tmp;
				break;
			}
		}
		return enums;
	}
}
