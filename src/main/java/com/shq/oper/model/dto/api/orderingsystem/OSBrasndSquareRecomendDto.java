package com.shq.oper.model.dto.api.orderingsystem;

import java.io.Serializable;


import io.swagger.annotations.ApiModel;



@ApiModel(value="订货系统品牌广场DTO")
@lombok.Data
public class OSBrasndSquareRecomendDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	

	private String store_name;

	/**
	 * 品牌商seq
	 */
  private String seq;
  
  

}