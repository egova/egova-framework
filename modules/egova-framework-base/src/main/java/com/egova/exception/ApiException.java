package com.egova.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 带有状态码的异常
 *
 * @author chendb
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 7419115665474379007L;

    /**
     * 异常状态码
     */
    private long code;

    public ApiException() {
    }

    public ApiException(long code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(long code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public ApiException(long code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

}