package com.shq.oper.model.dto.api;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.alibaba.fastjson.annotation.JSONField;
import com.shq.oper.enums.api.ResponseResultCodeEnums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 接口响应对应
 */

@ApiModel(value="接口返回通用对象")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class BaseResponseResultDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "返回状态[200:正常,400:异常]", name="code",example="200",allowableValues="200,400")
	private String code;	//正常
	
	@ApiModelProperty(value = "返回状态描述消息", name="msg",example="操作成功")
	private String msg;	
	
	@ApiModelProperty(value = "返回结果对象", name="result",example="")
	private Object result;	
	
	
	@ApiModelProperty(value = "返回时间", name="responseTime",example="2018-05-05 15:15:15")
	private String responseTime;
	
	@JSONField(serialize=false)  
	@ApiModelProperty(hidden=true)
	public boolean isSuccessCodeBy200() {
		boolean bool = false;
		if(String.valueOf(ResponseResultCodeEnums.SUCCESS_CODE.getCode()).equals(this.code)) {
			bool = true;
		}
		return bool;
	}
	
	@JSONField(serialize=false)  
	@ApiModelProperty(hidden=true)
	public LocalDateTime getResponseTimeByLocal() {
		return LocalDateTime.now();
	}

	public BaseResponseResultDto(ResponseResultCodeEnums responseResultCodeEnums) {
		super();
		this.code=responseResultCodeEnums.getCode().toString();
		this.msg=responseResultCodeEnums.getName();
	}
	
	
	
}