package com.shq.oper.model.domain.primarydb.ticket;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 场次实体
 */
@lombok.Data
@Table(name = "t_field")
public class Field implements Serializable {
    /**
     * 场次id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 场次名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 场次状态（0 禁用 1 启用）
     */
    @Column(name = "status")
    private Short status;

    /**
     * 场地名称
     */
    @Column(name = "address_name")
    private String addressName;

    /**
     * 场次开始时间
     */
    @Column(name = "begin_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    /**
     * 场次结束时间
     */
    @Column(name = "end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 是否禁用（0为否，1为是）
     */
    @Column(name = "is_disabled")
    private Boolean isDisabled;

    /**
     * 场次说明
     */
    @Column(name = "field_desc")
    private String fieldDesc;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
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