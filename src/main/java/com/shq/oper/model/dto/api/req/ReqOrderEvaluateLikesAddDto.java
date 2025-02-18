package com.shq.oper.model.dto.api.req;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;



@ApiModel(value="请求添加评价订单点赞DTO")
@lombok.Data
public class ReqOrderEvaluateLikesAddDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 *评价订单编号
	 */
	private String orderEvaluateId;
	
	/**
	 * 评价用户seq
	 */
	private String userSeq;


}