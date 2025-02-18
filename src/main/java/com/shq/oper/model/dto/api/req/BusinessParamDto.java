package com.shq.oper.model.dto.api.req;

import java.io.Serializable;
import java.util.List;


@lombok.Data
public class BusinessParamDto implements Serializable{
	private static final long serialVersionUID = 1L;

	//用户seq
	private String seq;

	//优惠券类型（1:代金券，2:抵扣券）
	private String couponType;

	//优惠券金额
	private String amount;

}