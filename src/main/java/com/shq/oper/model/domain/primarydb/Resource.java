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

import lombok.Data;

@Data
@Table(name = "t_resource")
public class Resource implements Serializable {
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
     * 系统代码
     */
    @Column(name = "system_code")
    private String systemCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 资源url路径
     */
    private String url;

    /**
     * 排序
     */
    private Short sort;

    /**
     * 类型，1=菜单，2=功能
     */
    private Byte type;

    /**
     * 备注
     */
    private String remark;

    private String icon;

    @Column(name = "parent_id")
    private Long parentId;

    private static final long serialVersionUID = 1L;
    
    @Transient
    private String parentName;
    
    @Transient
    private Long adminId;
    
    
}