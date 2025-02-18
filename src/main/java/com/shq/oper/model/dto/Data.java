package com.shq.oper.model.dto;

import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * Controller 返回页面List数据封装类
 * @author tjun
 *
 * @param <T>
 */
public class Data<T> {

	private String code;
	
	private List<T> data;
	
	private long count;
	
	private String msg;

	public Data() {
		super();
	}

	public Data(String code, List<T> data, long count, String msg) {
		super();
		this.code = code;
		this.data = data;
		this.count = count;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public static <T> Data<T> ok(PageInfo<T> value) {
		return new Data<T>("0", value.getList(), value.getTotal(), "");
	}
	public static <T> Data<T> ok(Page<T> value) {
		return new Data<T>("0", value, value.getTotal(), "");
	}
	public static <T> Data<T> error(String msg) {
		return new Data<T>("false", null, 0, msg);
	}
}
