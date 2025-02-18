package com.shq.oper.model.domain.primarydb;

import lombok.Data;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Table(name = "t_publice_num")
public class PubliceNum implements Serializable {
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
     * 名称
     */
    private String name;

    /**
     * 连接地址
     */
    @Column(name = "connection_adress")
    private String connectionAdress;

    /**
     * 浮窗文案
     */
    @Column(name = "floating_content")
    private String floatingContent;

    /**
     * 二维码图
     */
    @Column(name = "code_img")
    private String codeImg;
    
    /**
     * 是否禁用（0为否，1为是）
     */
    @Column(name = "is_disabled")
    private Boolean isDisabled;
    
    private static final long serialVersionUID = 1L;

}