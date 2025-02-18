package com.shq.oper.model.dto.api.homepage;

import java.io.Serializable;


import io.swagger.annotations.ApiModel;



@ApiModel(value="首页邀请页列表商品DTO")
@lombok.Data
public class HomePageInvitationPageGoodsDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	


	/**
	 * 商品code
	 */
  private String goodsCode;
  
  /*
   * 排序
   */
  private String sort;

}