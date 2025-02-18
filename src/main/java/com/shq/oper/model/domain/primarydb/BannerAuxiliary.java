package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "t_banner_auxiliary")
public class BannerAuxiliary implements Serializable {
    @Id
    private Long id;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * logo图片地址
     */
    @Column(name = "logo_path")
    private String logoPath;

    /**
     * banner图片地址
     */
    @Column(name = "banner_img_path")
    private String bannerImgPath;

    /**
     * 活动图片地址
     */
    @Column(name = "activity_img_path")
    private String activityImgPath;

    /**
     * 主表标识
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 最近更新人
     */
    @Column(name = "update_admin")
    private String updateAdmin;

    private static final long serialVersionUID = 1L;
}