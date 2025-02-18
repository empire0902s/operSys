package com.shq.oper.model.dto.api.goods;

import java.io.Serializable;


/**
 * 订货商品信息
 */
@lombok.Data
public class ProductPara  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String Id;	//188,
	private String CreateTime;	//"2018-03-28T10:34:07.767",
	private String CreateUser;	//"",
	private String EditTime;	//"0001-01-01T00:00:00",
	private String EditUser;	//null,
	private String Product_Code;	//"3728815201803280936193619131477",
	private String ParameterId;	//14593,
	private String PDName;	//"30cm",
	private String PNace;	//"高"	
	
}
