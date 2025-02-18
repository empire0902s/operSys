package com.shq.oper.model.domain.mongo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

@lombok.Data
@Document(collection="t_guest_browsing_log")
public class GuestBrowsingLog  implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@Field("remort_ip")
	private String remortIP;
	
	/*cookieId*/
	@Field("cookieVisitor_uuid")
	private String cookieVisitorUuid;
	
	
	/*sessiond*/
	@Field("cookieVisitor_sessionid")
	private String cookieVisitorSessionid;	
	
	/*当前地址*/
	@Field("currentUrl")
	private String currentUrl;	
	
	/*操作方式*/
	@Field("operateMethod")
	private String operateMethod;

	
	/*浏览器内核*/
	@Field("browser_kernel")
	private String browserKernel;
	
	/*操作系统类型*/
	@Field("operating_system_type")
	private String operatingSystemType;

	/*操作系统版本*/
	@Field("operating_system_version")
	private String operatingSystemVersion;
	
	/** 创建时间 **/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;
	
	/*头部声明*/
	@Field("language")
	private String language;
	
	/*分辨率*/
	@Field("resolving_power")
	private String resolvingPower;

	/*PC端？手机端*/
	@Field("mobile_type")
	private String mobileType;
	
	/*用户代理*/
	@Field("userAgent")
	private String userAgent;
	
	/*该cookie访问次数*/
	@Field("cookieVisitCount")
	private Integer cookieVisitCount;
	
	/*配置追踪器地址id*/
	@Field("config_tracker_site_id")
	private String configTrackerSiteId;
	/*j*/
	@Field("cookie_visitorIdValues_currentVisitTs")
	private String cookieVisitorIdValuesCurrentVisitTs;
	
	/*城市*/
	@Field("city")
	private String city;
	
	/*省份*/
	@Field("province")
	private String province;
	
	/*用户编号*/
	@Field("userid")
	private String userid;
	
	/*性别*/
	@Field("gender")
	private String gender;
	
	/*年龄*/
	@Field("age")
	private String age;
	
	/*角色*/
	@Field("role")
	private String role;
	
	/*等级*/
	@Field("level")
	private String level;
	
	/*配置提交路径*/
	@Field("configReferrerUrl")
	private String configReferrerUrl;
	
	/*数据引擎*/
	@Field("engine")
	private String engine;
	
	/*窗体宽度*/
	@Field("winWidth")
	private String winWidth;
	
	/*窗体高度*/
	@Field("winHeight")
	private String winHeight;
	
}