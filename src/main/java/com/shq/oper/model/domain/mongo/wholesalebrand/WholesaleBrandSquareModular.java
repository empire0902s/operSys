package com.shq.oper.model.domain.mongo.wholesalebrand;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 品牌广场模块表
 *@author zhuwanchun
 * @date 2018/10/12
 */
@Data
@Document(collection="t_wholesale_brandSquareModular")
public class WholesaleBrandSquareModular implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	/**
	 * 栏目类型(1.品牌广场主页品牌推荐模块 2.品牌广场热卖商品模块 3.品牌商店模块1 4.品牌商品模块2 5.品牌商品模块3)
	 */
	private Integer columnType;

	/**
	 * 模块所属店铺seq
	 */
	private String associatedShopSeq;

	/**
	 * 模块名称
	 */
	private String modularName;

	/**
	 * 模块状态(1.禁用  2.启用)
	 */
	private Integer modularStatus;

	/**
	 * 模块排序
	 */
	private Integer sortNum;

//	/**
//	 * 行业id
//	 */
//	private String industryId;
//
//	private String industryName;

	private String createTime;

	private String admin;


}

