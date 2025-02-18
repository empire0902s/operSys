package com.shq.oper.model.domain.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/** 代金券抵扣券日统计**/
@lombok.Data
@Document(collection="t_deducitble_statistics_day")
public class DeducitbleStatisticsDay implements Serializable {
	private static final long serialVersionUID = 1L;
	/****/
	@Id
	private String id;

	private String createTime;

	private String statisticsTime;

	/**
	 * 获取抵扣券用户数
	 */
	private Long userNum=0l;

	/**
	 * 抵扣券使用金额
	 */
	private Double usedAmount=0.0;

	/**
	 * 抵扣券使用人数
	 */
	private Long usedNum=0l;

	/**
	 * 升级清空人数
	 */
	private Long deleteNum=0l;

	/**
	 * 清空总余额
	 */
	private Double deleteAmout=0.0;

	/**
	 * 余额总数
	 */
	private Double balanceTotal=0.0;

	/**
	 * 未获取抵扣券数
	 */
	private Long loseNum=0l;

	/**
	 * 1.代金券，2抵扣券
	 */
	private Integer type;


}