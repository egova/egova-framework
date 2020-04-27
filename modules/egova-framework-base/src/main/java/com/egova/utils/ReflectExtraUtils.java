package com.egova.utils;

import com.egova.exception.FrameworkException;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具
 *
 * @author chenabao
 */
public class ReflectExtraUtils {

    /**
     * 获取字段值
     *
     * @param object 对象
     * @param field  对象字段
     * @return
     * @throws Exception
     */
    public static Object getFieldValue(Object object, Field field) {
        try {

            Method m = object.getClass().getMethod("get" + convertFirstUpper(field.getName()));

            Class<?> clazz = object.getClass();

            if (m == null && ("boolean".equals(field.getGenericType().toString())
                    || "class java.lang.Boolean"
                    .equals(field.getGenericType().toString()))) {
                m = clazz.getMethod("is" + convertFirstUpper(field.getName()));
            }
            // 调用getter方法获取属性值
            assert m != null;
            return m.invoke(object);
        } catch (NoSuchMethodException e) {
            throw new FrameworkException("没有找到字段的get方法", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FrameworkException("不能访问该字段的get方法", e);
        }
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
            String methodName = "get" + StringUtils.capitalize(fieldName);
            Method method = clazz.getDeclaredMethod(methodName);

            if (method == null) {
                methodName = "is" + StringUtils.capitalize(fieldName);
                method = clazz.getDeclaredMethod(methodName);
            }

            method.setAccessible(true);
            return method.invoke(obj);

        } catch (Exception e) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception ex) {
                throw new FrameworkException("不能访问该字段的get方法", e);
            }
        }
    }

    /**
     * 设置字段的值
     *
     * @param target     对象
     * @param fieldName  字段名
     * @param fieldClass 字段类型
     * @param fieldValue 字段值
     */
    public static void setFieldValue(Object target, String fieldName,
                                     Class<?> fieldClass, Object fieldValue) {
        if (!fieldClass.isAssignableFrom(fieldValue.getClass())) {
            return;
        }
        Class<?> clazz = target.getClass();
        try {
            Method method = clazz.getDeclaredMethod(
                    "set" + Character.toUpperCase(fieldName.charAt(0))
                            + fieldName.substring(1),
                    fieldClass);

            if (method == null) {
                String methodName = convertFirstLower(fieldName);
                method = clazz.getDeclaredMethod(methodName);
            }

            method.setAccessible(true);
            method.invoke(target, fieldValue);

        } catch (Exception e) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, fieldValue);
            } catch (Exception e1) {
                throw new FrameworkException("设置字段的值异常", e);
            }
        }
    }

    /**
     * 获取字段的注解
     *
     * @param clazz           类型
     * @param fieldName       字段名
     * @param annotationClass 注解类型
     * @param <T>             注解约束
     * @return
     */
    public static <T extends Annotation> T getAnnotation(Class<?> clazz,
                                                         String fieldName, Class<T> annotationClass) {
        try {

            T annotation = getFieldAnnotation(clazz, fieldName, annotationClass);
            if (annotation == null) {
                annotation = getMethodAnnotation(clazz, fieldName, annotationClass);
            }
            return annotation;

        } catch (Exception e) {
            throw new FrameworkException("获取字段注解", e);
        }
    }

    /**
     * 获取字段注解
     *
     * @param clazz           类型
     * @param fieldName       字段名称
     * @param annotationClass 注解类型
     * @param <T>             注解约束
     * @return
     */
    public static <T extends Annotation> T getFieldAnnotation(Class<?> clazz,
                                                              String fieldName, Class<T> annotationClass) {
        try {

            Field field = clazz.getDeclaredField(fieldName);
            if (field != null && field.isAnnotationPresent(annotationClass)) {
                return field.getAnnotation(annotationClass);
            }
            return null;

        } catch (Exception e) {
            throw new FrameworkException("获取字段注解", e);
        }
    }


    public static <T extends Annotation> T getMethodAnnotation(Class<?> clazz,
                                                               String fieldName, Class<T> annotationClass) {
        try {
            String methodName = "get" + StringUtils.capitalize(fieldName);
            Method method = clazz.getDeclaredMethod(methodName);

            if (method == null) {
                methodName = "is" + StringUtils.capitalize(fieldName);
                method = clazz.getDeclaredMethod(methodName);
            }
            if (method.isAnnotationPresent(annotationClass)) {
                return method.getAnnotation(annotationClass);
            }
            return null;

        } catch (Exception e) {
            throw new FrameworkException("获取方法注解", e);
        }

    }

    /**
     * 设置第一个字符为小写
     *
     * @param str
     * @return
     */
    private static String convertFirstLower(String str) {
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

    /**
     * 设置第一个字符为大写
     *
     * @param str
     * @return
     */
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
