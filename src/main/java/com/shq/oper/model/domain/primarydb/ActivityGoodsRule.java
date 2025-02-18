package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

@lombok.Data
@Table(name = "t_activity_goods_rule")
public class ActivityGoodsRule implements Serializable {
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
     * 规则名称
     */
    private String name;

    /**
     * 规则类型｛组合商品,1,2,3,4级类目｝
     */
    private Integer type;


    /**
     * 规则说明
     */
    @Column(name = "rule_des")
    private String ruleDes;

    private static final long serialVersionUID = 1L;
    
    /**关联下拉框查询字段**/
    @Transient
    private String depend;
    
}