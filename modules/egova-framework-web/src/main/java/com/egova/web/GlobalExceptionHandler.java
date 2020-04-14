package com.egova.web;


import com.egova.exception.StatefulException;
import com.egova.rest.ResponseResult;
import com.egova.rest.ResponseResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * {@link StatefulException}异常处理，优先级为100
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@Slf4j
@Order(100)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StatefulException.class)
    public ResponseResult<Void> handleApiException(StatefulException e) {
        String msg = e.getMessage();
        log.warn("handle ApiException: {}", msg);
        return ResponseResults.error(msg);
    }
}
