package com.shq.oper.model.domain.primarydb.deductible;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: ltg
 * @date: Created in 11:54 2018/11/17
 */
@Data
@Table(name = "t_deductible")
public class Deductible implements Serializable {
    private static final long serialVersionUID = 1L;

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
     * 用户seq
     */
    @Column(name = "user_seq")
    private String userSeq;

    /**
     * 有效开始时间
     */
    @Column(name = "validay_start")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validayStart;

    /**
     * 有效结束时间
     */
    @Column(name = "validay_end")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validayEnd;

    /**
     * 状态（1.未使用，2.使用中，3已用完，4.已过期，5.升级清空）
     */
    private Short status;

    /**
     * 抵扣券余额
     */
    private BigDecimal balance;

    /**
     * 已抵扣金额
     */
    @Column(name = "used_balance")
    private BigDecimal usedBalance;

    /**
     * 抵扣券总额
     */
    private BigDecimal amount;

    /**
     * 是否锁定（1.解锁，2锁定）
     */
    @Column(name = "is_locking")
    private Integer isLocking;

    /**
     * 1:代金券，2：抵扣券
     */
    private Integer type;

    /**
     * 抵扣百分比
     */
    @Transient
    private Float discount;



}
