package com.shq.oper.model.dto.salestatistics;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * 活动订单统计DTO
 * 
 * @author ljz
 *
 */
@Data
public class ActivityCommodityOrderStatisticsDto implements Serializable {

	private static final long serialVersionUID = 1L;
	// 活动名称
	private String activeColumnMark;
	// 购买用户数
	private String totalUserSeqCounts;

	// 二次购买用户数
	private String numberOfUsersPurchasedTwice;

	// 订单总数
	private Integer totalOrdersCounts;

	// 订单总金额
	private Double totalOrdersMoney;

	// 已下单未付款订单数
	private Integer totalNumberOfUnpaidOrder;

	// 已经下单未付款订单总金额
	private Double totalAmountOfUnpaidOrder;

	// 已下单已付款订单数
	private Integer hasPaysTotalOrdersCounts;

	// 已经下单已付款订单总金额
	private Double hasPaysOrdersTotalMoney;

	// 退款总金额(未发货，包含运费)
	private Double totalRefundAmountMoneyIncludesFreight;

	// 退款总金额(已发货，不退运费)
	private Double totalRefundAmountMoneyNotIncludesFreight;

	// 退款订单数(包括申请退款,退款成功，退款失败)
	private Integer totalrefundOrdersCounts;

	// 退款总金额(申请退款的全部订单金额总数)
	private Double totalRefundAmountMoney;

	// 已售商品数量(已付款订单)
	private Integer hasPaysOrdersGoodsCounts;

	// 退款商品数量
	private Integer amountOfRefunds;

	// 退款率=退款商品数量/总销售商品数量
	private Double refundRate;

	public Double getRefundRate() {
		if (this.hasPaysOrdersGoodsCounts != null && this.hasPaysOrdersGoodsCounts != 0) {
			return ((double) this.amountOfRefunds / this.hasPaysOrdersGoodsCounts);
		}
		return 0D;
	}

	// 客单价=总销售金额/总销售订单数
	private Double averageCustomerSpending;

	public Double getAverageCustomerSpending() {
		if (this.hasPaysOrdersTotalMoney != null && this.hasPaysTotalOrdersCounts != 0) {
			return ((double) this.hasPaysOrdersTotalMoney / this.hasPaysTotalOrdersCounts);
		}
		return 0D;
	}
	//测试订单总数
	private Integer testOrdersTotalCount;
	//测试订单总金额
	private Double testOrderTotalAmmount;
	//测试订单购买用户数
	private Integer seqCounts;

	// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;
	// 结束时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;

	private Integer pageNumKey;
	
	private Integer pageSizeKey;
}
