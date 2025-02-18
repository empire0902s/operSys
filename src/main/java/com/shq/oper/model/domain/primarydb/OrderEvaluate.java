package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "t_order_evaluate")
public class OrderEvaluate implements Serializable {
	@Id
	@GeneratedValue(generator = "JDBC") 
	private Long id;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;

	/**
	 * 最近更新人
	 */
	@Column(name = "update_admin")
	private String updateAdmin;

	/**
	 * 订单编号
	 */
	@Column(name = "order_no")
	private String orderNo;

	/**
	 * 商品货号Code
	 */
	@Column(name = "goods_code")
	private String goodsCode;

	/**
	 * 评价时间
	 */
	@Column(name = "evaluate_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime evaluateTime;

	/**
	 * 评价用户seq
	 */
	@Column(name = "user_seq")
	private String userSeq;

	/**
	 * 评价等级1-5
	 */
	@Column(name = "evaluate_lev")
	private Short evaluateLev;

	/**
	 * 是否匿名0:实名,1匿名
	 */
	@Column(name = "is_anonymous")
	private Short isAnonymous;

	/**
	 * 是否置顶0不置顶,1置顶
	 */
	@Column(name = "is_top")
	private Short isTop;

	/**
	 * 追加评论Id(末尾为空)
	 */
	@Column(name = "append_evaluate_id")
	private Long appendEvaluateId;

	/**
	 * 审核状态)待审核,1审核通过
	 */
	@Column(name = "audit_stats")
	private Short auditStats;

	/**
	 * 审核人
	 */
	@Column(name = "audit_admin")
	private String auditAdmin;

	/**
	 * 审核时间
	 */
	@Column(name = "audit_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime auditTime;

	/**
	 * 后台人员回复
	 */
	@Column(name = "audit_reply")
	private String auditReply;

	/**
	 * 审核备注
	 */
	@Column(name = "audit_desc")
	private String auditDesc;

	/**
	 * 评价内容
	 */
	@Column(name = "evaluate_content")
	private String evaluateContent;
	/**
	 * sku
	 */
	@Column(name="goods_sku")
	private String goodsSku;
	
	/**
	 * userIcon 用户头像
	 */
	@Column(name="user_icon")
	private String userIcon;
	
	/**
	 * nickName 昵称
	 */
	@Column(name="nick_name")
	private String nickName;
	
	/**
	 * evaluatieType 评价类型  0 订单评价  1 手动评价
	 */
	@Column(name="evaluatie_type")
	private String evaluatieType;
	
	@Transient
	private String selMobile;
	
	/*
	 * 评论图片
	 */
	@Transient
	private String evaluatieImg;
	
	@Transient
	private List<String>evaluatieImgs=new ArrayList<String>();
	
	
	@Transient
	private List<OrderEvaluateImgs>orderEvaluateImgs;
	

	private static final long serialVersionUID = 1L;
}