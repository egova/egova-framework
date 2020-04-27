package com.egova.exception;

import lombok.EqualsAndHashCode;

/**
 * @description: 架构异常
 *
 * @author chendb
 * @date 2020-04-14 14:01:03
 */
@EqualsAndHashCode(callSuper = true)
public class FrameworkException  extends RuntimeException {

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