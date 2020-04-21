package com.egova.exception;

/**
 * @author chendb
 * @description: 架构异常
 * @date 2020-04-14 14:01:03
 */
public class FrameworkException extends RuntimeException {

    public FrameworkException(String message) {
        super(message);
    }

    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameworkException(Throwable cause) {
        super(cause);
    }

}
