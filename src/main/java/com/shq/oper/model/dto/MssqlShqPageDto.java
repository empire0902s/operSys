package com.shq.oper.model.dto;

import java.io.Serializable;

import javax.persistence.Transient;

import lombok.Data;

@Data
public class MssqlShqPageDto implements Serializable  {
	
	@Transient
	private Integer pageNumKey;
	@Transient
	private Integer pageSizeKey;
    private static final long serialVersionUID = 1L;
}
