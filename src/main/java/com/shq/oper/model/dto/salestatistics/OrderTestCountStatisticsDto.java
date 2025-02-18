package com.shq.oper.model.dto.salestatistics;

import java.io.Serializable;


/**
 * 测试订单统计Dto
 */
@lombok.Data
public class OrderTestCountStatisticsDto implements Serializable {
	private static final long serialVersionUID = 1L;

	// 测试订单总数
	private Integer testToatalOrdersCount;

	// 测试订单总金额
	private Integer testTotalSalesAmount;
	

}
