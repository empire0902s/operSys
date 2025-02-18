package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Table(name = "t_coupons_area_rule")
public class CouponsAreaRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@OrderBy(value = "DESC")
	@Id
	@GeneratedValue(generator = "JDBC")
	private Long id;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	/***
	 * 
	 * 创建人(id-name)
	 */
	@Column(name = "create_admin")
	private String createAdmin;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;

	/**
	 * 最近更新人(id-name)
	 */
	@Column(name = "update_admin")
	private String updateAdmin;

	/**
	 * 是否禁用（0为否，1为是）
	 */
	@Column(name = "is_disabled")
	private Boolean isDisabled;

	/**
	 * 规则名称
	 */
	private String name;

	/**
	 * 区级别{1全国,2省级,3市级,4县区级,5街道级}
	 */
	private Integer lev;

	/**
	 * 区域类型{包含,排除}）
	 */
	@Column(name = "lev_type")
	private Boolean levType;

	/**
	 * 区域范围明细，标识字符串
	 */
	@Column(name = "lev_range_detail")
	private String levRangeDetail;

	/**
	 * 描述
	 */
	@Column(name = "area_desc")
	private String areaDesc;


}
