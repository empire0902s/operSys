package com.shq.oper.model.domain.primarydb.ticket;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 门票实体
 * @author: ltg
 * @date: Created in 15:23 2018/10/16
 */
@lombok.Data
@Table(name = "t_ticket")
public class Ticket implements Serializable {

    @Id
    private Long id;

    /**
     * 门票名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 门票数量
     */
    @Column(name = "num")
    private Integer num;

    /**
     * 区域id
     */
    @Column(name = "area_id")
    private Long areaId;

    /**
     * 场地id
     */
    @Column(name = "field_id")
    private Long fieldId;

    /**
     * 门票状态
     */
    @Column(name = "status")
    private Short status;

    /**
     * 优惠券说明
     */
    @Column(name = "desc")
    private String desc;

    /**
     * 是否禁用（0为否，1为是）
     */
    @Column(name = "is_disabled")
    private Boolean isDisabled;

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
     * 价格
     */
    @Column(name = "price")
    private Double price;

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

    private static final long serialVersionUID = 1L;

}
