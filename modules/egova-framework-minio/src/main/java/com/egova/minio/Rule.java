package com.egova.minio;

import java.util.List;

/**
 * 算法规则
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public interface Rule<T> {

    /**
     * 从列表中选择一个具体的实例
     *
     * @param list 列表
     * @return 具体的实例
     */
    T choose(List<T> list);

}
