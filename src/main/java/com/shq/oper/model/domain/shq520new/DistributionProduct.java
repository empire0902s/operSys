package com.shq.oper.model.domain.shq520new;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@Table(name = "DistributionProduct")
public class DistributionProduct extends com.shq.oper.model.dto.MssqlShqPageDto implements Serializable {
    @Id
    @Column(name = "p_id")
    private Integer pId;
    //商品货号
    @Column(name = "Product_Code")
    private String productCode;

    //商品名称
    @Column(name = "Product_Name")
    private String productName;

    // 发票名称
    @Column(name = "InvoiceName")
    private String invoicename;

    // 一级分类
    @Column(name = "CategoryId")
    private Integer categoryid;

    // 二级分类
    @Column(name = "GenreId")
    private Integer genreid;

    // 三级分类
    @Column(name = "ThreeId")
    private Integer threeid;

    // 四级分类
    @Column(name = "FourId")
    private Integer fourid;

    //排序
    @Column(name = "Sort")
    private Integer sort;

    // 商品重量类型
    @Column(name = "WeightType")
    private String weighttype;

    // 商品重量
    @Column(name = "Weight")
    private String weight;

    // 商品体积
    @Column(name = "Volume")
    private String volume;

    // 保护范围
    @Column(name = "Distance")
    private Double distance;

    // 商品关键字
    @Column(name = "Keywords")
    private String keywords;

    // 广告语
    @Column(name = "Crime")
    private String crime;

    @Column(name = "Package")
    private String packageStr;

    // 物流计费方式
    @Column(name = "LogisticsiBlling")
    private Integer logisticsiblling;

    // 是否区域价
    @Column(name = "IsProductRegion")
    private Integer isproductregion;

    // 商品特性
    @Column(name = "Isstandard")
    private Integer isstandard;

    // 商品性质
    @Column(name = "Iscommoditynature")
    private Integer iscommoditynature;

    // 供货商SEQ
    private String company;

    //供货商名称
    @Column(name = "company_name")
    private String companyName;

    // 物流类型
    @Column(name = "LogisticsType")
    private String logisticstype;

    // 物流费用
    @Column(name = "LogisticsExpenses")
    private BigDecimal logisticsexpenses;

    @Column(name = "product_unit")
    private String productUnit;

    @Column(name = "product_details")
    private String productDetails;

    //佣金设置
    @Column(name = "Label")
    private Integer label;

    @Column(name = "ExhibitCount")
    private Integer exhibitcount;

    @Column(name = "Visitors")
    private Integer visitors;

    @Column(name = "BuyNumber")
    private Integer buynumber;

    @Column(name = "IsEditor")
    private Integer iseditor;

    @Column(name = "IsEditorAuditor")
    private String iseditorauditor;

    @Column(name = "IsEditorAuditorTime")
    private Date iseditorauditortime;

    @Column(name = "IsOriginal")
    private Integer isoriginal;

    @Column(name = "IsOriginalAuditor")
    private String isoriginalauditor;

    @Column(name = "IsOriginalAuditorTime")
    private Date isoriginalauditortime;

    @Column(name = "FinancialAudit")
    private Integer financialaudit;

    @Column(name = "FinancialAuditor")
    private String financialauditor;

    @Column(name = "FinancialAuditTime")
    private Date financialaudittime;

    @Column(name = "FinancialAuditRemark")
    private String financialauditremark;

    @Column(name = "MarketAudit")
    private Integer marketaudit;

    @Column(name = "MarketAuditor")
    private String marketauditor;

    @Column(name = "MarketAuditTime")
    private Date marketaudittime;

    @Column(name = "MarketAuditRemark")
    private String marketauditremark;

    //0:待审核，1上架，2下架
    @Column(name = "is_sale")
    private Integer isSale;

    // 上下架时间
    @Column(name = "is_sale_date")
    private Date isSaleDate;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    // 日供量
    @Column(name = "SelfConfessed")
    private Integer selfconfessed;

    //活动 状态
    @Column(name = "ActivityState")
    private Integer activitystate;

    @Column(name = "FinanceState")
    private Integer financestate;

    @Column(name = "ActivityEstablishTime")
    private Date activityestablishtime;

    @Column(name = "ActivityEstablishUser")
    private String activityestablishuser;

    @Column(name = "ActivityFinanceTime")
    private Date activityfinancetime;

    @Column(name = "ActivityFinanceUser")
    private String activityfinanceuser;

    @Column(name = "ActivitySXJUser")
    private String activitysxjuser;

    @Column(name = "ActivitySXJTime")
    private Date activitysxjtime;

    @Column(name = "ActivityCode")
    private String activitycode;

    @Column(name = "YsType")
    private Integer ystype;

    @Column(name = "YsDay")
    private String ysday;

    @Column(name = "YsStartTime")
    private Date ysstarttime;

    @Column(name = "YsEndTime")
    private Date ysendtime;

    @Column(name = "CommodityStartTime")
    private Date commoditystarttime;

    @Column(name = "CommodityFinishTime")
    private Date commodityfinishtime;

    @Column(name = "Submit")
    private Integer submit;

    @Column(name = "addCartCount")
    private Integer addcartcount;

    /*
     * 分销价格
     */
    @Transient
    private String distributionPrice;
    
    /*
     * 零售价格
     */
    @Transient
    private String goodsPrice;
    
    private static final long serialVersionUID = 1L;
}