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
@Table(name = "t_coupons_user")
public class CouponsUser implements Serializable {

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
	 * 用户标识
	 */
	@Column(name = "user_seq")
	private String userSeq;

	/**
	 * 活动Id
	 */
	@Column(name = "activity_id")
	private String activityId;

	/**
	 * 优惠券关联
	 */
	@Column(name = "coupons_id")
	private String couponsId;

	/**
	 * 来源,1000:来自赠送
	 */
	@Column(name = "receive_src")
	private String receiveSrc;

	/**
	 * 有效期开始时间
	 */
	@Column(name = "vali_day_start")
	private LocalDateTime valiDayStart;

	/**
	 * 有效期结束时间
	 */
	@Column(name = "vali_day_end")
	private LocalDateTime valiDayEnd;
	
    /**
     * 优惠模式{1固定金额,2浮动金额}
     */
	@Column(name = "coupons_type_model")
    private Integer couponsTypeModel;

    /**
	 * RANGEREDUCE("满减", "1"), // 满减
	 * 	ERECTREDUCE("立减", "2"),   //立减
	 * 	DISCOUNT("折扣", "3"),// 折扣
	 * 	CASH("代金券", "4"),//
	 * 	DEDUCTIBLE("抵扣券", "5"),//
     */
    @Column(name = "coupons_type")
    private Integer couponsType;

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
     * 折扣
     */
    @Column(name = "amt_discount")
    private Integer amtDiscount;

    /**
     * 立减金额
     */
    @Column(name = "amt_sub")
    private BigDecimal amtSub;

    /**
     * 描述
     */
    @Column(name = "coupons_type_desc")
    private String couponsTypeDesc;


	/**
	 * 优惠券状态枚举,1未使用,2锁定中,3已使用,4已过期
	 */
	@Column(name = "coupons_status")
	private Integer couponsStatus;

	/**
	 * 使用时间
	 */
	@Column(name = "use_time")
	private LocalDateTime useTime;

	/**
	 * 使用的单号
	 */
	@Column(name = "use_order")
	private String useOrder;

	/**
	 * 订单原金额
	 */
	@Column(name = "use_order_money")
	private BigDecimal useOrderMoney;

	/**
	 * 订单优惠金额
	 */
	@Column(name = "use_spend_money")
	private BigDecimal useSpendMoney;

	/**
	 * 使用描述
	 */
	@Column(name = "use_desc")
	private String useDesc;
	/**
	 * 用户手机号
	 */
	@Column(name = "phone")
	private String phone;

	/**
	 * 抵扣券余额
	 */
	@Column(name = "balance")
	private BigDecimal balance;

	/**
	 * 已抵扣余额
	 */
	@Column(name = "used_balance")
	private BigDecimal usedBalance;

	/**
	 * 代金券金额和抵扣券总额
	 */
	@Column(name = "amount")
	private BigDecimal amount;

	/**
	 * 赠送人seq
	 */
	private String giveSeq;

	/**
	 * 赠送优惠券名
	 */
	private String giveCouponsName;

	/**
	 * 优惠券名称
	 */
	@Transient
	private String couponsName;


    /**
     * 满减_满足金额
     */
//    @Column(name = "amt_full_over")
    @Transient
    @Deprecated
    private BigDecimal amtFullOver;
    
	private static final long serialVersionUID = 1L;
	
	 
	/**
	 * 优惠活动商品规则
	 */
	@Transient
	private String activityGoodsRuleId;	
		
	 /**
	 * 规则类型｛组合商品,1,2,3,4级类目｝
	 */
	 @Transient
	private String goodsRuleType;	
	 
	 /**
	 * 活动名称
	 */
	 @Transient
	private String actName;




	
	
}
