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
@Table(name = "t_user_behavior_statistics")
public class UserBehaviorStatistics implements Serializable {
	@Id
	@GeneratedValue(generator = "JDBC")
	private Long id;

	@Column(name = "create_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	/**
	 * 该统计记录的日期
	 */
	@Column(name = "statistics_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime statisticsTime;

	/**
	 * 独立访客量
	 */
	private Integer uv;

	/**
	 * ip访问量
	 */
	private Integer ip;

	/**
	 * 页面访问量
	 */
	private Integer pv;

	private static final long serialVersionUID = 1L;
}