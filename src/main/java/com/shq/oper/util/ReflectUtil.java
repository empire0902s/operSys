/**
 * 
 */
package com.shq.oper.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author HAI
 * 
 */
public class ReflectUtil {
	private static Log log = LogFactory.getLog(ReflectUtil.class);

	public static <T> T newnewInstance(String className) {
		Class<?> clazz = null;
		T t = null;
		try {
			clazz = Class.forName(className);

			t = (T) clazz.newInstance();

		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			log.error(e);
			e.printStackTrace();
		}

		return t;
	}

	public static <T, E> void setFieldValue(T target, String filedName, E value) {
		Class<?> clazzT = target.getClass();
		Field field;
		try {
			field = clazzT.getDeclaredField(filedName);
			setFieldValue(target, field, value);
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	public static <T, E> void setFieldValue(T target, Field field, E value) {
		try {
			if (field != null) {
				field.setAccessible(true);
				field.set(target, value);
			}
		} catch (SecurityException | IllegalArgumentException
				| IllegalAccessException e) {
			log.error(e);
			e.printStackTrace();
		}

	}

	public static <T> Object getFieldValue(T target, String filedName) {
		Class<?> clazzT = target.getClass();
		Field field = null;
		Object value = null;
		try {
			field = clazzT.getDeclaredField(filedName);
			value = getFieldValue(target, field);
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException e) {
			log.error(e);
			e.printStackTrace();
		}

		return value;
	}

	public static <T> Object getFieldValue(T target, Field field) {

		Object value = null;
		try {
			if (field != null) {
				field.setAccessible(true);
				value = field.get(target);
			}
		} catch (SecurityException | IllegalArgumentException
				| IllegalAccessException e) {
			log.error(e);
			e.printStackTrace();
		}

		return value;
	}

	public static <T> List<Field> getAllFields(T target) {
		Class<?> clazz = target.getClass();
		Class<?> pClazz = clazz.getSuperclass();
		List<Field> fieldList = new ArrayList<>();
		fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
		while (!Object.class.equals(pClazz)) {
			clazz = pClazz;
			fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
			pClazz = pClazz.getSuperclass();
		}

		return fieldList;
	}

}
