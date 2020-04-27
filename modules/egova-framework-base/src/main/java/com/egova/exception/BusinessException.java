package com.egova.exception;


import lombok.EqualsAndHashCode;

/**
 * 业务运行时异常
 * @author chendb
 * @date 2016年12月8日 下午11:23:51
 */
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {


    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

}