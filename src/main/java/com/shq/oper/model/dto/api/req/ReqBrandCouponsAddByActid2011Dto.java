package com.shq.oper.model.dto.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;


@ApiModel(value="请求通过活动添加品牌广场优惠券DTO")
@lombok.Data
public class ReqBrandCouponsAddByActid2011Dto implements Serializable{
	private static final long serialVersionUID = 1L;
	

    /**
     * 品牌店铺seq
     */
    private String brandShopSeq;

	@ApiModelProperty(value = "活动Id")
    private String activityId;

    /**
     * 来源系统
     */
    private String fromSys;
    /**
     * 来源系统Code
     */
    private String fromSysCode;


	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间
	
	
	List<ReqBrandCouponsDto> brandCouponsList;
	
//	public static void main(String[] args) {
//		ReqBrandActivityAddDto dto = new ReqBrandActivityAddDto();
//		List<ReqBrandCouponsDto> brandCouponsList = new ArrayList<ReqBrandCouponsDto>();
//		brandCouponsList.add(new ReqBrandCouponsDto());
//		dto.setBrandCouponsList(brandCouponsList);
//		System.out.println(JsonUtils.toDefaultJsonEmptyWrite(dto));
//
//	}
}