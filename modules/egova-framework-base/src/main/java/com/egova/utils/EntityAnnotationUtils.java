package com.egova.utils;

import com.flagwind.reflect.entities.EntityField;
import com.flagwind.reflect.entities.EntityType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author chendb
 * @description: 注解帮助类
 * @date 2020-04-17 08:02:46
 */
public class EntityAnnotationUtils {

    /**
     * 是否有该注解
     *
     * @param entityType     实体元数据
     * @param annotationType 注解类型
     * @return
     */
    public boolean isAnnotated(EntityType entityType, Class<? extends Annotation> annotationType) {
        return AnnotatedElementUtils.isAnnotated(entityType.getInstanceType(), annotationType);
    }

    /**
     * 获取实体类型的注释
     *
     * @param entityType     实体元数据
     * @param annotationType 注解类型
     * @param <A>            注解约束
     * @return
     */
    public static <A extends Annotation> A getMergedAnnotation(EntityType entityType, Class<A> annotationType) {
        return AnnotatedElementUtils.getMergedAnnotation(entityType.getInstanceType(), annotationType);
    }

    /**
     * 获取重复的注解
     *
     * @param entityType     实体元数据
     * @param annotationType 注解类型
     * @param <A>            注解约束
     * @return
     */
    public static <A extends Annotation> Set<A> getMergedRepeatableAnnotations(EntityType entityType, Class<A> annotationType) {
        return AnnotatedElementUtils.getMergedRepeatableAnnotations(entityType.getInstanceType(), annotationType);
    }

    /**
     * 获取实体字段类型的注释
     *
     * @param field          实体字段元数据
     * @param annotationType 注解类型
     * @param <A>            注解约束
     * @return
     */
    public static <A extends Annotation> A getMergedAnnotation(EntityField field, Class<A> annotationType) {
        A result = null;
        if (field.getField() != null) {
            result = AnnotatedElementUtils.getMergedAnnotation(field.getField(), annotationType);
        }
        if (result == null && field.getGetter() != null) {
            result = AnnotatedElementUtils.getMergedAnnotation(field.getGetter(), annotationType);
        }
        if (result == null && field.getSetter() != null) {
            result = AnnotatedElementUtils.getMergedAnnotation(field.getSetter(), annotationType);
        }
        if (result == null && field.getJavaType() != null) {
            result = AnnotatedElementUtils.getMergedAnnotation(field.getJavaType(), annotationType);
        }
        return result;
    }

    /**
     * 获取重复的注解
     *
     * @param field          实体字段元数据
     * @param annotationType 注解类型
     * @param <A>            注解约束
     * @return
     */
    public static <A extends Annotation> Set<A> getMergedRepeatableAnnotations(EntityField field, Class<A> annotationType) {
        Set<A> result = null;
        if (field.getField() != null) {
            result = AnnotatedElementUtils.getMergedRepeatableAnnotations(field.getField(), annotationType);
        }
        if (CollectionUtils.isEmpty(result) && field.getGetter() != null) {
            result = AnnotatedElementUtils.getMergedRepeatableAnnotations(field.getGetter(), annotationType);
        }
        if (CollectionUtils.isEmpty(result) && field.getSetter() != null) {
            result = AnnotatedElementUtils.getMergedRepeatableAnnotations(field.getSetter(), annotationType);
        }
        if (CollectionUtils.isEmpty(result) && field.getJavaType() != null) {
            result = AnnotatedElementUtils.getMergedRepeatableAnnotations(field.getJavaType(), annotationType);
        }
        return result;
    }


    /**
     * 是否有该注解
     *
     * @param field          字段元数据
     * @param annotationType 注解类型
     * @return
     */
    public boolean isAnnotated(EntityField field, Class<? extends Annotation> annotationType) {
        boolean result = false;
        if (field.getField() != null) {
            result = AnnotatedElementUtils.isAnnotated(field.getField(), annotationType);
        }
        if (!result && field.getGetter() != null) {
            result = AnnotatedElementUtils.isAnnotated(field.getGetter(), annotationType);
        }
        if (!result && field.getSetter() != null) {
            result = AnnotatedElementUtils.isAnnotated(field.getSetter(), annotationType);
        }
        if (!result && field.getJavaType() != null) {
            result = AnnotatedElementUtils.isAnnotated(field.getJavaType(), annotationType);
        }
        return result;
    }
}
