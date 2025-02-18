package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "t_coupons_type_rule")
public class CouponsTypeRule implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(generator = "JDBC")
	@OrderBy(value = "DESC")
	@Id
	private Long id;

	/**
	 * 金额规则名称
	 */
	private String name;

	/**
	 * 优惠券模式{1-固定金额,2-浮动金额}
	 */
	private Integer model;

	/**
	 * 优惠券类型{1-满减，2-立减，3-折扣}
	 */
	private Integer type;

	/**
	 * 最低消费金额,满足后才可使用
	 */
	@Column(name = "min_spend_money")
	private BigDecimal minSpendMoney;

	/**
	 * 满减_优惠减少金额
	 */
	@Column(name = "amt_full_reduce")
	private BigDecimal amtFullReduce;

	/**
	 * 立减金额
	 */
	@Column(name = "amt_sub")
	private BigDecimal amtSub;

	/**
	 * 折扣
	 */
	@Column(name = "amt_discount")
	private BigDecimal amtDiscount;

	/**
	 * 描述
	 */
	@Column(name = "type_desc")
	private String typeDesc;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	/**
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
     * 来源系统
     */
    @Column(name = "from_sys")
    private String fromSys;
    /**
     * 来源系统Code
     */
    @Column(name = "from_sys_code")
    private String fromSysCode;
    
    
    /**关联下拉框查询字段**/
    @Transient
    private String depend;
    
    
}
