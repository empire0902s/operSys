package com.shq.oper.model.domain.primarydb;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: ltg
 * @date: Created in 15:51 2018/6/7
 */
@lombok.Data
@Table(name = "t_activity_goods_rule_details")
public class ActivityGoodsRuleDetails {
    /**
     * 用户-角色关系Id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 商品规则Id
     */
    @Column(name = "activity_goods_rule_id")
    private Long activityGoodsRuleId;

    /**
     * 规则类型
     */
    @Column(name = "rule_type")
    private Integer ruleType;

    /**
     * 规则关联值(如商品则商品Code,如供应商则供应商code)
     */
    @Column(name = "ref_sign_value")
    private String refSignValue;

    /**
     * 规则本地数据Id
     */
    @Column(name = "ref_sign_local_id")
    private String refSignLocalId;


    /**
     * 规则本地数据Id
     */
    @Column(name = "ref_sign_name")
    private String refSignName;

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
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;

    private static final long serialVersionUID = 1L;
    
    
    @Transient
    private String goodsRuleName;	//规则名称
    @Transient
    private Integer goodsRuleType;	//规则类型
    @Transient
    private Boolean goodsRuleDisabled;	//规则是否禁用
    @Transient
    private String goodsRuleDes;	//规则描述
    
    @Transient
    private String isSale;	//商品上下架状态
    @Transient
    private List<Long> couponsGoodsRuleIdList;	//优惠券商品规则列表
    
    @Transient
    private String fromSys;	//系统来源
    @Transient
    private String fromSysCode;	//系统来源Code
}
