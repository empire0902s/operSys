package com.shq.oper.model.dto.api.req;

import com.shq.oper.util.JsonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ApiModel(value="请求添加品牌广场优惠券品类规则DTO")
@lombok.Data
public class ReqBrandCouponsCategoryAddDto implements Serializable{
	private static final long serialVersionUID = 1L;

    /**
     * 优惠券id
     */
    private String id;

	@ApiModelProperty(value = "商品code")
//    private String code;
    private List<String> codes;

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