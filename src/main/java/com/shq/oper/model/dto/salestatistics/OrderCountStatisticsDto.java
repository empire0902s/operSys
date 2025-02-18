package com.shq.oper.model.dto.salestatistics;

import java.io.Serializable;


/**
 * 订单统计Dto
 */
@lombok.Data
public class OrderCountStatisticsDto implements Serializable {
	private static final long serialVersionUID = 1L;

	// 订单总数
	private Integer toatalOrdersCount;

	// 订单总金额
	private Integer totalSalesAmount;
	
	// 商品总销售量
	private Integer totalSalesCount;
	

}
