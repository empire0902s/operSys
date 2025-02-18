package com.shq.oper.model.dto.salestatistics;

import java.io.Serializable;

/**
 * 赠送优惠券日统计
 * @author: ltg
 * @date: Created in 11:24 2018/10/18
 */
@lombok.Data
public class GiveCouponsStatisticsDto implements Serializable {

    /**
     * 总发起赠送次数
     */
    private Integer frequency;

    /**
     * 总发起人数
     */
    private Integer num;

    /**
     *总发放额度
     */
    private Double total;

    /**
     *赠送优惠券已使用额度
     */
    private Double usedTotal;

    /**
     *赠送优惠券已过期额度
     */
    private Double outTotal;

    private static final long serialVersionUID = 1L;
}
