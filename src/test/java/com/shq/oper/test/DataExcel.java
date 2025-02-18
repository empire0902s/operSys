package com.shq.oper.test;

import java.io.Serializable;

import lombok.Data;

@Data
public class DataExcel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String batch;
	private String phone;
	private String isMsg;
	private String isApp;
	private String isEmail;
	private String msgContent;
}
