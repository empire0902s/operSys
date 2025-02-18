package com.shq.oper.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 优惠券状态
 * 
 * @author ljz
 * @date 2018年8月16日
 */
public enum BrandSquareModularTypeEnum {

	BRANDSQUARE_HOMEPAGE_BRAND_RECOMMENDATION_MODULE("首页推荐品牌", "1"),
	BRANDSQUARE_HOT_SALE_MODULE("特卖会", "2"),
	BANNER_AND_GOODS("热门分类商品", "7")/*,
	HOMEPAGE_BANNER("首页banner图", "8"),*/
	/*BRAND_STORE_MODULE_THREE("品牌商品模块3", "5"), */
	;
	private String key;
    private String code;

    public String getCode() {
            return code;
            }
    final static Map<String, Object> objectMap = new LinkedHashMap<String, Object>();
        final static Map<String, BrandSquareModularTypeEnum> map = new LinkedHashMap<String, BrandSquareModularTypeEnum>();
        static {
            for (BrandSquareModularTypeEnum HomePageModuleAvtiveEnum : BrandSquareModularTypeEnum.values()) {
                map.put(HomePageModuleAvtiveEnum.getCode(), HomePageModuleAvtiveEnum);
                objectMap.put(HomePageModuleAvtiveEnum.getKey(),HomePageModuleAvtiveEnum.getCode());
            }
        }
    private BrandSquareModularTypeEnum(String key, String code) {
            this.key = key;
            this.code = code;
            }

    public void setCode(String code) {
            this.code = code;
            }

    public String getKey() {
            return key;
            }


    public static Map<String, BrandSquareModularTypeEnum> getMap() {
            return map;
            }
    public static Map<String, Object> getObjectMap() {
        return objectMap;
    }

    public void setKey(String key) {
            this.key = key;
            }


	public static BrandSquareModularTypeEnum getByCode(String code) {
        return map.get(code);
    }
}
