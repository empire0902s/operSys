package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;


/**
 * 订货商品价格信息
 */
@lombok.Data
@Table(name = "t_goods_product_price")
public class ProductPrice  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "oper_sys_id")
    @GeneratedValue(generator = "JDBC")
    private Long operSysId;
  
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 最近更新人(id-name)
     */
    @Column(name = "update_admin")
    private String updateAdmin;
    
	 // DistributionProduct_Price
    
    @Column(name = "p_id")
    private String p_id;	//67,//价格id
    
    @Column(name = "product_code")
    private String Product_Code;	//"3728815201803280936193619131477",// Code
    
    @Column(name = "product_sku")
    private String Product_Sku;	//"20180328094350435011467",// Sku
    
    /**
     * 平台供货价
     */
    @Column(name = "platform_price")
    private String platformPrice;

    /**
     * 零售价
     */
    @Column(name = "retail_price")
    private String retailPrice;

    /**
     * 平台供货单价
     */
    @Column(name = "distribution_price")
    private String distributionPrice;

    /**
     * 分销价
     */
    @Column(name = "price_distribution")
    private String priceDistribution;
    
    @Column(name = "activity_start_time")
    private String ActivityStartTime;	//null,//活动开始时间
    
    @Column(name = "activity_finish_time")
    private String ActivityFinishTime;	//null,// 活动结束时候
    
    @Column(name = "seckill_price")
    private String SeckillPrice;	//0,//秒杀价格
    
    @Column(name = "seckill_commission")
    private String seckillcommission;	//0,// 活动分销商佣金
    
    @Column(name = "seckill_dl_mmission")
    private String seckillDlmmission;	//0,// 活动代理商佣金
    
    @Column(name = "activity_quantity")
    private String ActivityQuantity;	//0// 活动数量

    //活动数据同步状态
    @Column(name = "data_sync_state")
    private Integer dataSyncState;
    
    //上下架状态
    @Column(name = "up_down_state")
    private Integer upDownState;
    
    
    

    @Transient
    //@Column(name = "factory_price")
    private String FactoryPrice;	//80,// 出厂价
    
    @Transient
    //@Column(name = "platform_price")
    private String PlatformPrice;	//110,// 平台供货价
    
    @Transient
    //@Column(name = "retail_price")
    private String RetailPrice;	//199,// 零售价
    
    @Transient
    //@Column(name = "produce_count")
    private String ProduceCount;	//10,// 日生产量
    
    @Transient
    //@Column(name = "produce_moq")
    private String ProduceMoq;	//10,// 生产起订量
    
    @Transient
    //@Column(name = "moq")
    private String Moq;	//10,// 商家起订量
    
    @Transient
    //@Column(name = "cost_unit_price")
    private String Cost_unit_price;	//80,// 出厂单价
    
    @Transient
    //@Column(name = "distribution_price")
    private String Distribution_Price;	//110,// 平台供货单价
    
    @Transient
    //@Column(name = "price_distribution")
    private String Price_Distribution;	//120,// 分销价
    
    @Transient
    //@Column(name = "min_sales_num")
    private String min_sales_num;	//5,// 起卖量

    @Transient
    //@Column(name = "original_price")
    private String Original_price;	//50,// 原始出厂单价
    
    @Transient
    //@Column(name = "commission")
    private String commission;	//0,// 分销商佣金

    @Transient
    //@Column(name = "dlmmission")
    private String Dlmmission;	//0,// 代理商佣金
    
    
    @Transient
    //@Column(name = "standard_name1")
    private String StandardName1;	//"红",
    
    @Transient
    //@Column(name = "standard_name2")
    private String StandardName2;	//"1",
    
    @Transient
    //@Column(name = "standard_name3")
    private String StandardName3;	//"",
    
    @Transient
    //@Column(name = "standard_name4")
    private String StandardName4;	//"",
    
    @Transient
    //@Column(name = "standard_name5")
    private String StandardName5;	//"",
    
    @Transient
    //@Column(name = "standard_name6")
    private String StandardName6;	//"",
    
    @Transient
    //@Column(name = "standard_name7")
    private String StandardName7;	//"",
    
    @Transient
    //@Column(name = "standard_name8")
    private String StandardName8;	//"",
    
    
    @Transient
    //@Column(name = "standard_name1_id")
    private String StandardName1Id;	//"c_1_11411",
    
    @Transient
    //@Column(name = "standard_name2_id")
    private String StandardName2Id;	//"c_2_11411",
    
    @Transient
    //@Column(name = "standard_name3_id")
    private String StandardName3Id;	//null,
    
    @Transient
    //@Column(name = "standard_name4_id")
    private String StandardName4Id;	//null,
    
    @Transient
    //@Column(name = "standard_name5_id")
    private String StandardName5Id;	//null,
    
    @Transient
    //@Column(name = "standard_name6_id")
    private String StandardName6Id;	//null,
    
    @Transient
    //@Column(name = "standard_name7_id")
    private String StandardName7Id;	//null,
    
    @Transient
    //@Column(name = "standard_name8_id")
    private String StandardName8Id;	//null,
}
