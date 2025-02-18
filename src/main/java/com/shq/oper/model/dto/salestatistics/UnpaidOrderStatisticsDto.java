package com.shq.oper.model.dto.salestatistics;

import java.io.Serializable;

/**
 * 未付款订单统计DTO
 * 
 * @author ljz
 *
 */
@lombok.Data
public class UnpaidOrderStatisticsDto implements Serializable {
	private static final long serialVersionUID = 1L;

	// 未付款订单总数
	private Integer totalNumberOfUnpaidOrder;

	// 未付款订单总金额
	private Integer totalAmountOfUnpaidOrder;

}
