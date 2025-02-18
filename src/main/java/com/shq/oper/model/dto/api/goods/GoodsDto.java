package com.shq.oper.model.dto.api.goods;

import java.io.Serializable;
import java.util.List;


/**
 * 订货商品信息
 */
@lombok.Data
public class GoodsDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Distribution distribution;	//result 转换的对象
	
}
