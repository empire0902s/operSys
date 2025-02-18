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


@lombok.Data
@lombok.NoArgsConstructor
@Table(name = "t_activity")
public class Activity implements Serializable {
	
	
	public Activity(String batch,Boolean isDisabled) {
		super();
		this.batch = batch;
		this.isDisabled = isDisabled;
	}


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
     * 规则名称
     */
    private String name;

    /**
     * 批次
     */
    private String batch;

    /**
     * 活动宣传标题
     */
    @Column(name = "title")
    private String title;
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
     * 活动商品规则Id
     */
    @Column(name = "activity_goods_rule_id")
    private Long activityGoodsRuleId;

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
     * 用户角色规则
     */
    @Column(name = "user_role_rule")
    private Short userRoleRule;

    /**
     * 用户领取次数0不限次数，限X次
     */
    @Column(name = "receive_num_rule")
    private Integer receiveNumRule;

    /**
     * 活动说明
     */
    @Column(name = "activity_desc")
    private String activityDesc;

    /**
     * 活动类型(1:优惠券活动,2:商品活动)
     */
    @Column(name = "use_Type")
    private Integer useType;

    private static final long serialVersionUID = 1L;
    
    
    /**
     * 活动商品规则名称
     */
    @Transient
    private Long activityGoodsRuleName;

    @Transient
    private String brandShopSeq;

    /**关联下拉框查询字段**/
    @Transient
    private String depend;
    
}