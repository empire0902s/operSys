package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

@Data
@Table(name = "t_coupons")
public class Coupons implements Serializable {
	@Id
	private Long id;

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
    
	/**
	 * 优惠券名称
	 */
	private String name;

	/**
	 * 优惠券批次
	 */
	private String batch;

	/**
	 * 活动Id
	 */
	@Column(name = "activity_id")
	private String activityId;

	/**
	 * 优惠券品类Id//活动商品规则Id
	 */
	@Column(name = "category_rule_id")
	private BigDecimal categoryRuleId;

	/**
	 * 优惠规则Id
	 */
	@Column(name = "coupons_rule_id")
	private String couponsRuleId;
	/**
	 * 可使用平台编码，可多个用,号隔开
	 */
	@Column(name = "use_platform")
	private String usePlatform;
	/**
	 * 发放开始时间
	 */
	@Column(name = "send_time_start")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime sendTimeStart;

	/**
	 * 发放结束时间
	 */
	@Column(name = "send_time_end")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime sendTimeEnd;

	/**
	 * 有效期方式,0指定时间,1领取后天数
	 */
	@Column(name = "vali_day_type")
	private Integer valiDayType;

	/**
	 * 有效期明细{vali_day_start,vali_day_end}{vali_day_num}
	 */
	@Column(name = "vali_day_type_detail")
	private String valiDayTypeDetail;

	/**
	 * 发放数量0为不限量
	 */
	@Column(name = "send_num")
	private Integer sendNum;

	/**
	 * 已领取数量
	 */
	@Column(name = "receive_num")
	private Integer receiveNum;

	/**
	 * 发放方式,0系统自动发放到用户,1,用户领取
	 */
	@Column(name = "send_type")
	private Integer sendType;

	/**
	 * 优惠券说明
	 */
	@Column(name = "coupon_des")
	private String couponDes;

	/**
	 * 用户领取规则
	 */
	@Column(name = "receive_num_rule")
	private String receiveNumRule;

	@Transient
	private String couponsCategoryRuleName;

	@Transient
	private String couponsTypeRuleName;

	/**
	 * 财务审核人
	 */
	@Column(name = "finan_auditor")
	private String finanAuditor;
	
	/**
	 * 财务审核人时间
	 */
	@Column(name = "finan_auditTime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime finanAuditTime;
	
	/**
	 * 财务审核备注
	 */
	@Column(name = "finan_audit_remark")
	private String finanAuditRemark;
	
	/**
	 * 财务审核状态
	 */
	@Column(name = "finan_status")
	private Integer finanStatus;
	
	/**
	 * 内容图片
	 */
	@Column(name="content_img")
	private String contentImg;
	
	/**
	 * 禁用图片
	 */
	@Column(name="content_disabled_img")
	private String contentDisabledImg;
	
	
	/**
	 * 优惠券去使用跳转链接
	 */
	@Column(name="coupons_href_url")
	private String couponsHrefUrl;
	
    /**
     * 优惠模式{1固定金额,2浮动金额}
     */
	@Transient
    private Integer couponsTypeModel;

    /**
     * 优惠类型{满减，折扣，立减}
     */
	@Transient
    private Integer couponsType;

    /**
     * 最低消费金额,满足后才可使用
     */
	@Transient
    private BigDecimal minSpendMoney;

    /**
     * 满减_满足金额
     */
	@Transient
    private BigDecimal amtFullOver;

    /**
     * 满减_优惠减少金额
     */
	@Transient
    private BigDecimal amtFullReduce;

    /**
     * 折扣
     */
	@Transient
    private Integer amtDiscount;

    /**
     * 立减金额
     */
	@Transient
    private BigDecimal amtSub;

    /**
     * 金额规则
     */
	@Transient
    private String couponsTypeDesc;

    /**
     * 活动商品规则名称
     */
	@Transient
    private String categoryRuleName;

	@Transient
	private Integer sellNum;

	@Transient
	private String actBatchNo;	//对应活动批次号

	@Transient
	private String timeRange;	//时间范围

	@Transient
	private String brandShopSeq;

	@Transient
	private String activityName;

	private static final long serialVersionUID = 1L;
}