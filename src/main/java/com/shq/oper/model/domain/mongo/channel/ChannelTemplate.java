package com.shq.oper.model.domain.mongo.channel;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * @author: ltg
 * @date: Created in 16:23 2018/9/6
 */
@Data
@Document(collection="t_channel_template")
public class ChannelTemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String createAdmin;
    private String createTime;
    private String updateAdmin;
    private String updateTime;
    private String channelNum;
    private String name;
    //爱之家简介说明
    private String description;
    private Short templateType;
//    private String bannerImg;
    private String brandDescImg;

    //爱之家简介说明图片
//    private String loveDescImg;
    //二维码图
    private String qrCodeUrl;
    private String iosLoadUrl;
    private String androndLoadUrl;

    //启用状态 1：启用，2禁用
    private Integer state;


    private List<Model> models;

}
