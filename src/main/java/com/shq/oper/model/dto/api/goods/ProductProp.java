package com.shq.oper.model.dto.api.goods;

import java.io.Serializable;


/**
 * 订货商品信息
 */
@lombok.Data
public class ProductProp  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	   // DistributionProduct_Para
	private String Id;	//388,
	private String ClassId;	//1202,// 关联-genreId
	private String Name;	//"颜色",// 详细
	private String Status;	//1,// 状态启用为1
	private String Sort;	//1,// 排序
	private String CreateTime;	//"2018-03-28T10:34:07.767",// 创建时间
	private String CreateUser;	//"",// 创建人
	private String EditTime;	//null,// 修改时间
	private String EditUser;	//null,// 修改人
	private String ProductPropertyId;	//1846,
	private String Product_Code;	//"3728815201803280936193619131477"
	
}
