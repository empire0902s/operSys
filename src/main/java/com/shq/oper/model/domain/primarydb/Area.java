package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;

@Data
@Table(name = "t_area")
public class Area implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 层级，1=省，2=市，3=区
     */ 
    private String lev;

    /**
     * 上级代码
     */
    @Column(name = "parent_code")
    private String parentCode;
    
    /**
     * 是否禁用（0为否，1为是）
     */
    @Column(name = "is_disabled")
    private Boolean isDisabled;
    
    private static final long serialVersionUID = 1L;
    
    @Transient
    private String parentName;
    
    /**
     * 备注
     */
    @Transient
    private String remark;
}