package com.egova.rest;


/**
 * 响应结果工厂，各部门需要实现该接口来创建自己的结果包装类
 */
public interface ResponseResultFactory {

    <T> ResponseResult<T> create();

    <T> ResponseResult<T> success(T t, String message);

    <T> ResponseResult<T> error(String message);
}
