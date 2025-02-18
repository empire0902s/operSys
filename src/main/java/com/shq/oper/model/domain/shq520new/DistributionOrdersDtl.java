package com.shq.oper.model.domain.shq520new;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "Distribution_OrdersDtl")
public class DistributionOrdersDtl implements Serializable {
    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "orderDetailId")
    private String orderdetailid;

    @Column(name = "orderId")
    private String orderid;

    private String orderno;

    @Column(name = "goodsId")
    private String goodsid;

    private Integer count;

    private BigDecimal price;

    private String unit;

    @Column(name = "discountPrice")
    private BigDecimal discountprice;

    private String attributes;

    @Column(name = "goodsName")
    private String goodsname;

    private String spec;

    private BigDecimal amount;

    private String remark;

    @Column(name = "evaluationContent")
    private String evaluationcontent;

    @Column(name = "goodsImgUrl")
    private String goodsimgurl;

    @Column(name = "bEvaluation")
    private Boolean bevaluation;

    private String promotion;

    @Column(name = "orderSku")
    private String ordersku;

    @Column(name = "insertOrderType")
    private String insertordertype;

    @Column(name = "preferentialHowMany")
    private BigDecimal preferentialhowmany;

    @Column(name = "primitiveFactoryPrice")
    private BigDecimal primitivefactoryprice;

    @Column(name = "cost_unit_price")
    private BigDecimal costUnitPrice;

    @Column(name = "factoryPrice")
    private BigDecimal factoryprice;

    @Column(name = "subOrdersNo")
    private String subordersno;

    @Column(name = "isActivityGoods")
    private String isactivitygoods;

    @Column(name = "isDistrbutionGoods")
    private Boolean isdistrbutiongoods;

    @Column(name = "agentProfit")
    private BigDecimal agentprofit;

    @Column(name = "platformPrice")
    private BigDecimal platformprice;

    @Column(name = "logisticsPrice")
    private BigDecimal logisticsprice;

    @Column(name = "States")
    private Integer states;

    @Column(name = "useCash")
    private BigDecimal usecash;

    @Column(name = "useBalance")
    private BigDecimal usebalance;

    @Column(name = "goodsCode")
    private String goodscode;

    @Column(name = "activeColumnMark")
    private String activecolumnmark;

    private static final long serialVersionUID = 1L;
}