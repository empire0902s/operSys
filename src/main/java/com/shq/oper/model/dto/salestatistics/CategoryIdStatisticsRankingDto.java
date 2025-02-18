package com.shq.oper.model.dto.salestatistics;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.shq.oper.model.domain.shq520new.DistrFirstCategoty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 一级类目数据(交易额与销量)排名统计DTO
 * 
 * @author ljz
 *
 */
@Data
public class CategoryIdStatisticsRankingDto implements Serializable {

	private static final long serialVersionUID = 1L;
	// 排名
	private short sort;

	// 一级类目id
	private String categoryId;

	// 商品名称
	private String categoryIdName;

	// 销售金额
	private Double amt;

	// 商品销售数量
	private Integer sellNums;

	// 测试订单供销商销售总数(只计算字段address是以‘测试’开头的的商品数量)
	private Integer testSellNums;

	// 测试订单供销商销售金额(只计算字段address是以‘测试’开头的的商品金额)
	private Double testAmt;

	// 测试订单供销商销售总数(只计算字段address是以‘测试’开头的的商品数量)
	private Integer notIncludeTestSellNums;

	// 测试订单供销商销售金额(只计算字段address是以‘测试’开头的的商品金额)
	private Double notIncludeTestAmt;

	public Integer getNotIncludeTestSellNums() {
		if (this.sellNums != null && this.sellNums != 0 && this.testSellNums != null) {
			return this.sellNums - this.testSellNums;
		} else {
			return 0;
		}
	}

	public Double getNotIncludeTestAmt() {
		if (this.amt != null && this.amt != 0 && this.testAmt != null) {
			return this.amt - this.testAmt;
		} else {
			return 0D;
		}
	}

	// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;
	// 结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;

	private Integer pageNumKey;

	private Integer pageSizeKey;
}
