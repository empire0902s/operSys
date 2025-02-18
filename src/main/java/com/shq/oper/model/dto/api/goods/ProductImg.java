package com.shq.oper.model.dto.api.goods;

import java.io.Serializable;


/**
 * 订货商品信息
 */
@lombok.Data
public class ProductImg  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String Id;	//349,
	private String Code;	//"3728815201803280936193619131477",
	private String Sku;	//null,
	private String ImgName;	//"2018032809425648672.jpeg",
	private String ImgUrl;	//"/UploadFile/GHS/20180328/",
	private String Status;	//0,
	private String UploadUser;	//null,
	private String UploadDate;	//"0001-01-01T00:00:00"	
	
}
