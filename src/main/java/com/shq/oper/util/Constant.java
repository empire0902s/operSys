package com.shq.oper.util;

/**
 * 系统常量
 * 
 * @author tjun
 * @createBy 2017年4月10日
 */
public class Constant {
	public final static String OPER_USER_AES_KEY = "SQP_OPERATE_2018";
	public final static String OPER_USER = "OPER_USER";
	public final static String OPER_RESOURCE = "OPER_RESOURCE";
	public final static String OPER_TOKEN = "OPER_TOKEN";
	public final static String OPER_SYSTEM_CODE = "OPER_SYSTEM_CODE";
	public final static String OPER_SYSTEM_CODE_VALUE = "0000";
	public final static String SYSTEM_CODE = "SYSTEM_CODE";
	public final static String SMS_PHONE = "SMS_PHONE";
	public final static String NO_IMG_CODE = "NO_IMG_CODE";
	public final static String SMS_CODE = "SMS_CODE";
	public final static String IMG_CODE = "IMG_CODE";
	public final static String SMS_TIME = "SMS_TIME";//

	/**优惠券默认来源系统  运营系统编号**/
	public final static String COUPONS_FROM_SYS_CODE_DEFAULT = "operSys_Code";//
	
	
	
	/***Redis 缓存 15秒**/
	public final static String CACHEKEY_SECOND_15= "OPERSYS_SECOND_15";//  15秒
	/***Redis 缓存 20分钟**/
	public final static String CACHEKEY_MINUTE_20= "OPERSYS_MINUTE_20";
	/***Redis 缓存 12小时**/
	public final static String CACHEKEY_HOUR_12= "OPERSYS_HOUR_12";// 12小时
	
	/***Redis 缓存 1分钟**/
	public final static String OPERSYS_USER_LOGIN_VERIFICATION_CODE= "OPERSYS_USER_LOGIN_VERIFICATION_CODE";// 1分钟
	
	public static String getkey() {
		return Constant.CACHEKEY_HOUR_12;
	}
}
