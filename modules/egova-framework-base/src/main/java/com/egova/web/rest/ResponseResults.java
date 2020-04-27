package com.egova.web.rest;

import com.flagwind.application.Application;

import java.io.Reader;

/**
 * 响应结果工具类
 *
 * @author chenabao
 */
public class ResponseResults {

    /**
     * 获取工厂类
     *
     * @return
     */
    private static ResponseResultFactory getFactory() {
        return Application.resolve(ResponseResultFactory.class);
    }

    /**
     * 执行委托的方法并把返回的结果进行包装
     *
     * @param supplier 委托的方法
     * @param <T>      泛型约束
     * @return 包装结果
     */
    public static <T> ResponseResult<T> execute(java.util.function.Supplier<T> supplier) {
        try {
            return success(supplier.get());
        } catch (Exception ex) {
            return error(ex.getMessage());
        }
    }

    /**
     * 包装成功的响应结果
     *
     * @param t       响应结果
     * @param message 响应消息
     * @param <T>     泛型约束
     * @return 包装结果
     */
    public static <T> ResponseResult<T> success(T t, String message) {
        return getFactory().success(t, message);
    }

    /**
     * 包装成功的响应结果
     *
     * @param t   响应结果
     * @param <T> 泛型约束
     * @return 包装结果
     */
    public static <T> ResponseResult<T> success(T t) {
        return getFactory().success(t, null);
    }

    /**
     * 包装异常的响应结果
     *
     * @param message 响应消息
     * @param <T>     泛型约束
     * @return 包装结果
     */
    public static <T> ResponseResult<T> error(String message) {
        return getFactory().error(message);
    }

    /**
     * 将 reader 反序列化为 ResponseResult 实现类
     *
     * @param reader JSON reader
     * @param <T>    泛型约束
     * @return ResponseResult 实现类
     */
    public static <T> ResponseResult<T> deserialize(Reader reader) {
        return getFactory().deserialize(reader);
    }

    /**
     * 将 reader 反序列化为 ResponseResult 实现类
     *
     * @param json JSON 字符串
     * @param <T>  泛型约束
     * @return ResponseResult 实现类
     */
    public static <T> ResponseResult<T> deserialize(String json) {
        return getFactory().deserialize(json);
    }
}
