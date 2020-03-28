package com.egova.cloud.api;


import com.egova.rest.ResponseResult;
import com.egova.utils.ResponseResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * {@link ApiException}异常处理，优先级为100
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@Order(100)
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseResult<Void> handleApiException(ApiException e) {
        String msg = e.getMessage();
        LOG.warn("handle ApiException: {}", msg);
        return ResponseResultUtils.error(msg);
    }

}
