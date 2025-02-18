package com.shq.oper.model.domain.mongo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** App位置信息数据，app每半个钟上报位置信息 **/
@lombok.Data
@Document(collection="t_app_location_data")
public class AppLocationData implements Serializable {
	private static final long serialVersionUID = 1L;
	/****/
	@Id
	private String id;

	private String createTime;
	
	
	/**
	 * 是否安装启动
	 */
	private Boolean isInstallStartUp = false;

	/**
	 * 渠道类型
	 * 0、520
	 * 1、360
	 * 2、百度
	 * 3、腾讯（应用宝）
	 * 4、搜狗
	 * 5、华为
	 * 6、小米
	 * 7、vivo
	 * 8、豌豆荚
	 * 9、OPPO
	 * 10、金立
	 * 99、IOS
	 */
	private Short channel;
	

	/** 设备唯一Id 	 **/
	private String deviceUniqueId = "";
	
	/**
	 * 设备类型：android,ios
	 */
	private String deviceType = "";

	/**
	 * 百度地图经度
	 */
	private String baiduLongitude = "";

	/**
	 * 百度地图纬度
	 */
	private String  baiduLatitude = "";

	
	/**
	 * 当前位置：通过经纬度，根据百度算出来的大致位置
	 */
	private String currLocation = "";	
	
	/**
	 * 当前IP
	 */
	private String loadIp = "";

	/**
	 * 用户seq(如果是安装首次启动 userSeq可以为空，否则不能为空)
	 */
	private String userSeq = "";	

	

	
}