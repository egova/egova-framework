package com.egova.utils;

import com.egova.rest.ResponseResult;
import com.egova.rest.ResponseResultFactory;
import com.flagwind.application.Application;

/**
 * 响应结果工具类
 */
public class ResponseResultUtils {

    private static ResponseResultFactory getFactory() {
        ResponseResultFactory factory = Application.resolve(ResponseResultFactory.class);
        return factory;
    }

    public static <T> ResponseResult<T> success(T t, String message) {
        return getFactory().success(t, message);
    }

    public static <T> ResponseResult<T> success(T t) {
        return getFactory().success(t, null);
    }

    public static  <T> ResponseResult<T> error(String message) {
        return getFactory().error(message);
    }
}
