package com.egova.web.config;

import com.egova.exception.ApiException;
import com.egova.rest.ResponseResult;
import com.egova.rest.ResponseResults;
import com.egova.web.annotation.Api;
import com.egova.web.config.mvc.MvcConfig;
import com.egova.web.config.mvc.ParameterFilter;
import com.egova.web.config.websocket.WebSocketConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collections;

/**
 * @author chendb
 * @description: web 自动配置类
 * @date 2020-04-18 09:35:30
 */

@ImportAutoConfiguration({MvcConfig.class, WebSocketConfig.class})
public class WebAutoConfiguration {


    @Bean
    public FilterRegistrationBean<ParameterFilter> parameterFilter() {
        FilterRegistrationBean<ParameterFilter> filterRegistrationBean = new FilterRegistrationBean<>(new ParameterFilter());
        filterRegistrationBean.setUrlPatterns(Collections.singleton("/*"));
        filterRegistrationBean.setOrder(100);
        return filterRegistrationBean;
    }

    /**
     * {@link ApiException}异常处理，优先级为100
     *
     * @author 奔波儿灞
     * @since 1.0
     */
    @Slf4j
    @Order(100)
    @RestControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(ApiException.class)
        public ResponseResult<Void> handleApiException(ApiException e) {
            String msg = e.getMessage();
            log.warn("handle ApiException: {}", msg);
            return ResponseResults.error(msg);
        }
    }


    /**
     * 返回body处理，包装成 {@link ResponseResult} 类型
     * 如果是 {@link ResponseEntity} 或者已经是 {@link ResponseResult} ，则不包装
     *
     * @author 奔波儿灞
     * @since 1.0
     */
    @RestControllerAdvice(annotations = RestController.class)
    public static class ResponseResultAdvice implements ResponseBodyAdvice<Object> {

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
            return ResponseResults.success(body);
        }

    }

}
