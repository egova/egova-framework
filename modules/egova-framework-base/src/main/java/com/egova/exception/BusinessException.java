package com.egova.exception;


import lombok.Data;

/**
 * 业务运行时异常
 *
 * @author chendb
 * @date 2016年12月8日 下午11:23:51
 */
@Data
public class BusinessException extends StatefulException {

    private static final long serialVersionUID = -6447910316357429620L;

    public BusinessException(int status, String message) {
        super(status, message);
    }

    public BusinessException(int status, String message, Throwable cause) {
        super(status, message, cause);
    }

    public BusinessException(int status, Throwable cause) {
        super(status, cause);
    }

}
