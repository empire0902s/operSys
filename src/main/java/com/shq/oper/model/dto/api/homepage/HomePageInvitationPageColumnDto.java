package com.shq.oper.model.dto.api.homepage;

import java.io.Serializable;


import io.swagger.annotations.ApiModel;



@ApiModel(value="首页邀请页列表栏目DTO")
@lombok.Data
public class HomePageInvitationPageColumnDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	


	/**
	 * 栏目名称
	 */
  private String name;

}