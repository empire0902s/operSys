package com.shq.oper.model.domain.mongo.wholesalebrand;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 首页顶部图片轮询
 * @author Administrator
 *
 */
@Data
@Document(collection="t_wholesale_banner")
public class WholesaleBrandSquareBanner implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	/**
	 * 图片位置
	 */
	private String imageLocation;
	
	/**
	 * 点击后跳转链接
	 */
	private String jumpTarget;
	
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 内容
	 */
	private String content;

	/**
	 * 排序
	 */
	private Integer sortNum;
	
	/**
	 * 识别标识
	 * 	1首页顶部轮播图（可储存多张）
	 *	2首页酒水广告横幅图（有且只有一张）
	 *	3首页食品广告横幅图（有且只有一张）
	 *	4分类页顶部广告横幅图（有且只一张）
	 *  7品牌广场首页banner图
	 * 
	 */
	private Integer identifyType;

	/**
	 * 行业id
	 */
	private String industryId;

	private String industryName;

	private String createTime;

	private String admin;
	

}
