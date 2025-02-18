package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Table(name = "t_role")
public class Role implements Serializable {
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
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 备注
     */
    private String remark;

    /**
     * 分类
     */
    private String type;

    private static final long serialVersionUID = 1L;
}