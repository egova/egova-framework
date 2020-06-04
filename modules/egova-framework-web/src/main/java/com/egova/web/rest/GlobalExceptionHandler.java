package com.egova.web.rest;

import com.egova.exception.ApiException;
import com.egova.web.rest.ResponseResult;
import com.egova.web.rest.ResponseResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

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

    @ExceptionHandler(SQLException.class)
    public ResponseResult<Void> handleSQLException(SQLException e) {
        String msg = e.getMessage();
        log.error("handle SQLException: {}", msg);
        // todo 是否写库
        return ResponseResults.error("数据库异常");
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseResult<Void> handleDataAccessException(DataAccessException e) {
        String msg = e.getMessage();
        log.error("handle DataAccessException: {}", msg);
        // todo 是否写库
        return ResponseResults.error("数据库异常");
    }

}