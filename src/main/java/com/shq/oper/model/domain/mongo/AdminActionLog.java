package com.shq.oper.model.domain.mongo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/** 管理员操作记录表 **/
@lombok.Data
@Document(collection="t_admin_action_log")
public class AdminActionLog implements Serializable {
	private static final long serialVersionUID = 1L;
	/****/
	@Id
	private String id;

	/** 管理员ID **/
	@Field("admin_id")
	private Long adminId;
	
	/** 管理员Name **/
	@Field("admin_name")
	private String adminName;

	/** 访问路径 **/
	@Field("action")
	private String action;

	/** 引用路径 **/
	@Field("ref_action")
	private String refAction;

	/** IP地址 **/
	@Field("ip")
	private String ip;

	/** 参数 **/
	@Field("action_para")
	private String actionPara;

	/** 创建时间 **/
	@Field("create_time")
	private LocalDateTime createTime;

	private String startTime;

	private String endTime;

}