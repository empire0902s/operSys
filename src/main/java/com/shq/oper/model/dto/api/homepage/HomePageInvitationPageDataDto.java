package com.shq.oper.model.dto.api.homepage;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;



@ApiModel(value="首页邀请页列表返回数据DTO")
@lombok.Data
public class HomePageInvitationPageDataDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	


	/**
	 * 商品栏目名称
	 */
    private String columnName;
    /**
	 * 商品code
	 */
    private List<HomePageInvitationPageGoodsDto> goodsList;
    

}