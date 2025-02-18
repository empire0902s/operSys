package com.shq.oper.model.dto.api.brandsquare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;



@ApiModel(value="品牌广场热卖商品DTO")
@lombok.Data
public class HotSaleGoodsDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	

	private String id;

	/**
	 * 商品goodsCode
	 */
	private String goodsCode;
	
	/**
	 * 所属模块id
	 */
	private String columnId;

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
  
  /*
   * 商品名称
   */
  private String goodsName;
  
  /*
   * 分销价格
   */
  private String distributionPrice="0";
  
  /*
   * 商品图片
   */
  private String thumbnail;
  
  /*
   * 单位（件）
   */
  private String unit;
  
 
  private String salesVolume;
  
  
  private String companyName="shq520";
  
  /*
	 * 零售价格
	 */
  private String goodsPrice="0";
  
  private List<GoodsProStandard> goodsProStandard=new ArrayList<>();

}