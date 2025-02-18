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
@Table(name = "t_banner_model_goods_detail")
public class BannerModelGoodsDetail implements Serializable {
	/**
	 * 用户-角色关系Id
	 */
	@Id
	private Long id;

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
	 * BannerId
	 */
	@Column(name = "banner_id")
	private Long bannerId;

	/**
	 * 商品Code
	 */
	@Column(name = "goods_code")
	private String goodsCode;

	/**
	 * 1级类目Id
	 */
	@Column(name = "first_class_id")
	private String firstClassId;

	/**
	 * 2级类目Id
	 */
	@Column(name = "second_class_id")
	private String secondClassId;

	/**
	 * 3级类目Id
	 */
	@Column(name = "three_class_id")
	private String threeClassId;

	/**
	 * 4级类目Id
	 */
	@Column(name = "four_class_id")
	private String fourClassId;

	/**
	 * 排序
	 */
	@Column(name = "sort_no")
	private Short sortNo;

	/**
	 * 是否禁用（0为否，1为是）
	 */
	@Column(name = "is_disabled")
	private Boolean isDisabled;
	
	@Transient
	private String productName;
	

	private static final long serialVersionUID = 1L;
}