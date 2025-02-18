package com.shq.oper.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 优惠券状态
 * 
 * @author ljz
 * @date 2018年8月16日
 */
public enum HomePageModuleStyleTypeEnum {

	BROADCAST_BANNER("轮播Banner", "1"),
	COUPON_BANNER("优惠券Banner", "2"),
	ADVERTISING_PICTURES("广告图", "3"),
	COMMODITY_SLIDING_COLUMN("商品滑动栏", "4"),
	LOGO_BANNER("logo图Banner", "5"), 
	BANNER_PICTURES("Banner图片", "6"),
	BRAND_SQUARE("品牌广场", "7"),
	SECKILL("秒杀", "8"),
	GENERAL_MERCHANDISE_LIST("普通商品列表", "9"),
	SIMILAR_ACTIVITIES_COLUMN_IN_BRAND_PLAZA("品牌广场类似活动栏目", "10"),
	ACTIVITY_BANNER_COLLECTION("活动banner集合", "11"),
	;
	private String key;
    private String code;

    public String getCode() {
            return code;
            }
    final static Map<String, Object> objectMap = new LinkedHashMap<String, Object>();
        final static Map<String, HomePageModuleStyleTypeEnum> map = new LinkedHashMap<String, HomePageModuleStyleTypeEnum>();
        static {
            for (HomePageModuleStyleTypeEnum HomePageModuleAvtiveEnum : HomePageModuleStyleTypeEnum.values()) {
                map.put(HomePageModuleAvtiveEnum.getCode(), HomePageModuleAvtiveEnum);
                objectMap.put(HomePageModuleAvtiveEnum.getKey(),HomePageModuleAvtiveEnum.getCode());
            }
        }
    private HomePageModuleStyleTypeEnum(String key, String code) {
            this.key = key;
            this.code = code;
            }

    public void setCode(String code) {
            this.code = code;
            }

    public String getKey() {
            return key;
            }


    public static Map<String, HomePageModuleStyleTypeEnum> getMap() {
            return map;
            }
    public static Map<String, Object> getObjectMap() {
        return objectMap;
    }

    public void setKey(String key) {
            this.key = key;
            }


	public static HomePageModuleStyleTypeEnum getByCode(String code) {
        return map.get(code);
    }
}
