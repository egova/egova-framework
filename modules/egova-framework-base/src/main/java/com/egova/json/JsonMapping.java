package com.egova.json;

import java.io.Reader;
import java.util.List;

/**
 * @author chendb
 * @description: json操作接口定义
 * @date 2020-04-14 14:48:17
 */
public interface JsonMapping {

    /**
     * 反序列化对象
     *
     * @param json  JSON字符串
     * @param clazz 反序列化类型
     * @param <T>   类型
     * @return 类型
     */
    <T> T deserialize(String json, Class<T> clazz);

    /**
     * 反序列化对象
     *
     * @param reader JSON Reader
     * @param clazz  反序列化类型
     * @param <T>    类型
     * @return 类型
     */
    <T> T deserialize(Reader reader, Class<T> clazz);

    /**
     * 反序列化成list
     *
     * @param json  JSON字符串
     * @param clazz 反序列化类型
     * @param <T>   类型
     * @return 类型
     */
    <T> List<T> deserializeList(String json, Class<T> clazz);

    /**
     * 反序列化成list
     *
     * @param reader JSON Reader
     * @param clazz  反序列化类型
     * @param <T>    类型
     * @return 类型
     */
    <T> List<T> deserializeList(Reader reader, Class<T> clazz);

    /**
     * 序列化成JSON字符串
     *
     * @param value 对象
     * @return JSON字符串
     */
    String serialize(Object value);

}
