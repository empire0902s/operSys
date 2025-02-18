package com.shq.oper.model.domain.mongo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * 爱之家---栏目实体类
 * @author Administrator
 */
@lombok.Data
@Document(collection="SelectionGoodsClass")
public class SelectionGoodsClass  implements Serializable {
	private static final long serialVersionUID = 1L;
	/****/
	
	@Id
	private String id;
	
	/**
	 * 商品名字
	 */
	private String productName;

	/**
	 * 商品Code
	 */
	private String productCode;
	
	/**
	 * 时间
	 */
	private Date createDate;
	

	
	private Integer isRelease;
	
	/**
	 * 栏目Id
	 */
	private Integer appGroup;

	
	/**
	 * 排序
	 */
	private Integer sort;
	
	/**
	 * 判断删除状态（0为正常，1为删除）
	 */
	private Integer state = 0;

	private String strIsRelease;
	
	
}
