package com.egova.minio;

/**
 * 负载均衡
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public interface LoadBalancer<T> {

    /**
     * 选择一个实例
     *
     * @return 实例
     */
    T get();

}
