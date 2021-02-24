package com.egova.web.rest;

import com.egova.web.annotation.Api;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author chendb
 * @description:
 * @date 2020-04-25 08:50:17
 */
@RestControllerAdvice(annotations = RestController.class)
public class ResponseResultAdvice implements ResponseBodyAdvice<Object> {

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // controller类增加了@Api注解或者方法增加了@Api注解才包装返回值
        return methodParameter.getDeclaringClass().isAnnotationPresent(Api.class) || methodParameter.getMethodAnnotation(Api.class) != null;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter,
                                  MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // ResponseEntity、OperateResult，则不包装
        if (body instanceof ResponseEntity || body instanceof ResponseResult) {
            return body;
        }

        if (body instanceof WrappedData) {
            return ResponseResults.unwrap((WrappedData) body);
        }

        // 其余统一包装成OperateResult
        return ResponseResults.success(body);
    }

}