package com.shq.oper.model.dto.api.goods;

import java.io.Serializable;
import java.util.List;

import com.shq.oper.model.domain.primarydb.Product;
import com.shq.oper.model.domain.primarydb.ProductPrice;



/**
 * 订货商品信息
 */
@lombok.Data
public class Distribution  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Product Product;
	private List<ProductPrice> listPrice;
	private List<ProductProp> listProp;
	private List<ProductStand> listStand;
	private List<ProductPara> listPara;
	private List<ProductImg> listProductImg;
	
}
