package com.shq.oper.model.dto.api.res;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: ltg
 * @date: Created in 14:00 2018/7/19
 */

@ApiModel(value="返回品牌广场商品范围列表DTO")
@lombok.Data
public class ResBrandCouponsGoodsListDto  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String goodsId;

    private String goodsName;

    private String goodsCode;

    private LocalDateTime createTime;


}
