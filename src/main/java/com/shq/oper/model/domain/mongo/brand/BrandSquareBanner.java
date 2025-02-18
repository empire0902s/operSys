package com.shq.oper.model.domain.mongo.brand;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 首页顶部图片轮询
 * @author Administrator
 *
 */
@Document(collection="banner")
public class BrandSquareBanner {
	
	private static final long serialVersionUID = 1L;
	
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
	 * 所属模块id
	 */
	private String columnId;
	
	
	
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getImageLocation() {
		return imageLocation;
	}
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
	
	public String getJumpTarget() {
		return jumpTarget;
	}
	public void setJumpTarget(String jumpTarget) {
		this.jumpTarget = jumpTarget;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Integer getIdentifyType() {
		return identifyType;
	}
	public void setIdentifyType(Integer identifyType) {
		this.identifyType = identifyType;
	}

	public Integer getSortNum(){ return sortNum; }
	public void setSortNum(Integer sortNum){ this.sortNum = sortNum; }
}
