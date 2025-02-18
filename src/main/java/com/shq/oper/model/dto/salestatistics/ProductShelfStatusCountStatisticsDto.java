package com.shq.oper.model.dto.salestatistics;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 商品上下架数量统计Dto
 */
@lombok.Data
public class ProductShelfStatusCountStatisticsDto implements Serializable {
	private static final long serialVersionUID = 1L;

	// 上架数量
	private Integer isForSale;

	// 下架数量
	private Integer notForSale;

	// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;
	// 结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;

}
