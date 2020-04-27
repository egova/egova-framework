package com.egova.cache;

import com.egova.utils.ReflectExtraUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 缓存key的生成器
 */
public class CacheKeyGenerator implements KeyGenerator {

	public Class<?> getProxyClass(Object target) {
		Class clazz;
		if (target instanceof Proxy) {
			InvocationHandler invocationHandler = Proxy.getInvocationHandler(target);
			if (invocationHandler.getClass().toString().contains("MapperProxy")) {
				clazz = (Class) ReflectExtraUtils.getFieldValue(invocationHandler, "mapperInterface");
				return clazz;
			} else if (invocationHandler.getClass().toString().contains("JdkDynamicAopProxy")) {
				ProxyFactory advised = (ProxyFactory) ReflectExtraUtils.getFieldValue(invocationHandler, "advised");
				Class<?>[] objectList = advised.getProxiedInterfaces();
				if (objectList.length > 0) {
					clazz = objectList[0];
					return clazz;
				}

			}
		}
		return target.getClass();
	}

	@Override
	public Object generate(Object target, Method method, Object... params) {
		StringBuilder sb = new StringBuilder();
		Class clazz = getProxyClass(target);
		if (clazz.getSimpleName().endsWith("ServiceImpl")
				|| clazz.getSimpleName().contains("ServiceImpl$")) {
			sb.append("Service");
		} else if (clazz.getSimpleName().endsWith("Repository")
				|| clazz.getSimpleName().contains("Repository$")
				|| clazz.getSimpleName().endsWith("AbstractRepositoryBase")
				|| clazz.getSimpleName().contains("AbstractRepositoryBase$")) {
			sb.append("Repository");
		} else {
			sb.append(clazz.getSimpleName());
		}
		sb.append(":");
		if (method.getName().startsWith("findBy")) {
			sb.append(method.getName().substring(6));
		} else if (method.getName().startsWith("getBy")) {
			sb.append(method.getName().substring(5));
		} else if (method.getName().equalsIgnoreCase("getDataFromCache")) {
			sb.append("all");
		} else if (method.getName().equalsIgnoreCase("getManyFromCache")) {
			sb.append("key-value");
		} else {
			sb.append(method.getName());
		}
		for (Object obj : params) {
			if (obj != null) {
				sb.append(":");
				sb.append(obj.toString());
			}
		}
		return sb.toString();
	}
}
