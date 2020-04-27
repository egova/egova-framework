package com.egova.web.rest;

import com.egova.web.annotation.RequestDecorating;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;


public class DecoratingHandlerMapping extends RequestMappingHandlerMapping {


    private List<HandlerMethodInterceptor> handlerMethodInterceptors;

    public DecoratingHandlerMapping(List<HandlerMethodInterceptor> handlerMethodInterceptors) {
        this.handlerMethodInterceptors = handlerMethodInterceptors;
    }

    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {

        super.registerHandlerMethod(handler, method, mapping);

        if (handlerMethodInterceptors != null) {
            for (HandlerMethodInterceptor interceptor : handlerMethodInterceptors) {
                if (interceptor.preHandle(handler, method, mapping)) {
                    interceptor.postHandle(handler, method, mapping);
                }
            }
        }

    }

    @Override
    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
        return super.lookupHandlerMethod(lookupPath, request);
    }


    @Override
    protected RequestCondition<DecoratingRequestCondition> getCustomMethodCondition(Method method) {
        RequestDecorating sate = AnnotationUtils.findAnnotation(method, RequestDecorating.class);
        return createCondition(sate);
    }

    @Override
    protected RequestCondition<DecoratingRequestCondition> getCustomTypeCondition(Class<?> handlerType) {
        RequestDecorating state = AnnotationUtils.findAnnotation(handlerType, RequestDecorating.class);
        return createCondition(state);
    }

    private RequestCondition<DecoratingRequestCondition> createCondition(RequestDecorating decorating) {
        return decorating == null ? null : new DecoratingRequestCondition(decorating.state());
    }

}
