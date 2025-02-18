package com.shq.oper.model.domain.mongo.channel;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * @author: ltg
 * @date: Created in 13:42 2018/9/11
 */
@Data
public class Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private Short sort;
    //1：普通商品，2：品牌广场，3：预售商品
    private Short goodsType;
    private String modelName;
    //banner数
    private Integer bannerNum;
//    private Special special;
//    private List<ChannelBanner> bannerList;
    //关联banner表
    private String bannerUniqeKey;
    //关联商品表
    private String goodUniqeKey;
    //模板id
    private String mId;

    private String createTime;
    private String createAdmin;

    //是否展示：0不显示，1显示
    private Integer isShow;
//    private List<ChannelGoods> goodsList;
}
