package com.shq.oper.model.domain.mongo.brand;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 品牌广场热销商品
 * @author Administrator
 *
 */
@Document(collection="hotSaleGoods")
public class HotSaleGoods {
	
	private static final long serialVersionUID = 1L;
	
	private String id;

	/**
	 * 商品goodsCode
	 */
	private String goodsCode;

	/**
	 * 商品上下架状态(1为下架，2为上架)
	 */
	private Integer activateStatus;

	/**
	 * 上传日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date uploadTime;

	/**
	 * 排序编号
	 */
	private Integer sortNum;

	/**
	 * 关注数（redis每满1000更新一次）
	 */
	private Integer fabulousNum;

	/**
	 * 所属模块id
	 */
	private String columnId;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getGoodsCode(){ return goodsCode; }
	public void setGoodsCode(String goodsCode){ this.goodsCode = goodsCode; }

	public Integer getActivateStatus(){ return activateStatus; }
	public void setActivateStatus(Integer activateStatus){ this.activateStatus = activateStatus; }

	public Integer getSortNum(){ return sortNum; }
	public void setSortNum(Integer sortNum){ this.sortNum = sortNum; }

	public Integer getFabulousNum(){ return fabulousNum; }
	public void setFabulousNum(Integer fabulousNum){ this.fabulousNum = fabulousNum; }

	public Date getUploadTime(){ return uploadTime; }
	public void setUploadTime(Date uploadTime){ this.uploadTime = uploadTime; }

	public String getColumnId(){ return columnId; }
	public void setColumnId(String columnId){ this.columnId = columnId; }

}
