package com.shq.oper.model.dto;

import java.io.Serializable;

/**
 * @author: ltg
 * @date: Created in 15:59 2018/8/21
 */
@lombok.Data
public class HomePageItemDto implements Serializable {

    private String name;
//
//    private String createAdmin;
//
//    private String createTime;

    private Integer sortNum;

    private String moduleId;

    private Integer id;

    private Integer pid;

    private String goodsCode;

    private String imgUrl;

    private Boolean add;

    private Boolean delete;

    private Boolean modify;

    //模块id
    private String dataId;

    //1：item,2:detail,3:goods,4banner
    private Integer modeType;

    private String groupItemId;

    private String goodsItemId;

    private String imgTarget;

    private String imgTitle;

    private String productName;

    private String companyName;

    private Integer isSale;

    private Integer styleType;


}
