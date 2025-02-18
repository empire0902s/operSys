package com.shq.oper.enums.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: ltg
 * @date: Created in 16:54 2018/7/16
 */
public enum MessageSendEnum {

    AIZHIJIA_CODE_ENUMS("爱之家", "4001"),
    SHENGHUOQUAN_CODE_ENUMS("生活圈", "4002"),
    SYS_520_WHOLESALE_NETWORK("520批发网", "4003"),
    MESSAGE_ORDER_SEND_CODE_ENUMS("爱之家推送","4004"),
    ;
    private String name;
    private String code;

    public String getName() {
        return name;
    }
    public String getCode() {
        return code;
    }
    private MessageSendEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    private static Map<String, MessageSendEnum> map = new HashMap<>();
    private static Map<String, String> strMap = new HashMap<>();

    static {
        for(MessageSendEnum ems : MessageSendEnum.values()) {
            map.put(ems.getCode(), ems);
            strMap.put(ems.getName(),ems.getCode());
        }
    }
    public static MessageSendEnum getByCode(String code) {
        return map.get(code);
    }

    public static Map<String, MessageSendEnum> getMap(){
        return map;
    }


    public static String getValue(String key){
        return strMap.get(key);
    }
}
