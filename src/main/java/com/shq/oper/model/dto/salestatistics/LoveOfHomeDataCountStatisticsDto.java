package com.shq.oper.model.dto.salestatistics;

import java.io.Serializable;


/**
 * 爱之家统计Dto
 */
@lombok.Data
public class LoveOfHomeDataCountStatisticsDto implements Serializable {
	private static final long serialVersionUID = 1L;

	//爱之家注册用户数
	private Integer loveOfHomeUserCounts;

	// 爱之家购买用户数
	private Integer totalOrderPurchasesUser;
	
	// 爱之家二次购买用户数
	private Integer twoPurchasesOfUsers;
	

}
