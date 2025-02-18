package com.shq.oper.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: ltg
 * @date: Created in 9:51 2018/6/12
 */
public enum ActivityGoodsRuleTypeEnum {

    PORTFOLIO("自由选择商品", "0"), // 自由选择商品
    FIRST_CATEGORY("一级类目", "1"),   //1级类目
    TWO_CATEGORY("二级类目", "2"),// 2级类目
    THREE_CATEGORY("三级类目", "3"),   //3级类目
    FOUR_CATEGORY("四级类目", "4"),// 4级类目
    SUPPLIER("供应商", "5"),   //供应商
        ;
    private String key;
    private String code;

    public String getCode() {
            return code;
            }
    final static Map<String, Object> objectMap = new LinkedHashMap<String, Object>();
        final static Map<String, ActivityGoodsRuleTypeEnum> map = new LinkedHashMap<String, ActivityGoodsRuleTypeEnum>();
        static {
            for (ActivityGoodsRuleTypeEnum activityGoodsRuleTypeEnum : ActivityGoodsRuleTypeEnum.values()) {
                map.put(activityGoodsRuleTypeEnum.getCode(), activityGoodsRuleTypeEnum);
                objectMap.put(activityGoodsRuleTypeEnum.getKey(),activityGoodsRuleTypeEnum.getCode());
            }
        }
    private ActivityGoodsRuleTypeEnum(String key, String code) {
            this.key = key;
            this.code = code;
            }

    public void setCode(String code) {
            this.code = code;
            }

    public String getKey() {
            return key;
            }


    public static Map<String, ActivityGoodsRuleTypeEnum> getMap() {
            return map;
            }
    public static Map<String, Object> getObjectMap() {
        return objectMap;
    }

    public void setKey(String key) {
            this.key = key;
            }


    public static ActivityGoodsRuleTypeEnum getByCode(String code) {
        return map.get(code);
    }

}
