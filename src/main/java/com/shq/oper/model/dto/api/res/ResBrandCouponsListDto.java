package com.shq.oper.model.dto.api.res;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Transient;

import io.swagger.annotations.ApiModel;



@ApiModel(value="返回品牌广场活动优惠券列表DTO")
@lombok.Data
public class ResBrandCouponsListDto  implements Serializable{

	private String id;

	/**
	 * 是否禁用（0为否，1为是）
	 */
	private Boolean isDisabled;

    /**
     * 来源系统
     */
    private String fromSys;
    /**
     * 来源系统Code
     */
    private String fromSysCode;
    
	/**
	 * 优惠券名称
	 */
	private String name;


	/**
	 * 活动Id
	 */
	private String activityId;

//	/**
//	 * 优惠规则Id
//	 */
//	private String couponsRuleId;

	/**
	 * 发放开始时间
	 */
	private String sendTimeStart;

	/**
	 * 发放结束时间
	 */
	private String sendTimeEnd;

	/**
	 * 有效期方式,0指定时间,1领取后天数
	 */
//	private Integer valiDayType;

	private String valiDayStart;
	private String valiDayEnd;
//	private String valiDayNum;

	//明细
	private String valiDayTypeDetail;
	

	/**
	 * 发放数量0为不限量
	 */
	private Integer sendNum;

	/**
	 * 已领取数量
	 */
	private Integer receiveNum;


	/**
	 * 优惠券说明
	 */
	private String couponDes;

	/**
	 * 用户领取规则
	 */
	private String receiveNumRule;

//	@Transient
//	private String couponsTypeRuleName;

	
	/**
	 * 内容图片
	 */
//	private String contentImg;
	
	/**
	 * 禁用图片
	 */
	private String contentDisabledImg;
	
	
	/**
	 * 优惠券去使用跳转链接
	 */
//	private String couponsHrefUrl;
	

    /**
     * 优惠类型{满减，折扣，立减}
     */
    private Integer couponsType;

    /**
     * 最低消费金额,满足后才可使用
     */
    private BigDecimal minSpendMoney;


    /**
     * 满减_优惠减少金额
     */
    private BigDecimal amtFullReduce;

    /**
     * 折扣
     */
    private Integer amtDiscount;

    /**
     * 立减金额
     */
    private BigDecimal amtSub;

    /**
     * 金额规则
     */
    private String couponsTypeDesc;


	
	private static final long serialVersionUID = 1L;
}