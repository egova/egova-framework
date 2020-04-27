package com.egova.web.rest;

/**
 * 响应结果接口定义，考虑到各部分有各自的结果包装类，所以只定义了接口，各部分需要在各自结果包装类上实现这一接口
 *
 * @param <T> 泛型结果类型
 */
public interface ResponseResult<T> {

    /**
     * 功能描述：是否有异常
     *
     * @return boolean
     */
    boolean getHasError();

    /**
     * 功能描述：获取响应结果
     *
     * @return T
     */
    T getResult();

    /**
     * 功能描述：获取响应的消息
     *
     * @return java.lang.String
     */
    String getMessage();

}
