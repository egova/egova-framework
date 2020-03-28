package com.egova.cloud.api;

/**
 * API异常
 *
 * @author 奔波儿灞
 * @see ApiExceptionHandler
 * @since 1.0
 */
public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
