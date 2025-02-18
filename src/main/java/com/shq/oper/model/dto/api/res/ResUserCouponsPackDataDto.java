package com.shq.oper.model.dto.api.res;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.BaseResponseResultDto;
import com.shq.oper.util.JsonUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "1002,1003返回用户优惠券卡包对象Dto", description = "返回用户优惠券卡包对象Dto")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ResUserCouponsPackDataDto implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@ApiModelProperty(value = "来源系统")
	private String fromSys;	//来源系统
	
	@ApiModelProperty(value = "来源系统Code")
	private String fromSysCode;	//来源系统Code

	/**
	 * 优惠券名称
	 */
	private String couponsName;	

	
	/**
	 * 用户标识
	 */
	private String userSeq;	

	
	private String couponsUserId;	

	/**
	 * 优惠券关联
	 */
	private String couponsId;	

	/**
	 * 优惠活动商品规则
	 */
	private String activityGoodsRuleId;	
	
	/**
	 * 规则类型｛组合商品,1,2,3,4级类目｝
	 */
	private String goodsRuleType;	


	/**活动批次号**/
	private String activityBatchNo;
	/**活动名称**/
	private String activityName;	


	/**
	 * 有效期开始时间
	 */
	private String valiDayStart;	

	/**
	 * 有效期结束时间
	 */
	private String valiDayEnd;	

	/**
	 * 优惠券状态枚举,1未使用,2锁定中,3已使用,4已过期
	 */
	private String couponsStatus;	
	/**优惠券状态枚举,1未使用,2锁定中,3已使用,4已过期**/
	private String couponsStatusName;

	/**
	 * 使用时间
	 */
	private String useTime;	

	/**
	 * 使用的单号
	 */
	private String useOrder;	

	/**
	 * 订单原金额
	 */
	private String useOrderMoney;	

	/**
	 * 订单优惠金额
	 */
	private String useSpendMoney;	

	

	/**
	 * 优惠券规则说明
	 */
	private String couponsTypeDesc;
	
	/**
	 * 优惠类型{满减，立减,折扣}
	 */
	private String couponsType;	
	private String couponsTypeName;

	/**
	 * 最低消费金额,满足后才可使用
	 */
	private String minSpendMoney;	


	/**
	 * 满减_优惠减少金额
	 */
	private String amtFullReduce;	

	/**
	 * 折扣
	 */
	private String amtDiscount;	

	/**
	 * 立减金额
	 */
	private String amtSub;	
	
	/**
	 * 优惠券说明
	 */
	private String couponDes;
	

	/**优惠券去使用跳转连接。为空默认跳转首页**/
	private String couponsHrefUrl;

	/**优惠券内容图片 为空使用默认图片**/
	private String contentImg;

	/**优惠券内容禁用图片 为空使用默认图片**/
	private String contentdisabledImg;

	/**
	 * 赠送人seq
	 */
	private String giveSeq;

	/**
	 * 来源
	 */
	private String receiveSrc;

	/**
	 * 抵扣券余额
	 */
	private BigDecimal balance;

	/**
	 * 已抵扣余额
	 */
	private BigDecimal usedBalance;

	/**
	 * 代金券金额
	 */
	private BigDecimal amount;

	//赠送人名
	private String giveCouponsName;



	public static void main(String[] args) {
		String str = "{\"code\":\"200\",\"msg\":\"操作成功\",\"responseTime\":\"2018-05-07 23:03:28\",\"result\":\"{\\\"1002\\\":{\\\"pages\\\":{\\\"limit\\\":10,\\\"offset\\\":0,\\\"page\\\":1,\\\"pageAllNums\\\":1,\\\"totalNums\\\":3},\\\"datas\\\":[{\\\"activityBatchNo\\\":\\\"520GiftPackeg\\\",\\\"activityId\\\":\\\"1\\\",\\\"activityName\\\":\\\"520专场活动礼包\\\",\\\"amtFullOver\\\":\\\"999.00\\\",\\\"amtFullReduce\\\":\\\"99.00\\\",\\\"couponsId\\\":\\\"60\\\",\\\"couponsName\\\":\\\"520专场满520减52\\\",\\\"couponsStatus\\\":\\\"1\\\",\\\"couponsType\\\":\\\"1\\\",\\\"couponsTypeDesc\\\":\\\"满999.0减99.0\\\",\\\"couponsTypeModel\\\":\\\"1\\\",\\\"couponsUserId\\\":\\\"1\\\",\\\"createAdmin\\\":\\\"520GiftPackeg\\\",\\\"createTime\\\":\\\"2018-05-07 21:28:17.0\\\",\\\"minSpendMoney\\\":\\\"1099.00\\\",\\\"receiveSrc\\\":\\\"520GiftPackeg\\\",\\\"userSeq\\\":\\\"用户标识1001\\\",\\\"valiDayEnd\\\":\\\"2018-05-16 23:59:59.0\\\",\\\"valiDayStart\\\":\\\"2018-05-08 00:00:00.0\\\"},{\\\"activityBatchNo\\\":\\\"520GiftPackeg\\\",\\\"activityId\\\":\\\"1\\\",\\\"activityName\\\":\\\"520专场活动礼包\\\",\\\"amtFullOver\\\":\\\"999.00\\\",\\\"amtFullReduce\\\":\\\"99.00\\\",\\\"couponsId\\\":\\\"61\\\",\\\"couponsName\\\":\\\"华项专场满520减52\\\",\\\"couponsStatus\\\":\\\"1\\\",\\\"couponsType\\\":\\\"1\\\",\\\"couponsTypeDesc\\\":\\\"满999.0减99.0\\\",\\\"couponsTypeModel\\\":\\\"1\\\",\\\"couponsUserId\\\":\\\"2\\\",\\\"createAdmin\\\":\\\"520GiftPackeg\\\",\\\"createTime\\\":\\\"2018-05-07 21:28:17.0\\\",\\\"minSpendMoney\\\":\\\"1099.00\\\",\\\"receiveSrc\\\":\\\"520GiftPackeg\\\",\\\"userSeq\\\":\\\"用户标识1001\\\",\\\"valiDayEnd\\\":\\\"2018-05-21 00:00:00.0\\\",\\\"valiDayStart\\\":\\\"2018-05-03 14:09:46.0\\\"},{\\\"activityBatchNo\\\":\\\"520GiftPackeg\\\",\\\"activityId\\\":\\\"1\\\",\\\"activityName\\\":\\\"520专场活动礼包\\\",\\\"amtFullOver\\\":\\\"999.00\\\",\\\"amtFullReduce\\\":\\\"99.00\\\",\\\"couponsId\\\":\\\"62\\\",\\\"couponsName\\\":\\\"小熊猫优惠券满100减10\\\",\\\"couponsStatus\\\":\\\"1\\\",\\\"couponsType\\\":\\\"1\\\",\\\"couponsTypeDesc\\\":\\\"满999.0减99.0\\\",\\\"couponsTypeModel\\\":\\\"1\\\",\\\"couponsUserId\\\":\\\"3\\\",\\\"createAdmin\\\":\\\"520GiftPackeg\\\",\\\"createTime\\\":\\\"2018-05-07 21:28:17.0\\\",\\\"minSpendMoney\\\":\\\"1099.00\\\",\\\"receiveSrc\\\":\\\"520GiftPackeg\\\",\\\"userSeq\\\":\\\"用户标识1001\\\",\\\"valiDayEnd\\\":\\\"2018-05-16 23:59:59.0\\\",\\\"valiDayStart\\\":\\\"2018-05-08 00:00:00.0\\\"}]}}\"}";
		BaseResponseResultDto dto = JsonUtils.parse(str, BaseResponseResultDto.class);
		Map map = JsonUtils.parse(dto.getResult().toString(), HashMap.class);
		System.out.println("map==="+JsonUtils.toDefaultJson(map));
		
		Map codeMap = JsonUtils.parse(map.get("1002").toString(), HashMap.class);
		PageDto pageDto = JsonUtils.parse(codeMap.get("pages").toString(), PageDto.class);
		System.out.println("pageDto==="+JsonUtils.toDefaultJson(pageDto));
		List<ResUserCouponsPackDataDto> datas = JsonUtils.parse(codeMap.get("datas").toString(), ArrayList.class);
		System.out.println("datas==="+JsonUtils.toDefaultJson(datas));
	}
}