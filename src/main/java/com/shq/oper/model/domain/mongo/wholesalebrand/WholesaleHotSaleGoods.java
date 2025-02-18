package com.shq.oper.model.domain.mongo.wholesalebrand;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 品牌广场热销商品
 * @author Administrator
 *
 */
@Data
@Document(collection="t_lesale_hotSaleGoods")
public class WholesaleHotSaleGoods implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
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

	/**
	 * 行业类型
	 */
	private Integer industryType;


}
