package com.shq.oper.enums.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ltg
 * @date: Created in 16:54 2018/7/16
 */
public enum ChannelEnums {

    CHANNEL_CODE_ENUMS("获取渠道模板", "7001"),
    CHANNEL_GOOD_ENUMS("获取模板商品", "7002"),
    CHANNEL_BANNER_ENUMS("获取模板banner", "7003"),
    ACTIVITY_TEMPLATE_ENUMS("获取活动模板", "7004"),


    ;
    private String name;
    private String code;

    public String getName() {
        return name;
    }
    public String getCode() {
        return code;
    }
    private ChannelEnums(String name, String code) {
        this.name = name;
        this.code = code;
    }

    private static Map<String, ChannelEnums> map = new HashMap<>();
    private static Map<String, String> strMap = new HashMap<>();

    static {
        for(ChannelEnums ems : ChannelEnums.values()) {
            map.put(ems.getCode(), ems);
            strMap.put(ems.getName(),ems.getCode());
        }
    }
    public static ChannelEnums getByCode(String code) {
        return map.get(code);
    }

    public static Map<String, ChannelEnums> getMap(){
        return map;
    }


    public static String getValue(String key){
        return strMap.get(key);
    }
}
