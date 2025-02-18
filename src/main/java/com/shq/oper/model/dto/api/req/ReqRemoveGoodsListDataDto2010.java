package com.shq.oper.model.dto.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;


@ApiModel(value="请求删除优惠券品类规则DTO")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqRemoveGoodsListDataDto2010 implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "优惠券id")
	private Long couponsId;	//正常

	@ApiModelProperty(value = "来源系统")
	private String fromSys;

	@ApiModelProperty(value = "来源系统code")
	private String fromSysCode;

	@ApiModelProperty(value = "商品codes")
	private List<String> codes;
	
}