package com.shq.oper.model.dto.salestatistics;

import java.io.Serializable;

/**
 * 赠送优惠券日统计
 * @author: ltg
 * @date: Created in 11:24 2018/10/18
 */
@lombok.Data
public class GiveCouponsDayStatisticsDto implements Serializable {

    /**
     * 日期
     */
    private String date;

    /**
     * 日赠送次数
     */
    private Integer frequency;

    /**
     *日赠送人数
     */
    private Integer num;

    /**
     * 日发起赠送人数
     */
    private Integer giveNum;

    /**
     *日赠送金额
     */
    private Double total;

    private static final long serialVersionUID = 1L;
}
