package com.shq.oper.enums;

import java.util.LinkedHashMap;
import java.util.Map;


public enum ProductTypeEnum {

	ADVAPP("vip商品", "VIP"), // 商品类型
    BUSINESS("百业惠盟","business")//百业惠盟
	;
	private String key;
    private String code;

    public String getCode() {
            return code;
            }
    final static Map<String, Object> objectMap = new LinkedHashMap<String, Object>();
        final static Map<String, ProductTypeEnum> map = new LinkedHashMap<String, ProductTypeEnum>();
        static {
            for (ProductTypeEnum BannerTypeEnum : ProductTypeEnum.values()) {
                map.put(BannerTypeEnum.getCode(), BannerTypeEnum);
                objectMap.put(BannerTypeEnum.getKey(),BannerTypeEnum.getCode());
            }
        }
    private ProductTypeEnum(String key, String code) {
            this.key = key;
            this.code = code;
            }

    public void setCode(String code) {
            this.code = code;
            }

    public String getKey() {
            return key;
            }


    public static Map<String, ProductTypeEnum> getMap() {
            return map;
            }
    public static Map<String, Object> getObjectMap() {
        return objectMap;
    }

    public void setKey(String key) {
            this.key = key;
            }


	public static ProductTypeEnum getByCode(String code) {
        return map.get(code);
    }
}
