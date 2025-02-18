package com.shq.oper.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

@lombok.Data
public class ValiDayDetailDto implements Serializable {
	/**
	 * 优惠券持续天数
	 */
	private Integer days;
	/**
	 * 优惠券发放时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timeBegin;
	/**
	 * 优惠券截至时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timeEnd;

	private static final long serialVersionUID = 1L;

}