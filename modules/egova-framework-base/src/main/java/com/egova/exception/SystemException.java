package com.egova.exception;

/**
 * 系统异常，主要用来处理系统的bug，无需向用户反馈具体信息
 * 
 * @author chendb
 */
public class SystemException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 7239207816552396911L;

    /**
     * 构造函数
     * 
     * @param message 消息
     * @param cause 异常
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     * 
     * @param message 消息
     */
    public SystemException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * 
     * @param cause 异常
     */
    public SystemException(Throwable cause) {
        super(cause);
    }

}
