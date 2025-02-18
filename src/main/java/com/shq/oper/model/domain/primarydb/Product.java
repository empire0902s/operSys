package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;


/**
 * 订货商品信息
 */
@lombok.Data
@Table(name = "t_goods_product")
//	//DistributionProduct
public class Product  implements Serializable{
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


    @Column(name = "p_id")
    private String p_id;	//商品ID		//133,

    @Column(name = "product_code")
    private String product_Code;	//商品货号	//"3728815201803280936193619131477",

    @Column(name = "product_name")
    private String product_Name;	//商品名称	//"男士公文包",

    @Column(name = "category_id")
    private String categoryId;	// 一级分类	//2100,

    @Column(name = "genre_id")
    private String genreId;	// 二级分类	//1169,

    @Column(name = "three_id")
    private String threeId;	// 三级分类	//1489,

    @Column(name = "four_id")
    private String fourId;	// 四级分类	//0,

    @Column(name = "sort")
    private String sort;	 //排序	//1,

    @Column(name = "financial_audit")
    private String financialAudit;	//财务审核	//0,

    @Column(name = "financial_auditor")
    private String financialAuditor;	// 财务审核人	//null,

    @Column(name = "financial_auditTime")
    private String financialAuditTime;	// 财务审核时间	//"0001-01-01T00:00:00",

    @Column(name = "financial_audit_remark")
    private String financialAuditRemark;	// 财务审核备注	//null,

    @Column(name = "market_audit")
    private String marketAudit;	//市场审核	//0,

    @Column(name = "market_auditor")
    private String marketAuditor;	// 市场审核人	//null,

    @Column(name = "marketA_audit_time")
    private String marketAuditTime;	// 市场审核时间	//"0001-01-01T00:00:00",

    @Column(name = "market_audit_remark")
    private String marketAuditRemark;	// 市场审核备注	//null,

    @Column(name = "activity_state")
    private String activityState;	//活动状态	//0


    @Column(name = "is_sale")
    private String is_sale;		// 上下架状态  0待审核 1上架   2下架	//0,

    @Column(name = "is_sale_date")
    private String is_sale_date;	// 上下架时间	//"0001-01-01T00:00:00",

    //    @Transient
    @Column(name = "company_name")
    private String company_name;	//供货商名称	//"测试周先生工厂",

    //    @Transient
    @Column(name = "company")
    private String company;	// 供货商SEQ	//"3728815",


    @Transient
    //@Column(name = "weight_type")
    private String weightType;	// 商品重量类型	//"轻货",

    @Transient
    //@Column(name = "logisticsi_blling")
    private String logisticsiBlling;	// 物流计费方式	//2,

    @Transient
  //@Column(name = "is_product_region")
    private String isProductRegion;	// 是否区域价	//0,

    @Transient
    //@Column(name = "is_standard")
    private String isstandard;	// 商品特性	//2,

    @Transient
    //@Column(name = "is_commoditynature")
    private String iscommoditynature;	// 商品性质	//1,


    @Transient
    //@Column(name = "logistics_type")
    private String logisticsType;	// 物流类型	//"省内",

    @Transient
    //@Column(name = "logistics_expenses")
    private String logisticsExpenses;	// 物流费用	//0,


    @Transient
    //@Column(name = "product_unit")
    private String product_unit;	// 商品单位	//"个",

    @Transient
    //@Column(name = "created_time")
    private String created_time;	// 创建时间	//"0001-01-01T00:00:00",

    @Transient
    //@Column(name = "updated_time")
    private String updated_time;	// 修改时间	//"0001-01-01T00:00:00",

    @Transient
    //@Column(name = "product_details")
    private String product_details;		// 详细设置模块

    @Transient
    //@Column(name = "exhibit_count")
    private String exhibitCount;	//展示次数	//16,

    @Transient
    //@Column(name = "visitors")
    private String visitors;	//浏览人数	//3,

    @Transient
    //@Column(name = "buy_number")
    private String buyNumber;	//购买件数		//0,




    @Transient
    //@Column(name = "invoice_name")
    private String invoiceName;	// 发票名称	//"公文包",

    @Transient
    //@Column(name = "weight")
    private String weight;	// 商品重量	//"1",

    @Transient
    //@Column(name = "volume")
    private String volume;	// 商品体积	//"0.3",

    @Transient
    //@Column(name = "distance")
    private String distance;	// 保护范围	//0.52,

    @Transient
    //@Column(name = "keywords")
    private String keywords;		// 商品关键字	//"公文包 男士",

    @Transient
    //@Column(name = "crime")
    private String crime;	// 广告语	//"男士办公的选择",

    @Transient
    //@Column(name = "package")
    private String Package;	// 商品包装	//"纸盒",

    @Transient
    //@Column(name = "label")
    private String label;	//佣金设置	//0,



    @Transient
    //@Column(name = "description")
    private String description;		//null,

    @Transient
    //@Column(name = "self_confessed")
    private String selfConfessed;	// 日供量	//10,




    //------------------------------------------------------------------------//
    //------------------------------自定义属性--------------------------------//
    //------------------------------------------------------------------------/

    @Transient
    private String label_NameStr;	//佣金设置 状态
    public String getLabel_NameStr() {
    	String str = null;
    	if(!StringUtils.isEmpty(label)) {
    		switch (label) {
			case "0":
				str = "未设置";
				break;
			case "1":
				str = "已设置";
				break;
			default:
				break;
			}
    	}
    	return str;
    }

    @Transient
    private String financialAudit_NameStr;	//财务审核 状态
    public String getFinancialAudit_NameStr() {
    	String str = null;
    	if(!StringUtils.isEmpty(financialAudit)) {
    		switch (financialAudit) {
    		case "0":
				str = "待审核";
				break;
			case "1":
				str = "审核通过";
				break;
			case "2":
				str = "不通过";
				break;
			case "3":
				str = "审核异常";
				break;
			default:
				break;
			}
    	}
    	return str;
    }

    @Transient
    private String marketAudit_NameStr;	//市场审核 状态
    public String getMarketAudit_NameStr() {
    	String str = null;
    	if(!StringUtils.isEmpty(marketAudit)) {
    		switch (marketAudit) {
			case "0":
				str = "待审核";
				break;
			case "1":
				str = "审核通过";
				break;
			case "2":
				str = "不通过";
				break;
			case "3":
				str = "审核异常";
				break;
			case "4":
				str = "下架";
				break;
			default:
				break;
			}
    	}
    	return str;
    }

    @Transient
    private String activityState_NameStr;	//活动 状态
    public String getActivityState_NameStr() {
    	String str = null;
    	if(!StringUtils.isEmpty(activityState)) {
    		switch (activityState) {
			case "0":
				str = "未设置";
				break;
			case "1":
				str = "已设置";
				break;
			case "2":
				str = "已上架";
				break;
			case "3":
				str = "已下架";
				break;
			default:
				break;
			}
    	}
    	return str;
    }
    
}
