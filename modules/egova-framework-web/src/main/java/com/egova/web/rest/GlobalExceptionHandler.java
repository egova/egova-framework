package com.egova.web.rest;

import com.egova.exception.ApiException;
import com.egova.rest.ResponseResult;
import com.egova.rest.ResponseResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author chendb
 * @description:
 * @date 2020-04-25 08:49:30
 */
@Slf4j
@Order(100)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseResult<Void> handleApiException(ApiException e) {
        String msg = e.getMessage();
        log.warn("handle ApiException: {}", msg);
        return ResponseResults.error(msg);
    }
}