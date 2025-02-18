package com.shq.oper.model.domain.mongo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** 爱之家首页模块表 **/
@lombok.Data
@Document(collection="t_home_page_module")
public class HomePageModule implements Serializable {
	private static final long serialVersionUID = 1L;
	/****/
	@Id
	private String id;
	
	private String createTime;
	
	private String createAdmin;
	
	private String updateTime;
	
	private String updateAdmin;
	
	
	/** 模块名称 **/
	private String moduleName = "";
	
	/**
	 * 显示模块名称(0不显示，1显示)
	 */
	private Integer showModuleName = 0;
	

	/**
	 * 排序值 默认1000
	 */
	private Integer sortNum = 1000;
	
	
	/**
	 * 展示状态(0不显示，1显示)
	 */
	private Integer showState = 0;
	
	
	/**
	 * 模块类型( 0 默认首页模块, 1 品牌广场模块)
	 */
	private Integer moduleType = 0;
	

	/**
	 * 栏目下面是否有空白   默认为 0     app:20px
	 */
	private Integer  followingBlank = 0;

	
	/**
	 * 是否可以加载更多 (0不显示，1显示加载更多)
	 */
	private Integer showloadMore = 0;
	
	/**
	 * 更多 目标链接
	 */
	private String loadMoreTarget = "";
	
	
	/**
	 * 活动类型 {0,普通商品,1秒杀活动,2砍价活动,3预售活动,4品牌广场}
	 */
	private Integer actityType = 0;
	
	
	/**
	 * 样式1 (图片轮播区域)   数据内容为banner
	 * 样式2(优惠券) 数据内容为banner
	 * 样式3(广告图)  用于区分 优惠券  样式其实和优惠券一样   数据内容为banner
	 * 样式4 滑动栏  数据内容为商品distribution
	 * 样式5  数据内容为banner
	 * 样式6 数据内容为banner
	 * 样式7 (品牌广场) 数据内容为banner  + 商品
	 * 样式8   秒杀(只有banner) 商品需要另外的接口返回
	 * 样式9   商品列表  (一般会有10个)
	 * 
	 * 模块样式类型{1轮播Banner,2优惠券Banner,3广告图,4商品滑动栏,5带logo图Banner,6,Banner图片,7品牌广场,8秒杀,9普通商品列表}
	 */
	private Integer styleType = 0;
	
	
	
	/**
	 * 模块附属表
	 */
	private HomePageModuleAttached moduleAttached = new HomePageModuleAttached();
	
	/**
	 * 模块Banner 列表
	 */
	private List<HomePageBanner> listBanner = new ArrayList<>();

	/**
	 * 模块 商品goods 列表
	 */
	private List<HomePageGoods> listGoods  = new ArrayList<>();
	
	/**
	 * 模块 组合Banner和Goods 列表
	 */
	private List<HomePageGroupItem> listGroupItem = new ArrayList<>();
	
	
	
}
