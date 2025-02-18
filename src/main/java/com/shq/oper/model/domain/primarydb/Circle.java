package com.shq.oper.model.domain.primarydb;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 发圈实体
 * @author: ltg
 * @date: Created in 14:32 2018/10/19
 */
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
@Table(name = "t_circle")
public class Circle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 头像
     */
    @Column(name = "head_img")
    private String headImg;

    /**
     * 1：每日爆款，2：宣传推广
     */
    @Column(name = "type")
    private Short type;

    /**
     *是否展示（1,：展示，2：不展示）
     */
    @Column(name = "is_show")
    private Short isShow;

    /**
     *内容
     */
    @Column(name = "content")
    private String content;

    /**
     *图片
     */
    @Column(name = "picture")
    private String picture;

    /**
     *商品Sku
     */
    @Column(name = "commodity_sku")
    private String commoditySku;

    /**
     *分享次数
     */
    @Column(name = "share_num")
    private Integer shareNum;

    /**
     *真实分享次数
     */
    @Column(name = "real_share")
    private Integer realShare;

    /**
     *点击次数
     */
    @Column(name = "click_num")
    private Integer clickNum;

    /**
     *点击人数
     */
    @Column(name = "click_people_num")
    private Integer clickPeopleNum;

    /**
     * 查看次数
     */
    @Column(name = "reed_num")
    private Integer reedNum;

    /**
     * 查看人数
     */
    @Column(name = "reed_people_num")
    private Integer reedPeopleNum;

    /**
     * 评论
     */
    @Column(name = "comment")
    private String comment;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
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

    /**
     * 商品类型（1：普通商品，2：秒杀商品）
     */
    @Column(name = "goods_type")
    private Integer goodsType;

    @Transient
    private String timeRange;

    @Transient
    private LocalDateTime beginTime;

    @Transient
    private LocalDateTime endTime;


}
