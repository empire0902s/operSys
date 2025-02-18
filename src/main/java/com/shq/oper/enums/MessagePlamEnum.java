package com.shq.oper.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: ltg
 * @date: Created in 19:01 2018/7/23
 */
public enum MessagePlamEnum {

    AIZHIJIAN_MESSAGE_ENUM("爱之家", 1),
    SHENGHUOQUAN_MESSAGE_ENUM("生活圈", 2),
    PIFA_MESSAGE_ENUM("520批发网", 3),
    ;
    private String key;
    private Integer code;

    public Integer getCode() {
        return code;
    }

    private MessagePlamEnum(String key, Integer code) {
        this.key = key;
        this.code = code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    private MessagePlamEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    final static Map<String, Object> map = new LinkedHashMap<String, Object>();

    public static Map<String, Object> getMap() {
        return map;
    }

    public void setKey(String key) {
        this.key = key;
    }

    static {
        for (MessagePlamEnum message : MessagePlamEnum.values()) {
            map.put(message.getKey(), message.getCode());
        }
    }

    public static MessagePlamEnum getNameByCode(String codeStr) {
        MessagePlamEnum enums = null;
        for (MessagePlamEnum tmp : MessagePlamEnum.values()) {
            if (tmp.getCode().toString().equals(codeStr)) {
                enums = tmp;
                break;
            }
        }
        return enums;
    }

    public static int getValue(String key){
        return Integer.parseInt(map.get(key)+"");
    }
}
