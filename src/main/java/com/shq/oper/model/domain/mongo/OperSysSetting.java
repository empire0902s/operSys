package com.shq.oper.model.domain.mongo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** 砍价规则 **/
@lombok.Data
@Document(collection="t_opersys_setting")
public class OperSysSetting implements Serializable {
	private static final long serialVersionUID = 1L;
	/****/
	@Id
	private String id;

	private String updateTime;
	
	private String updateAdmin;

	private String showCode = "";	

	private Object jsonValue = "";	
	
	private String parentObjectId = "";	
	
	
	
}