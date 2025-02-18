package com.shq.oper.model.dto.api.res;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.shq.oper.model.domain.primarydb.OrderEvaluateImgs;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "3003返回订单评价接口DTO", description = "返回订单评价接口DTO")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ResOrderEvaluateListDataDto implements Serializable {

	private static final long serialVersionUID = 1L;

    private String id;
    
    private String userIcon;

	/**
	 * 更新时间
	 */
	private String updateTime;

	/**
	 * 最近更新人
	 */
	private String updateAdmin;

	/**
	 * 订单编号
	 */
	private String orderNo;

	/**
	 * 商品货号Code
	 */
	private String goodsCode;

	/**
	 * 评价时间
	 */
	private String evaluateTime;

	/**
	 * 评价用户seq
	 */
	private String userSeq;

	/**
	 * 评价等级1-5
	 */
	private Short evaluateLev;

	/**
	 * 是否匿名0:实名,1匿名
	 */
	private Short isAnonymous;

	/**
	 * 是否置顶0不置顶,1置顶
	 */
	private Short isTop;

	/**
	 * 追加评论Id(末尾为空)
	 */
	private Long appendEvaluateId;

	/**
	 * 审核状态)待审核,1审核通过
	 */
	private Short auditStats;

	/**
	 * 审核人
	 */
	private String auditAdmin;

	/**
	 * 审核时间
	 */
	private LocalDateTime auditTime;

	/**
	 * 后台人员回复
	 */
	private String auditReply;

	/**
	 * 审核备注
	 */
	private String auditDesc;

	/**
	 * 评价内容
	 */
	private String evaluateContent;
	
	/**
	 * 订单评价点赞数
	 */
	private String likesCount;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 昵称
	 */
	private String  nickName;
	
	/**
	 * 商品sku
	 */
	private String goodsSku;
	
	/**
	 * 评价图片集合
	 */
	private List<OrderEvaluateImgs>imgsList;

	
}