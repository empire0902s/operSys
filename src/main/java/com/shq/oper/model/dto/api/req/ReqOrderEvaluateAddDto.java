package com.shq.oper.model.dto.api.req;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;

import io.swagger.annotations.ApiModel;



@ApiModel(value="请求添加评价订单DTO")
@lombok.Data
public class ReqOrderEvaluateAddDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private String id;
	
	/**
	 *订单编号
	 */
	private String orderNo;
	
	/**
	 * 商品货物code
	 */
	private String goodsCode;

	
	/**
	 * 评价时间
	 */
	private LocalDateTime evaluateTime;

	/**
	 * 评价用户seq
	 */
	private String userSeq;

	/**
	 * 评价等级1-5
	 */
	private Short evaluateLev;

	/**
	 * 是否匿名 0:实名,1匿名
	 */
	private Short isAnonymous;

	/**
	 * 是否置顶 0不置顶,1置顶
	 */
	private Short isTop;

	/**
	 * 追加评论Id(末尾为空)
	 */
	private Long appendEvaluateId;
	
	/**
	 * sku
	 */
	private String sku;
	
	/**
	 * 评价内容
	 */
	private String evaluateContent;
	
	/**
	 * 评论来源  0 用户评论  1 后台评论
	 */
	private String evaluatieType;
	
	

	List<String> imgsPathList;

}