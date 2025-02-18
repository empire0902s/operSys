package com.shq.oper.model.domain.mongo.wholesalebrand;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 品牌广场首页品牌推荐
 * 
 * @author Administrator
 *
 */
@Data
@Document(collection = "t_wholesale_brandRecommend")
public class WholesaleBrandRecommend implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
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

	/**
	 * 行业类型
	 */
	private Integer industryType;
	

}
