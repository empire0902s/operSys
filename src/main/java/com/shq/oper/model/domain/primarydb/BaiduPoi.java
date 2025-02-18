package com.shq.oper.model.domain.primarydb;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: ltg
 * @date: Created in 16:27 2018/8/2
 */
@Data
@Table(name = "t_baidu_poi")
public class BaiduPoi implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 创建人(id-name)
     */
    @Column(name = "create_admin")
    private String createAdmin;

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

    //地店铺地址
    private String address;

    //维度
    private String latitude;

    //经度
    private String longitude;

    //店铺名
    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "geo_id")
    private Long geoId;


}
