package com.egova.rest;

/**
 * 响应结果接口定义，考虑到各部分有各自的结果包装类，所以只定义了接口，各部分需要在各自结果包装类上实现这一接口
 * @param <T> 泛型结果类型
 */
public interface ResponseResult<T> {

    boolean getHasError();

    T getResult();

    String getMessage();

}
