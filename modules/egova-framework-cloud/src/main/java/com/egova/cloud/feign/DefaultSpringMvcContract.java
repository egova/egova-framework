package com.egova.cloud.feign;

import com.egova.cloud.FeignToken;
import com.egova.web.annotation.RequestDecorating;
import feign.MethodMetadata;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.ConversionService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

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
        if (methodAnnotation instanceof RequestDecorating || methodAnnotation.annotationType().isAnnotationPresent(RequestDecorating.class)) {
            RequestDecorating methodMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestDecorating.class);
            assert methodMapping != null;
            data.template().query("@" + methodMapping.name(), methodMapping.value());
        }
        if (methodAnnotation instanceof FeignToken || methodAnnotation.annotationType().isAnnotationPresent(FeignToken.class)) {
            FeignToken feignToken = AnnotatedElementUtils.findMergedAnnotation(method, FeignToken.class);
            assert feignToken != null;
            data.template().query("$obtain", feignToken.obtain().name());
        }
    }
}
