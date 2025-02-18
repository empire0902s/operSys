package com.shq.oper.model.dto.api.res;

import java.io.Serializable;


@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ResBaseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer code;	//来源系统
	
	private String message;	//来源系统Code

    private Object data;

}