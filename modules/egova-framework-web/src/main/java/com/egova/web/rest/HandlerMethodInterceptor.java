package com.egova.web.rest;

import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;

/**
 * api请求方法注册拦截器
 */
public interface HandlerMethodInterceptor
{
	boolean preHandle(Object handler, Method method, RequestMappingInfo mapping);

	void postHandle(Object handler, Method method, RequestMappingInfo mapping);
}
