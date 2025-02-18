package com.shq.oper.model.domain.mongo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: ltg
 * @date: Created in 16:22 2018/11/19
 */
@Data
public class Product  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String Id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品code
     */
    private String goodsCode;

    /**
     * 供应商名
     */
    private String companyName;


    /**
     * 百业惠盟商品：{0：普通商品；1:专区商品}
     **/
    private Short ifWYFcommodity;

    /**
     * 商品状态
     * 公司控制商品状态优先权：0：显示 （商家） 1:不显示(下架)
     */
    private int companyState;

    /**
     * 是否品牌商商品   1：品牌商品,0:平台
     */
    private Integer ifBrandProduct;


}
