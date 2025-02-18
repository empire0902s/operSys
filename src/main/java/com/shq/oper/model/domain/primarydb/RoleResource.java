package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Table(name = "t_role_resource")
public class RoleResource implements Serializable {
    /**
     * 角色-资源权限关系id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 资源权限id
     */
    @Column(name = "resource_id")
    private Long resourceId;

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

    private static final long serialVersionUID = 1L;
    
    @Transient
    private List<Long> resourceIdList;
    
}