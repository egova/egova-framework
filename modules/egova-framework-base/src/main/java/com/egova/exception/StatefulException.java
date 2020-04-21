package com.egova.exception;

import lombok.Data;

/**
 * 带有状态码的异常
 *
 * @author chendb
 */
@Data
public class StatefulException extends RuntimeException {
    private static final long serialVersionUID = 7419115665474379007L;

    /**
     * 异常状态码
     */
    private int status;

    public StatefulException() {
    }

    public StatefulException(int status, String message) {
        super(message);
        this.status = status;
    }

    public StatefulException(int status, Throwable throwable) {
        super(throwable);
        this.status = status;
    }

    public StatefulException(int status, String message, Throwable throwable) {
        super(message, throwable);
        this.status = status;
    }

}
