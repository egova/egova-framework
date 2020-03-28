package com.egova.cloud.feign;

import com.egova.cloud.api.ApiExceptionHandler;
import com.egova.rest.ResponseResult;
import com.egova.utils.ResponseResultUtils;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * feign调用异常
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@Order(100)
@RestControllerAdvice
public class FeignExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(FeignException.class)
    public ResponseResult<Void> handleFeignException(FeignException e) {
        String msg = e.getMessage();
        LOG.warn("handle FeignException: {}", msg);
        return ResponseResultUtils.error(msg);
    }

}
