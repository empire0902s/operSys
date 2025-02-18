package com.shq.oper.model.dto.salestatistics;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品数据(交易金额与销售量)排名统计DTO
 * 
 * @author ljz
 *
 */
@Data
public class CommodityStatisticsRankingDto implements Serializable {

	private static final long serialVersionUID = 1L;

	// 排名
	private short sort;

	// 商品code
	private String goodsCode;

	// 商品名称
	private String goodsName;

	// 供应商名称
	private String companyName;

	// 销售金额
	private Double amt;

	public Integer getNotIncludeTestSellNums() {
		if (this.sellNums != null && this.sellNums != 0&&this.testAmt!=null) {
			return this.sellNums - this.testSellNums;
		} else {
			return 0;
		}
	}

	public Double getNotIncludeTestAmt() {
		if (this.amt != null && this.amt != 0&&this.testAmt!=null) {
			return this.amt - this.testAmt;
		} else {
			return 0D;
		}
	}

	// 商品销售数量
	private Integer sellNums;

	// 商品点击数
	private Integer exhibitCount;

	// 购买转化率
	private Double purchaseConversionRate;

	// 测试订单商品销售总数(只计算字段address是以‘测试’开头的的商品数量)
	private Integer testSellNums;

	// 测试订单商品销售金额(只计算字段address是以‘测试’开头的的商品金额)
	private Double testAmt;

	// 测试订单商品销售总数(只计算字段address是以‘测试’开头的的商品数量)
	private Integer notIncludeTestSellNums;

	// 测试订单商品销售金额(只计算字段address是以‘测试’开头的的商品金额)
	private Double notIncludeTestAmt;

	// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;
	// 结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;

	public Float getPurchaseConversionRate() {
		if (this.exhibitCount != null && this.exhibitCount != 0) {
			return (Float) (this.sellNums / (float) this.exhibitCount);
		} else {
			return 1f;
		}
	}

	private Integer pageNumKey;

	private Integer pageSizeKey;

}
