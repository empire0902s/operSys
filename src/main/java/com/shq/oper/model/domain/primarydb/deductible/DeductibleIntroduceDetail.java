package com.shq.oper.model.domain.primarydb.deductible;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: ltg
 * @date: Created in 13:58 2018/11/17
 */
@Data
@Table(name = "t_deductible_introduce_detail")
public class DeductibleIntroduceDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @OrderBy(value = "DESC")
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 规则id
     */
    @Column(name = "introduce_id")
    private Long introduceId;

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
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    private Integer sort;


}
