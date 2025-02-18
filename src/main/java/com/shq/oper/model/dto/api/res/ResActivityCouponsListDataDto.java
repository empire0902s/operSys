package com.shq.oper.model.dto.api.res;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "1008,1010返回活动优惠券列表DTO", description = "返回活动优惠券列表DTO")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ResActivityCouponsListDataDto implements Serializable {
	

	private String fromSys;	//来源系统
	
	private String fromSysCode;	//来源系统Code

	
	
	/**
	 *优惠券Id 
	 **/
	private String couponsId;	

    /**[1:领取,2:已领取,3:优惠券已结束]**/
    private String userReceiveStatus;
    private String userReceiveStatusName;
    
	/**
	 *对应活动批名次
	 **/
	private String actName;	
	
	/**
	 *对应活动批次号
	 **/
	private String actBatchNo;	
	
	/**
	 * 优惠券名称
	 */
	private String name;

	
	/**
	 * 发放开始时间
	 */
	private String sendTimeStartStr;

	/**
	 * 发放结束时间
	 */
	private String sendTimeEndStr;

	/**
	 * 发放数量0为不限量
	 */
	private String sendNum;

	/**已发放数量**/
	private String receiveNum;
	
	/**
	 * 用户领取规则。
	 */
	private String receiveNumRule;

	/**用户已领取数量**/
	private String receiveNumByUse;

	/**
	 * 优惠券说明
	 */
	private String couponDes;

	/**优惠券去使用跳转连接。为空默认跳转首页**/
	private String couponsHrefUrl;

	/**优惠券内容图片 为空使用默认图片**/
	private String contentImg;

	/**优惠券内容禁用图片 为空使用默认图片**/
	private String contentdisabledImg;
		
	
	/**
	 * 优惠券规则说明
	 */
	private String couponsTypeDesc;
	
    /**
     * 优惠类型{满减，折扣，立减}
     */
    private String couponsType;
    
    private String couponsTypeName;

    /**
     * 最低消费金额,满足后才可使用
     */
    private String minSpendMoney;


    /**
     * 满减_优惠减少金额
     */
    private String amtFullReduce;

    /**
     * 折扣
     */
    private String amtDiscount;

    /**
     * 立减金额
     */
    private String amtSub;
    
    /**
     * 有效期明细
     */
	private String valiDayTypeDetail;
	
	
	
	/**
	 * 优惠券持续天数
	 */
	private Integer valiDayNum;
	


	
	private static final long serialVersionUID = 1L;
}