package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@lombok.Data
@Table(name = "t_activity_goods_rule_goods_product")
public class ActivityGoodsRuleGoodsProduct implements Serializable {
    /**
     * 用户-角色关系Id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 商品规则Id
     */
    @Column(name = "activity_goods_rule_id")
    private Long activityGoodsRuleId;

    /**
     * 商品Id
     */
    @Column(name = "goods_product_pid")
    private String goodsProductPid;

    /**
     * 规则类型
     */
    @Column(name = "rule_type")
    private Integer ruleType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  createTime;

    /**
     * 创建人(id-name)
     */
    @Column(name = "create_admin")
    private String createAdmin;

    private static final long serialVersionUID = 1L;
}