package com.shq.oper.model.domain.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="order_detail")
@Data
public class OrderDetail {
	/**
	 * 订单详情ID
	 */
	@Id
	private String orderDetailId;

	/**
	 * 订单ID
	 */
	private String orderId;

	/**
	 * 商店ID
	 */
	private String goodsId;

	/**
	 * 统计
	 */
	private Integer count;

	/**
	 * 价格
	 */
	private Double price;

	/**
	 * 单位
	 */
	private String unit;

	/**
	 * 折扣价格
	 */
	private Double discountPrice;

	/**
	 * 属性
	 */
	private String attributes;

	/**
	 * 商品名
	 */
	private String goodsName;
	/**
	 * 商品code
	 */
	private String goodsCode;
	/**
	 * 规格
	 */
	private String spec;

	/**
	 * 订单详情总价
	 */
	private Double amount;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 评价内容
	 */
	private String evaluationContent;

	/**
	 * 图片url
	 */
	private String goodsImgUrl;

	/**
	 * 是否评价，true已评价，false未评价
	 */
	private Boolean bEvaluation;

	/**
	 * 优惠信息
	 */
	private String promotion;

	/**
	 * 订货系统sku
	 */
	private String orderSku;

	/**
	 * 当前下单用户类型 1 为消费者 ; 2 分销商 ; 3 代理商
	 */
	private String insertOrderType;

	/**
	 * 当前下单用户下单优惠金额
	 */
	private Double preferentialHowMany = 0d;

	/**
	 * 原始出厂单价
	 */
	private Double primitiveFactoryPrice;

	/**
	 * 出厂单价
	 */
	private Double cost_unit_price;

	/**
	 *  出厂价
	 */
	private Double factoryPrice;

	/**
	 * 子订单号==>关联订单(可以起查询到子订单的订单信息)
	 */
	private String subOrdersNo;

	/**
	 * 标识是什么商品："0":普通商品下单 ; "1":秒杀商品 ;
	 */
	private String isActivityGoods;

	/**
	 * 是否为分销商品
	 */
	private Boolean isDistrbutionGoods=false;

	/**
	 * 代理商利润
	 */
	private Double agentProfit;

	/**
	 * 分销商利润
	 */
	private Double distributorProfit;

	/**
	 * 平台价
	 */
	private Double platformPrice;

	/**
	 * 秒杀出厂单价
	 **/
	private Double seckillUnivalence;

	/**
	 * 秒杀平台供货单价
	 **/
	private Double seckillPlatPrice;

	/**
	 *活动商品标识
	 */
	private String activeColumnMark;

	/**
	 * 运单号(物流单号)
	 */
	private String waybill;

	/**
	 * 承运公司
	 */
	private String carrier;
	/**
	 * 最新物流信息
	 **/
	private String logistcsInfo;

	/**
	 * 当前下单用户单个商品优惠金额
	 */
	private Double singlePreferentialHowMany = 0d;

	public Boolean getbEvaluation() {
		return bEvaluation;
	}

	public void setbEvaluation(Boolean bEvaluation) {
		this.bEvaluation = bEvaluation;
	}
}