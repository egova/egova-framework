package com.egova.exception;


/**
 * 业务运行时异常
 * @author chendb
 * @date 2016年12月8日 下午11:23:51
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -6447910316357429620L;

	public BusinessException() {
        super();
    }

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
