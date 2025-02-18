package com.shq.oper.model.dto.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;


@ApiModel(value="请求添加品牌广场优惠券批量激活DTO")
@lombok.Data
public class ReqBrandCouponsBatchEnbleDto implements Serializable{
	private static final long serialVersionUID = 1L;

    /**
     * 优惠券ids
     */
//    private List<Long> ids;
     private String ids;

    //是否禁用
    private boolean isDisabled;

	@ApiModelProperty(value = "商品code")
    private String code;

    /**
     * 品牌店铺seq
     */
    private String brandShopSeq;

    /**
     * 来源系统
     */
    private String fromSys;
    /**
     * 来源系统Code
     */
    private String fromSysCode;

}