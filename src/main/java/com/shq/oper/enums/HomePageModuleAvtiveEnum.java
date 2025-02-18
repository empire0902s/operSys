package com.shq.oper.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 优惠券状态
 * 
 * @author ljz
 * @date 2018年8月16日
 */
public enum HomePageModuleAvtiveEnum {

	ORDINARY_GOODS("普通模块", "0"), // 普通商品
	SECKILL_ACTIVITY("秒杀活动", "1"), // 秒杀活动
	ADVANCE_SALE_ACTIVITY("预售活动", "3"), // 预售活动
	BRAND_SQUARE("品牌广场", "4"), // 预售活动
	NON_SECOND_KILL_COMMODITY_SQUARE("非秒杀商品广场", "5"), // 预售活动
	;
	private String key;
    private String code;

    public String getCode() {
            return code;
            }
    final static Map<String, Object> objectMap = new LinkedHashMap<String, Object>();
        final static Map<String, HomePageModuleAvtiveEnum> map = new LinkedHashMap<String, HomePageModuleAvtiveEnum>();
        static {
            for (HomePageModuleAvtiveEnum HomePageModuleAvtiveEnum : HomePageModuleAvtiveEnum.values()) {
                map.put(HomePageModuleAvtiveEnum.getCode(), HomePageModuleAvtiveEnum);
                objectMap.put(HomePageModuleAvtiveEnum.getKey(),HomePageModuleAvtiveEnum.getCode());
            }
        }
    private HomePageModuleAvtiveEnum(String key, String code) {
            this.key = key;
            this.code = code;
            }

    public void setCode(String code) {
            this.code = code;
            }

    public String getKey() {
            return key;
            }


    public static Map<String, HomePageModuleAvtiveEnum> getMap() {
            return map;
            }
    public static Map<String, Object> getObjectMap() {
        return objectMap;
    }

    public void setKey(String key) {
            this.key = key;
            }


	public static HomePageModuleAvtiveEnum getByCode(String code) {
        return map.get(code);
    }
}
