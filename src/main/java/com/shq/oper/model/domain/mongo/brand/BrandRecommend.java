package com.shq.oper.model.domain.mongo.brand;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 品牌广场首页品牌推荐
 * 
 * @author Administrator
 *
 */
@Document(collection = "brandRecommend")
public class BrandRecommend {

	private static final long serialVersionUID = 1L;

	private String id;

	/**
	 * 品牌logo图片位置
	 */
	private String brandLogoImg;

	/**
	 * 点击后跳转链接
	 */
	private String jumpTarget;

	/**
	 * 品牌名称
	 */
	private String brandName;

	/**
	 * 排序
	 */
	private Integer sortNum;
	
	
	private String modularName;

	/**
	 * 所属模块id
	 */
	private String columnId;
	
	/**
	 * 是否禁用(1.启用   0.禁用)
	 * @return
	 */
	private Integer brandRecommendStatus;
	
	
	public Integer getBrandRecommendStatus() {
		return brandRecommendStatus;
	}

	public void setBrandRecommendStatus(Integer brandRecommendStatus) {
		this.brandRecommendStatus = brandRecommendStatus;
	}

	public String getModularName() {
		return modularName;
	}

	public void setModularName(String modularName) {
		this.modularName = modularName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBrandLogoImg() {
		return brandLogoImg;
	}

	public void setBrandLogoImg(String brandLogoImg) {
		this.brandLogoImg = brandLogoImg;
	}

	public String getJumpTarget() {
		return jumpTarget;
	}

	public void setJumpTarget(String jumpTarget) {
		this.jumpTarget = jumpTarget;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
}
