package com.egova.json;

import com.egova.exception.FrameworkException;
import com.egova.json.databind.ObjectMappingCustomer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chendb
 * @description: 架构json操作类实现
 * @date 2020-04-14 15:30:12
 */
public class JacksonJsonMapping implements JsonMapping {

    private ObjectMappingCustomer enableAssociativeObjectMapping;

    private ObjectMappingCustomer disableAssociativeObjectMapping;

    public JacksonJsonMapping(ObjectMappingCustomer enableAssociativeObjectMapping, ObjectMappingCustomer disableAssociativeObjectMapping) {
        this.enableAssociativeObjectMapping = enableAssociativeObjectMapping;
        this.disableAssociativeObjectMapping = disableAssociativeObjectMapping;
    }

    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        try {
            return enableAssociativeObjectMapping.readValue(json, clazz);
        } catch (IOException e) {
            throw new FrameworkException(clazz + "反序列化异常", e);
        }
    }

    @Override
    public <T> T deserialize(String json, Type type) {
        try {
            JavaType javaType = enableAssociativeObjectMapping.getTypeFactory().constructType(type);
            return enableAssociativeObjectMapping.readValue(json, javaType);
        } catch (IOException e) {
            throw new FrameworkException(type + "反序列化异常", e);
        }
    }

    @Override
    public <T> T deserialize(Reader reader, Class<T> clazz) {
        try {
            return enableAssociativeObjectMapping.readValue(reader, clazz);
        } catch (IOException e) {
            throw new FrameworkException(clazz + "反序列化异常", e);
        }
    }

    @Override
    public <T> T deserialize(Reader reader, Type type) {
        try {
            JavaType javaType = enableAssociativeObjectMapping.getTypeFactory().constructType(type);
            return enableAssociativeObjectMapping.readValue(reader, javaType);
        } catch (IOException e) {
            throw new FrameworkException(type + "反序列化异常", e);
        }
    }

    @Override
    public <T> List<T> deserializeList(String json, Class<T> clazz) {
        try {
            CollectionType collectionType = enableAssociativeObjectMapping.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return enableAssociativeObjectMapping.readValue(json, collectionType);
        } catch (IOException e) {
            throw new FrameworkException(clazz + "元素数组反序列化异常", e);
        }
    }


    public <T> List<T> deserializeList(String json, Type type) {
        try {
            JavaType javaType = enableAssociativeObjectMapping.getTypeFactory().constructType(type);
            CollectionType collectionType = enableAssociativeObjectMapping.getTypeFactory().constructCollectionType(ArrayList.class, javaType);
            return enableAssociativeObjectMapping.readValue(json, collectionType);
        } catch (IOException e) {
            throw new FrameworkException(type + "元素数组反序列化异常", e);
        }
    }


    @Override
    public <T> List<T> deserializeList(Reader reader, Class<T> clazz) {
        try {
            CollectionType collectionType = enableAssociativeObjectMapping.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return enableAssociativeObjectMapping.readValue(reader, collectionType);
        } catch (IOException e) {
            throw new FrameworkException(clazz + "元素数组反序列化异常", e);
        }
    }

    @Override
    public <T> List<T> deserializeList(Reader reader, Type type) {
        try {
            JavaType javaType = enableAssociativeObjectMapping.getTypeFactory().constructType(type);
            CollectionType collectionType = enableAssociativeObjectMapping.getTypeFactory().constructCollectionType(ArrayList.class, javaType);
            return enableAssociativeObjectMapping.readValue(reader, collectionType);
        } catch (IOException e) {
            throw new FrameworkException(type + "元素数组反序列化异常", e);
        }
    }

    @Override
    public String serialize(Object value) {
        return this.serialize(value, true);
    }

    @Override
    public String serialize(Object value, boolean enableAssociative) {
        try {
            if (enableAssociative) {
                return enableAssociativeObjectMapping.writeValueAsString(value);
            } else {
                return disableAssociativeObjectMapping.writeValueAsString(value);
            }
        } catch (JsonProcessingException e) {
            throw new FrameworkException("对象序列化异常", e);
        }
    }

}
