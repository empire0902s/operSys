package com.shq.oper.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 优惠券状态
 * 
 * @author ljz
 * @date 2018年4月28日
 */
public enum BannerTypeEnum {

	ADVAPP("app启动页广告", "ADVAPP"), // app启动页广告
	;
	private String key;
    private String code;

    public String getCode() {
            return code;
            }
    final static Map<String, Object> objectMap = new LinkedHashMap<String, Object>();
        final static Map<String, BannerTypeEnum> map = new LinkedHashMap<String, BannerTypeEnum>();
        static {
            for (BannerTypeEnum BannerTypeEnum : BannerTypeEnum.values()) {
                map.put(BannerTypeEnum.getCode(), BannerTypeEnum);
                objectMap.put(BannerTypeEnum.getKey(),BannerTypeEnum.getCode());
            }
        }
    private BannerTypeEnum(String key, String code) {
            this.key = key;
            this.code = code;
            }

    public void setCode(String code) {
            this.code = code;
            }

    public String getKey() {
            return key;
            }


    public static Map<String, BannerTypeEnum> getMap() {
            return map;
            }
    public static Map<String, Object> getObjectMap() {
        return objectMap;
    }

    public void setKey(String key) {
            this.key = key;
            }


	public static BannerTypeEnum getByCode(String code) {
        return map.get(code);
    }
}
