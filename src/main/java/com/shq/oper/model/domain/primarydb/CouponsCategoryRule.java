package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

@Data
@Table(name = "t_coupons_category_rule")
public class CouponsCategoryRule implements Serializable {
	
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
     * 品类规则名称
     */
    @Column(name = "category_name")
    private String categoryName;

    /**
     * 品类范围{1234级类目,活动类型,组合单品 }
     */
    @Column(name = "category_range")
    private String categoryRange;

    /**
     * 品类范围明细，标识字符串
     */
    @Column(name = "category_range_detail")
    private String categoryRangeDetail;

    /**
     * 使用描述
     */
    @Column(name = "category_desc")
    private String categoryDesc;

    private static final long serialVersionUID = 1L;
}