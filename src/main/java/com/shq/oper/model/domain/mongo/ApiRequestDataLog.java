package com.shq.oper.model.domain.mongo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/** 接口请求数据记录 **/
@lombok.Data
@Document(collection="t_api_request_data_log")
public class ApiRequestDataLog implements Serializable {
	private static final long serialVersionUID = 1L;
	/****/
	@Id
	private String id;

	/** 访问路径 **/
	@Field("action")
	private String action;

	/** 引用路径 **/
	@Field("ref_action")
	private String refAction;

	/** IP地址 **/
	@Field("ref_ip")
	private String refIp;

	/** 创建时间 **/
	@Field("create_time")
	private LocalDateTime createTime;

	/** 请求方法Code **/
	@Field("action_code")
	private String actionCode;

	/** 请求系统来源 **/
	@Field("action_source")
	private String actionSource;
	
	/** 请求系统设备类型 **/
	@Field("action_device_type")
	private String actionDeviceType;
	
	/** 请求参数 **/
	@Field("action_para")
	private String actionPara;

	/** 返回参数 **/
	@Field("return_data")
	private String returnData;

	private String startTime;

	private String endTime;

}