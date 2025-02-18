package com.shq.oper.enums.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ltg
 * @date: Created in 16:54 2018/8/31
 */
public enum BaiduPositionReportEnum {

    APP_POSITTION_REPORT_ENUM("APP定位上报", "6001"),
    ;
    private String name;
    private String code;

    public String getName() {
        return name;
    }
    public String getCode() {
        return code;
    }
    private BaiduPositionReportEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    private static Map<String, BaiduPositionReportEnum> map = new HashMap<>();
    private static Map<String, String> strMap = new HashMap<>();

    static {
        for(BaiduPositionReportEnum ems : BaiduPositionReportEnum.values()) {
            map.put(ems.getCode(), ems);
            strMap.put(ems.getName(),ems.getCode());
        }
    }
    public static BaiduPositionReportEnum getByCode(String code) {
        return map.get(code);
    }

    public static Map<String, BaiduPositionReportEnum> getMap(){
        return map;
    }


    public static String getValue(String key){
        return strMap.get(key);
    }
}
