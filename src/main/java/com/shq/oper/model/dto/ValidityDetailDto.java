package com.shq.oper.model.dto;

import java.io.Serializable;




@lombok.Data
public class ValidityDetailDto implements Serializable {
	/**
	 * 优惠券持续天数
	 */
	private Integer vali_day_num;
	/**
	 * 优惠券发放时间
	 */
	private String vali_day_start;
	/**
	 * 优惠券截至时间
	 */
	private String vali_day_end;

	private static final long serialVersionUID = 1L;

}