package com.shq.oper.enums.api;


public enum ResponseResultCodeEnums {

	SUCCESS_CODE("操作成功", 200), 
	FAIL_CODE("操作失败", 400),
	;
	private String name;
	private Integer code;
	
	public String getName() {
		return name;
	}
	public Integer getCode() {
		return code;
	}
	private ResponseResultCodeEnums(String name, Integer code) {
		this.name = name;
		this.code = code;
	}
	
}
