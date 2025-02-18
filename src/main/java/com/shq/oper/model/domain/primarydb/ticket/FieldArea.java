package com.shq.oper.model.domain.primarydb.ticket;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 场次区域实体
 * @author: ltg
 * @date: Created in 14:52 2018/10/16
 */
@lombok.Data
@Table(name = "t_field_area")
public class FieldArea implements Serializable {
    private static final long serialVersionUID = 1L;

    @OrderBy(value = "DESC")
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     *场次Id
     */
    @Column(name = "field_id")
    private Long fieldId;

    /**
     * 区域名称
     */
    @Column(name = "name")
    private String name;

    /**
     *区域人数
     */
    @Column(name = "num")
    private Integer num;

    /**
     * 是否禁用（0为否，1为是）
     */
    @Column(name = "is_disabled")
    private Boolean isDisabled;

    /**
     * 区域说明
     */
    @Column(name = "field_area_desc")
    private String fieldAreaDesc;

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
    
    @Transient
    private String fieldName;
    @Transient
    private String addressName;

}
