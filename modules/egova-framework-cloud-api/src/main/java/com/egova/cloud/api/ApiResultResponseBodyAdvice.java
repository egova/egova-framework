package com.egova.cloud.api;


import com.egova.rest.ResponseResult;
import com.egova.utils.ResponseResultUtils;
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
 * 返回body处理，包装成 {@link ResponseResult} 类型
 * 如果是 {@link ResponseEntity} 或者已经是 {@link ResponseResult} ，则不包装
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@RestControllerAdvice(annotations = RestController.class)
public class ApiResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 方法增加了@Api注解才包装返回值
        return methodParameter.getMethodAnnotation(Api.class) != null;
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
        // 其余统一包装成OperateResult
        return ResponseResultUtils.success(body);
    }

}
