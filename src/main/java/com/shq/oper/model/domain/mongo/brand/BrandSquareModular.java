package com.shq.oper.model.domain.mongo.brand;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 品牌广场模块表
 *@author zhuwanchun
 * @date 2018/10/12
 */
@Document(collection="brandSquareModular")
public class BrandSquareModular {

	private static final long serialVersionUID = 1L;

	private String id;

	/**
	 * 栏目类型(1.首页推荐品牌 2.特卖会 7.首页banner图 8.热门分类商品 )
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


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Integer getColumnType() {
		return columnType;
	}
	public void setColumnType(Integer columnType) {
		this.columnType = columnType;
	}

	public String getAssociatedShopSeq() {
		return associatedShopSeq;
	}
	public void setAssociatedShopSeq(String associatedShopSeq) {
		this.associatedShopSeq = associatedShopSeq;
	}

	public String getModularName() {
		return modularName;
	}
	public void setModularName(String modularName) {
		this.modularName = modularName;
	}

	public Integer getModularStatus() {
		return modularStatus;
	}
	public void setModularStatus(Integer modularStatus) {
		this.modularStatus = modularStatus;
	}

	public Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

}

