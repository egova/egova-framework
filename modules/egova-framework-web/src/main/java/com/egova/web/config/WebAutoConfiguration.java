package com.egova.web.config;

import com.egova.web.config.mvc.MvcConfig;
import com.egova.web.config.mvc.ParameterFilterConfiguration;
import com.egova.web.config.websocket.WebSocketConfig;
import org.springframework.context.annotation.Import;

/**
 * @author chendb
 * @description: web 自动配置类
 * @date 2020-04-18 09:35:30
 */

@Import({MvcConfig.class, WebSocketConfig.class, ParameterFilterConfiguration.class})
public class WebAutoConfiguration {

//
//    @Bean
//    public FilterRegistrationBean<ParameterFilter> parameterFilter() {
//        FilterRegistrationBean<ParameterFilter> filterRegistrationBean = new FilterRegistrationBean<>(new ParameterFilter());
//        filterRegistrationBean.setUrlPatterns(Collections.singleton("/*"));
//        filterRegistrationBean.setOrder(100);
//        return filterRegistrationBean;
//    }
//
//    /**
//     * {@link ApiException}异常处理，优先级为100
//     *
//     * @author 奔波儿灞
//     * @since 1.0
//     */
//    @Slf4j
//    @Order(100)
//    @RestControllerAdvice
//    public static class GlobalExceptionHandler {
//
//        @ExceptionHandler(ApiException.class)
//        public ResponseResult<Void> handleApiException(ApiException e) {
//            String msg = e.getMessage();
//            log.warn("handle ApiException: {}", msg);
//            return ResponseResults.error(msg);
//        }
//    }
//
//
//    /**
//     * 返回body处理，包装成 {@link ResponseResult} 类型
//     * 如果是 {@link ResponseEntity} 或者已经是 {@link ResponseResult} ，则不包装
//     *
//     * @author 奔波儿灞
//     * @since 1.0
//     */
//    @RestControllerAdvice(annotations = RestController.class)
//    public static class ResponseResultAdvice implements ResponseBodyAdvice<Object> {
//
//        @Override
//        @SuppressWarnings("NullableProblems")
//        public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
//            // 方法增加了@Api注解才包装返回值
//            return methodParameter.getMethodAnnotation(Api.class) != null;
//        }
//
//        @Override
//        @SuppressWarnings("NullableProblems")
//        public Object beforeBodyWrite(Object body, MethodParameter methodParameter,
//                                      MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
//                                      ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
//            // ResponseEntity、OperateResult，则不包装
//            if (body instanceof ResponseEntity || body instanceof ResponseResult) {
//                return body;
//            }
//            // 其余统一包装成OperateResult
//            return ResponseResults.success(body);
//        }
//
//    }

}
