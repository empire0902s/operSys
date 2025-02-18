package com.shq.oper.model.dto.api.res;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shq.oper.util.JsonUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



@ApiModel(value="返回品牌广场活动列表DTO")
@lombok.Data
public class ResBrandActivityListDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	

    /**
     * 品牌店铺seq
     */
    private String brandShopSeq;
    
    
	@ApiModelProperty(value = "活动Id")
    private String id;

    /**
     * 规则名称
     */
    private String name;
    

    /**
     * 批次
     */
    private String batch;

    
    /**
     * 活动类型(1:优惠券活动,2:商品活动)
     */
    private String useType;


    /**
     * 发放开始时间
     */
    private String sendTimeStart;

    /**
     * 发放结束时间
     */
    private String sendTimeEnd;


    /**
     * 活动宣传标题
     */
    private String title;

    /**
     * 是否禁用（0为否，1为是）
     */
    private String isDisabled;

    /**
     * 活动说明
     */
    private String activityDesc;
 

	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间
	
	
	List<ResBrandCouponsListDto> brandCouponsList;
	
	public static void main(String[] args) {
		ResBrandActivityListDto dto = new ResBrandActivityListDto();
		List<ResBrandCouponsListDto> brandCouponsList = new ArrayList<ResBrandCouponsListDto>();
		brandCouponsList.add(new ResBrandCouponsListDto());
		dto.setBrandCouponsList(brandCouponsList);
		System.out.println(JsonUtils.toDefaultJsonEmptyWrite(dto));
		
	}
}