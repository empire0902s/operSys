package com.shq.oper.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

@lombok.Data
public class CouponsDto implements Serializable {
	private Long id;

	/**
	 * 优惠券名称
	 */
	private String name;

	/**
	 * 优惠券批次
	 */
	private Long batch;

	/**
	 * 活动Id
	 */
	private String activityId;


	/**
	 * 可使用平台编码，可多个用,号隔开
	 */
	private String usePlatform;


	/**
	 * 发放开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime sendTimeStart;

	/**
	 * 发放结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime sendTimeEnd;

	/**
	 * 有效期方式,0指定时间,1领取后天数
	 */
	private Integer valiDayType;

	/**
	 * 有效期明细{vali_day_start,vali_day_end}{vali_day_num}
	 */
	private String valiDayTypeDetail;

	/**
	 * 发放数量0为不限量
	 */
	private Integer sendNum;

	/**
	 * 发放方式,0系统自动发放到用户,1,用户领取
	 */
	private Integer sendType;

	/**
	 * 优惠券说明
	 */
	private String couponDes;
	/**
	 * 优惠券满多少
	 */
	private Integer couponsType_enough;
	/**
	 * 减多少
	 */
	private Integer couponsType_reduce;
	/**
	 * 折扣
	 */
	private Integer discount;
	

	private static final long serialVersionUID = 1L;

}