package com.egova.json;

import java.io.Reader;
import java.util.List;

/**
 * @description: json操作接口定义
 *
 * @author chendb
 * @date 2020-04-14 14:48:17
 */
public interface JsonMapping {

    <T> T deserialize(String json, Class<T> clazz);

    <T> T deserialize(Reader reader, Class<T> clazz);

    <T> List<T> deserializeList(String json, Class<T> elementClass);

    String serialize(Object value);

}
