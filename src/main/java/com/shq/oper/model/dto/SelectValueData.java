package com.shq.oper.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/***
 * 封装 页面Select 自动加载数据类
 * <br> 
 * 页面 select 配置  th:url="@{/url/selectValue}" lay-search
 * <br> 
 * JS     $("select[url]").loadSelect();
 * @author tjun
 * @param <T>
 */
@Data
@AllArgsConstructor
public class SelectValueData<T> {
	private static final long serialVersionUID = 1L;
	private String name;
	private T value;
}
