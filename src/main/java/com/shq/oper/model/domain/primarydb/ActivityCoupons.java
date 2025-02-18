package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

@lombok.Data
@Table(name = "t_activity_coupons")
public class ActivityCoupons implements Serializable {
    /**
     * 用户-角色关系Id
     */
    @Id
    private Long id;

    /**
     * 活动Id
     */
    @Column(name = "activity_id")
    private Long activityId;

    /**
     * 优惠券id
     */
    @Column(name = "coupons_id")
    private Long couponsId;

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

    private static final long serialVersionUID = 1L;
}