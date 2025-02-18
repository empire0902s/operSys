package com.shq.oper.model.domain.mongo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/** app安装与启动数据统计 **/
@lombok.Data
@Document(collection="t_app_data_statistics")
public class AppDataStatistics implements Serializable {
	private static final long serialVersionUID = 1L;
	/****/
	@Id
	private String id;

	/** 安装次数 **/
	@Field("installation_counts")
	private long installationCounts;

	/** 启动次数 **/
	@Field("startup_counts")
	private long startupCounts ;

	/**
	 * 渠道520
	 */
	private long instaCounts520;

	/**
	 * 渠道360
	 */
	private long instaCounts360;

	/**
	 * 渠道百度
	 */
	private long instaCountBaidu;

	/**
	 * 渠道腾讯
	 */
	private long instaCountsTencent;

	/**
	 * 渠道搜狗
	 */
	private long instaCountSugou;

	/**
	 * 渠道华为
	 */
	private long instaCountHuawei;

	/**
	 * 渠道小米
	 */
	private long instaCountXiaomi;

	/**
	 * 渠道vivo
	 */
	private long instaCountVivo;

	/**
	 * 渠道豌豆荚
	 */
	private long instaCountWandoujia;

	/**
	 * 渠道oppo
	 */
	private long instaCountOPPO;

	/**
	 * 渠道金立
	 */
	private long instaCountJinli;

	/**
	 * 渠道IOS
	 */
	private long instaCountIOS;

	/** 创建时间 **/
	@Field("create_time")
	private String createTime;
	
	/** 创建时间(用作可查询时间范围) **/
	@Field("check_create_time")
	private LocalDateTime checkCreateTime;



}