package com.shq.oper.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * FastJson 封装工具类
 *
 * @author tjun
 */
public final class JsonUtils {

    private JsonUtils() {
        throw new UnsupportedOperationException();
    }

    // 序列化特性
    private static final SerializerFeature[] SERIALIZER_FEATURES = {
            SerializerFeature.NotWriteRootClassName,
            // 将 key 输出为字符串
            SerializerFeature.WriteNonStringKeyAsString,
            // 禁止循环引用检测
            SerializerFeature.DisableCircularReferenceDetect
    };
    
    // 序列化特性
    private static final SerializerFeature[] SERIALIZER_FEATURES_ENPTYWRITE = {
    		SerializerFeature.WriteNullStringAsEmpty,
    		SerializerFeature.NotWriteRootClassName,
    		// 将 key 输出为字符串
    		SerializerFeature.WriteNonStringKeyAsString,
    		// 禁止循环引用检测
    		SerializerFeature.DisableCircularReferenceDetect
    };

    // 反序列化特性
    private static final Feature[] PARSE_FEATURES = {
            Feature.DisableCircularReferenceDetect
    };

    public static String toJson(Object object,SerializerFeature... serializerFeature) {
        return JSON.toJSONString(object,serializerFeature);
    }

    public static String toDefaultJsonEmptyWrite(Object object) {
    	return JsonUtils.toJson(object, SERIALIZER_FEATURES_ENPTYWRITE);
    }
    
    public static String toDefaultJson(Object object) {
        return JsonUtils.toJson(object, SERIALIZER_FEATURES);
    }

    public static <T> T parse(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz, PARSE_FEATURES);
    }

    /**
     * 转换为 List
     */
    public static <T> List<T> parseList(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * 转换为 Map
     */
    public static List<Map<String, Object>> parseMap(String json) {
        return JSON.parseObject(json, new TypeReference<List<Map<String, Object>>>() {
        }, PARSE_FEATURES);
    }

}