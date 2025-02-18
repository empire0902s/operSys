package com.shq.oper.model.dto.api.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "判断用户是否有领取优惠券记录接口Dto")
@lombok.Data
public class CouponsCheckGetOrNotParamDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户seq
	 */
	private String userSeq;


}