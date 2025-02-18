package com.shq.oper.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 消息通知类
 * 
 * @author tjun
 */
@Data
@AllArgsConstructor
public class Msg<T> {
	private boolean ok;// 是否成功
	private String msg;// 提示消息
	private T value;// 提示值
	private Integer code;// 消息代码
	
	public Msg(boolean ok, String msg) {
		super();
		this.ok = ok;
		this.msg = msg;
	}
	
	public static Msg<String> error(String msg) {
		return error(msg, null);
	}
	
	
	public static <T> Msg<T> error(String msg, T value) {
		return new Msg<T>(false, msg, value, null);
	}
	
	
	public static Msg<String> ok() {
		return new Msg<String>(true, null, null, null);
	}
	
	public static <T> Msg<T> ok(T value) {
		return ok(value, null);
	}
	public static <T> Msg<T> ok(String msg,T value) {
		return new Msg<T>(true, msg, value, null);
	}
	
	public static <T> Msg<T> ok(T value, Integer code) {
		return new Msg<T>(true, null, value, code);
	}
}
