package com.shq.oper.model.domain.shq520new;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper=true)
@Table(name = "Distribution_Orders")
public class DistributionOrders extends com.shq.oper.model.dto.MssqlShqPageDto  implements Serializable {
    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "orderId")
    private String orderid;

    private String orderno;

    @Column(name = "invoiceHead")
    private String invoicehead;

    @Column(name = "userId")
    private Integer userid;

    @Column(name = "userName")
    private String username;

    @Column(name = "buyersType")
    private String buyerstype;

    private Integer seq;

    @Column(name = "purchaseDate")
    private Date purchasedate;

    @Column(name = "payTime")
    private Date paytime;

    @Column(name = "orderStatus")
    private Integer orderstatus;

    @Column(name = "deliverStatus")
    private Integer deliverstatus;

    private BigDecimal freight;

    private BigDecimal amount;

    @Column(name = "payType")
    private String paytype;

    private String remark;

    @Column(name = "fromType")
    private Integer fromtype;

    @Column(name = "acceptTime")
    private Date accepttime;

    @Column(name = "rejectTime")
    private Date rejecttime;

    @Column(name = "rejectReason")
    private String rejectreason;

    @Column(name = "invoiceType")
    private String invoicetype;

    @Column(name = "invoiceContent")
    private String invoicecontent;

    @Column(name = "goodsNum")
    private Integer goodsnum;

    @Column(name = "pickUpWay")
    private String pickupway;

    @Column(name = "shopName")
    private String shopname;

    @Column(name = "refundsState")
    private String refundsstate;

    @Column(name = "bRefunds")
    private Boolean brefunds;

    @Column(name = "totalAmmount")
    private BigDecimal totalammount;

    @Column(name = "refundsReason")
    private String refundsreason;

    @Column(name = "bEvaluation")
    private Boolean bevaluation;

    @Column(name = "buyerTelephone")
    private String buyertelephone;

    @Column(name = "shopImgUrl")
    private String shopimgurl;

    private BigDecimal favorable;

    @Column(name = "payStatus")
    private String paystatus;

    @Column(name = "serialNumber")
    private String serialnumber;

    @Column(name = "ordersSummaryNo")
    private String orderssummaryno;

    @Column(name = "actualPayOrder")
    private String actualpayorder;

    @Column(name = "evaTime")
    private Date evatime;

    @Column(name = "seqSeller")
    private Integer seqseller;

    @Column(name = "receiveTime")
    private Date receivetime;

    @Column(name = "usedRedPacket")
    private BigDecimal usedredpacket;

    @Column(name = "isAutoTake")
    private Boolean isautotake;

    private String waybill;

    private String carrier;

    @Column(name = "deliveryWay")
    private String deliveryway;

    @Column(name = "approvedTime")
    private Date approvedtime;

    @Column(name = "goodsCode")
    private String goodscode;

    @Column(name = "countPrimitiveFactoryPrice")
    private BigDecimal countprimitivefactoryprice;

    @Column(name = "popMark")
    private Boolean popmark;

    @Column(name = "refundNo")
    private String refundno;

    @Column(name = "isServerCenterOrder")
    private Boolean isservercenterorder;

    @Column(name = "useBalance")
    private BigDecimal usebalance;

    @Column(name = "useCash")
    private BigDecimal usecash;

    @Column(name = "isRollbackBalance")
    private Integer isrollbackbalance;

    @Column(name = "deliveryMethod")
    private Integer deliverymethod;

    @Column(name = "consignerName")
    private String consignername;

    private Boolean sync;

    @Column(name = "totalAgentProfit")
    private BigDecimal totalagentprofit;

    @Column(name = "preferentialHowMany")
    private BigDecimal preferentialhowmany;

    @Column(name = "distrBuyAndSellStatus")
    private Integer distrbuyandsellstatus;

    @Column(name = "platformProcedure")
    private BigDecimal platformprocedure;

    @Column(name = "payProcedure")
    private BigDecimal payprocedure;

    @Column(name = "logisticsPrice")
    private BigDecimal logisticsprice;

    @Column(name = "profitAmount")
    private BigDecimal profitamount;

    @Column(name = "deliverTime")
    private Date delivertime;

    @Column(name = "shareSeq")
    private Integer shareseq;

    @Column(name = "isLocaleRecruitOrders")
    private String islocalerecruitorders;

    @Column(name = "largeOrderNo")
    private String largeorderno;

    @Column(name = "isSubOrders")
    private Boolean issuborders;

    @Column(name = "isLogistic")
    private String islogistic;

    @Column(name = "isConsumptionOrder")
    private Boolean isconsumptionorder;

    @Column(name = "afterCompleteTime")
    private Date aftercompletetime;

    @Column(name = "imgIdArrTime")
    private Date imgidarrtime;

    @Column(name = "imgIdArr")
    private String imgidarr;

    @Column(name = "isContainsAmount")
    private Integer iscontainsamount;

    @Column(name = "againAfterBeginTime")
    private Date againafterbegintime;

    @Column(name = "afterBeginTime")
    private Date afterbegintime;

    @Column(name = "afterWhy")
    private String afterwhy;

    @Column(name = "afterReason")
    private String afterreason;

    @Column(name = "lockDate")
    private Date lockdate;

    @Column(name = "isSingleLock")
    private Boolean issinglelock;

    @Column(name = "afterNo")
    private String afterno;

    @Column(name = "afterOrderno")
    private String afterorderno;

    @Column(name = "refusedAfterWhy")
    private String refusedafterwhy;

    private Integer route;

    @Column(name = "reviewStatus")
    private Integer reviewstatus;

    @Column(name = "companyName")
    private String companyname;

    @Column(name = "supplierSeq")
    private Integer supplierseq;

    @Column(name = "isComplain")
    private Boolean iscomplain;

    @Column(name = "isModifyBalanceA")
    private Boolean ismodifybalancea;

    @Column(name = "isModifyBalanceD")
    private Boolean ismodifybalanced;

    @Column(name = "returnAmount")
    private BigDecimal returnamount;

    @Column(name = "serverTime")
    private Date servertime;

    @Column(name = "distributorTime")
    private Date distributortime;

    @Column(name = "distributorStatus")
    private Integer distributorstatus;

    @Column(name = "serverStatus")
    private Integer serverstatus;

    @Column(name = "manualAccountTime")
    private Date manualaccounttime;

    @Column(name = "auditIntoAccountTime")
    private Date auditintoaccounttime;

    @Column(name = "auditUser")
    private String audituser;

    @Column(name = "auditIntoAccount")
    private Integer auditintoaccount;

    @Column(name = "distributorSeq")
    private Integer distributorseq;

    @Column(name = "serviceBuyAndSellStatus")
    private Integer servicebuyandsellstatus;

    @Column(name = "totalPlatformPrice")
    private BigDecimal totalplatformprice;

    @Column(name = "PlayMoney")
    private Integer playmoney;

    @Column(name = "Initiator")
    private String initiator;

    @Column(name = "expectedDeliveryTime")
    private Date expecteddeliverytime;

    @Column(name = "BalanceSynState")
    private Integer balancesynstate;

    @Column(name = "PlayMoneyTime")
    private Date playmoneytime;

    @Column(name = "IsOpenTicket")
    private Integer isopenticket;

    @Column(name = "BatchNumber")
    private String batchnumber;

    private static final long serialVersionUID = 1L;
}