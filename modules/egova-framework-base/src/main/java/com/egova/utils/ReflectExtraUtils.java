package com.egova.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectExtraUtils {


    public static Object getFieldValue(Object object, Field field)
            throws Exception {
        Method m = object.getClass().getMethod(
                "get" + convertFirstUpper(field.getName()));

        Class<?> clazz = object.getClass();

        if (m == null && (field.getGenericType().toString().equals("boolean")
                || field.getGenericType().toString()
                .equals("class java.lang.Boolean"))) {
            m = clazz.getMethod("is" + convertFirstUpper(field.getName()));
        }

        return m.invoke(object);// 调用getter方法获取属性值
    }

    /**
     * 取得字段值
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        if (obj == null || fieldName == null || "".equals(fieldName)) {
            return null;
        }

        Class<?> clazz = obj.getClass();
        try {
            String methodname = "get" + StringUtils.capitalize(fieldName);
            Method method = clazz.getDeclaredMethod(methodname);

            if (method == null) {
                methodname = "is" + StringUtils.capitalize(fieldName);
                method = clazz.getDeclaredMethod(methodname);
            }

            method.setAccessible(true);
            return method.invoke(obj);
        } catch (Exception e) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public static void setFieldValue(Object target, String fname,
                                     Class<?> fieldClass, Object fieldObj) {
        if (!fieldClass.isAssignableFrom(fieldObj.getClass())) {
            return;
        }
        Class<?> clazz = target.getClass();
        try {
            Method method = clazz.getDeclaredMethod(
                    "set" + Character.toUpperCase(fname.charAt(0))
                            + fname.substring(1),
                    fieldClass);

            if (method == null) {
                String methodName = convertFirstUpperLower(fname);
                method = clazz.getDeclaredMethod(methodName);
            }

            method.setAccessible(true);
            method.invoke(target, fieldObj);
        } catch (Exception e) {
            try {
                Field field = clazz.getDeclaredField(fname);
                field.setAccessible(true);
                field.set(target, fieldObj);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static <T extends Annotation> T getAnnotation(Class<?> clazz,
                                                         String fieldName, Class<T> annotationClass) {

        try {

            T annotation = getFieldAnnotation(clazz, fieldName,
                    annotationClass);
            if (annotation == null) {
                annotation = getMethodAnnotation(clazz, fieldName,
                        annotationClass);
            }
            return annotation;

        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }

    public static <T extends Annotation> T getFieldAnnotation(Class<?> clazz,
                                                              String fieldName, Class<T> annotationClass) {

        try {

            Field field = clazz.getDeclaredField(fieldName);
            if (field != null && field.isAnnotationPresent(annotationClass)) {
                return field.getAnnotation(annotationClass);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }

    public static <T extends Annotation> T getMethodAnnotation(Class<?> clazz,
                                                               String fieldName, Class<T> annotationClass) {

        try {
            String methodname = "get" + StringUtils.capitalize(fieldName);
            Method method = clazz.getDeclaredMethod(methodname);

            if (method == null) {
                methodname = "is" + StringUtils.capitalize(fieldName);
                method = clazz.getDeclaredMethod(methodname);
            }
            if (method.isAnnotationPresent(annotationClass)) {
                return method.getAnnotation(annotationClass);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }

    private static String convertFirstUpperLower(String str) {
        if (str == null || str.trim().length() == 0) {
            return str;
        }
        byte[] items = str.getBytes();
        if (items[0] <= 'Z') {
            items[0] = (byte) ((char) items[0] + 'a' - 'A');
            return new String(items);
        } else {
            return str;
        }

    }

    private static String convertFirstUpper(String str) {
        if (str == null || str.trim().length() == 0) {
            return str;
        }
        byte[] items = str.getBytes();
        if (items[0] >= 'a') {
            items[0] = (byte) ((char) items[0] - 'a' + 'A');
            return new String(items);
        } else {
            return str;
        }

    }

}
