package com.egova.cloud.feign;

import feign.MethodMetadata;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import com.egova.web.annotation.RequestDecorating;
import org.springframework.core.convert.ConversionService;

/**
 * @author chendb
 * @description:
 * @date 2020-04-27 15:35:32
 */
public class DefaultSpringMvcContract extends SpringMvcContract {

    public DefaultSpringMvcContract(List<AnnotatedParameterProcessor> annotatedParameterProcessors, ConversionService conversionService) {
        super(annotatedParameterProcessors, conversionService);
    }

    @Override
    protected void processAnnotationOnMethod(MethodMetadata data, Annotation methodAnnotation, Method method) {
        super.processAnnotationOnMethod(data, methodAnnotation, method);
        if (RequestDecorating.class.isInstance(methodAnnotation) || methodAnnotation.annotationType().isAnnotationPresent(RequestDecorating.class)) {
            RequestDecorating methodMapping = (RequestDecorating) AnnotatedElementUtils.findMergedAnnotation(method, RequestDecorating.class);
            data.template().query("@state",methodMapping.state());
        }
    }
}
