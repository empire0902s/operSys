package com.shq.oper.model.domain.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author: ltg
 * @date: Created in 20:30 2018/12/5
 */
@lombok.Data
@Document(collection="t_deducitble_use_detail")
public class DeductibleUseDetail {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    //代金券，抵扣券id
    private Long deducitbleId;

    //订单号
    private String useOrderNo;

    //抵扣金额
    private Double deductibleAmount;

    //1使用，2还原
    private Short type;

    private String userSeq;

    private String createTime;

    private Double amount;

    //代金券，抵扣券id
    private Integer deducitbleType;

   //{"1019":{"couponsUserId":324,"useOrderNo":"3201812052033430018","deductibleAmount":51.0,"type":1,"userSeq":5764920}}

}
