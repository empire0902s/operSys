package com.shq.oper.enums;

/**
 * 资源类型
 * @author tjun
 */
public enum ResourceTypeEnum {

	MENU(1),// 菜单
	FUN(2),// 功能
	;
	
	private Integer type;
	private ResourceTypeEnum(Integer type){
		this.type = type;
	}
	
	public boolean is(Integer type){
		return this.type.intValue() == type.intValue();
	}

	public Integer getType() {
		return type;
	}
	
	
}
