package com.egova.json;

import com.egova.exception.FrameworkException;
import com.egova.json.databind.ObjectMappingCustomer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chendb
 * @description: 架构json操作类实现
 * @date 2020-04-14 15:30:12
 */
public class JacksonJsonMapping implements JsonMapping {

    private ObjectMappingCustomer objectMappingCustomer;

    public JacksonJsonMapping(ObjectMappingCustomer objectMappingCustomer) {
        this.objectMappingCustomer = objectMappingCustomer;
    }

    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMappingCustomer.readValue(json, clazz);
        } catch (IOException e) {
            throw new FrameworkException(clazz + "反序列化异常", e);
        }
    }

    @Override
    public <T> T deserialize(Reader reader, Class<T> clazz) {
        try {
            return objectMappingCustomer.readValue(reader, clazz);
        } catch (IOException e) {
            throw new FrameworkException(clazz + "反序列化异常", e);
        }
    }

    @Override
    public <T> List<T> deserializeList(String json, Class<T> clazz) {
        try {
            CollectionType collectionType = objectMappingCustomer.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return objectMappingCustomer.readValue(json, collectionType);
        } catch (IOException e) {
            throw new FrameworkException(clazz + "元素数组反序列化异常", e);
        }
    }

    @Override
    public <T> List<T> deserializeList(Reader reader, Class<T> clazz) {
        try {
            CollectionType collectionType = objectMappingCustomer.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return objectMappingCustomer.readValue(reader, collectionType);
        } catch (IOException e) {
            throw new FrameworkException(clazz + "元素数组反序列化异常", e);
        }
    }

    @Override
    public String serialize(Object value) {
        try {
            return objectMappingCustomer.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new FrameworkException("对象序列化异常", e);
        }
    }

}
