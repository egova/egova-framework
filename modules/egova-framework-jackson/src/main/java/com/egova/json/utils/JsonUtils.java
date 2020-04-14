package com.egova.json.utils;

import com.egova.exception.FrameworkException;
import com.egova.json.databind.ObjectMappingCustomer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.flagwind.application.Application;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Json工具类
 *
 * @author chensoft
 * @date 2020-04-10 21:56
 */
public class JsonUtils {

	private static ObjectMappingCustomer ASSOCIATIVE_OBJECT_MAPPER;

	private static ObjectMappingCustomer CUSTOM_OBJECT_MAPPER;

	private static ObjectMappingCustomer getAssociativeMapping() {
		try {
			return Application.resolve(ObjectMappingCustomer.class);
		} catch (Exception ex) {
			if (ASSOCIATIVE_OBJECT_MAPPER == null) {
				ASSOCIATIVE_OBJECT_MAPPER = new ObjectMappingCustomer(true);
			}
			return ASSOCIATIVE_OBJECT_MAPPER;
		}
	}

	private static ObjectMappingCustomer getCustomMapping() {
		try {
			return Application.resolve("customObjectMapper");
		} catch (Exception ex) {
			if (CUSTOM_OBJECT_MAPPER == null) {
				CUSTOM_OBJECT_MAPPER = new ObjectMappingCustomer(false);
			}
			return CUSTOM_OBJECT_MAPPER;
		}
	}

	/**
	 * 功能描述：把json反序列化成指定类型的对象
	 *
	 * @return T
	 * @author chensoft
	 * @params [json:JSON的字符串值, clazz:对象类型]
	 * @date 2020-04-10 22:10
	 */
	public static <T> T deserializeByType(String json, TypeReference type) {
		try {
			T d = getAssociativeMapping().readValue(json, type);
			return d;
		} catch (Exception e) {
			throw new FrameworkException(type + "类型对象解析出错", e);
		}
	}

	/**
	 * 功能描述：把json反序列化成指定类型的对象
	 *
	 * @return T
	 * @author chensoft
	 * @params [json:JSON的字符串值, clazz:对象类型]
	 * @date 2020-04-10 22:10
	 */
	public static <T> T deserialize(String json, Class<?> clazz) {
		try {
			T d = (T) getAssociativeMapping().readValue(json, clazz);
			return d;
		} catch (IOException e) {
			throw new FrameworkException(clazz + "类型对象解析出错", e);
		}
	}

	/**
	 * 功能描述：把json反序列化成JsonNode数据结构
	 *
	 * @return com.fasterxml.jackson.databind.JsonNode
	 * @author chensoft
	 * @params [json:JSON的字符串值]
	 * @date 2020-04-10 22:08
	 */
	public static JsonNode readTree(String json) {
		try {
			return getAssociativeMapping().readTree(json);
		} catch (Exception ex) {
			throw new FrameworkException("解析json为JsonNode对象异常", ex);
		}
	}

	/**
	 * 功能描述：把json反序列化成List对象集合
	 *
	 * @return java.util.List<T> 对象集合
	 * @author chensoft
	 * @params [json:JSON的字符串值, clazz:元素类型]
	 * @date 2020-04-10 22:12
	 */
	public static <T> java.util.List<T> deserializeList(String json, Class<?> clazz) {
		try {
			if (StringUtils.isBlank(json)) {
				return new ArrayList<>();
			}
			JavaType type = constructCollectionType(getAssociativeMapping(), ArrayList.class, clazz);
			return getAssociativeMapping().readValue(json, type);
		} catch (IOException e) {
			throw new FrameworkException(clazz + "类型对象集合反序列化出错");
		}
	}

	/**
	 * 访方法序列的json对接会使用我们定义的序列化规则（如联想属性，字典类型）
	 *
	 * @param value 对象
	 * @return json
	 */
	public static String serialize(Object value) {
		try {
			if (value == null) {
				return null;
			}
			return getAssociativeMapping().writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new FrameworkException(value.getClass() + "类型对象解析出错");
		}
	}


	/**
	 * 该方法会使用ObjectMapper默认的序列化方法
	 *
	 * @param value 对象
	 * @return json
	 */
	public static String writeValue(Object value) {
		try {
			if (value == null) {
				return null;
			}
			return getCustomMapping().writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(value.getClass() + "类型对象解析出错", e);
		}
	}


	/**
	 * 使用ObjectMapper默认策略反序列化出对象
	 *
	 * @return T
	 * @author chensoft
	 * @params [json:字符串的Json值,clazz:Json的映射类型]
	 * @date 2020-04-10 22:04
	 */
	public static <T> T readValue(String json, Class<?> clazz) {
		try {
			T d = (T) getCustomMapping().readValue(json, clazz);
			return d;
		} catch (IOException e) {
			throw new RuntimeException(clazz + "类型对象解析出错", e);
		}
	}

	/**
	 * 构造集合类型
	 *
	 * @param mapper          mapper对象
	 * @param collectionClass 集合类型
	 * @param elementClasses  集合元素类型
	 * @return
	 */
	private static CollectionType constructCollectionType(ObjectMapper mapper, Class<? extends Collection> collectionClass, Class<?> elementClasses) {
		return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClasses);
	}

}
