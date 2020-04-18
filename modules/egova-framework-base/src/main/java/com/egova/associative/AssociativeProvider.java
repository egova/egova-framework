package com.egova.associative;

/**
 * @author chendb
 * @description: 实体联想提供器
 * @date 2020-04-17 08:44:51
 */
public interface AssociativeProvider {

    /**
     * 根据关键字联想到对象
     *
     * @param key 联想关键字
     * @return 联想到的对象
     */
    Object associate(Object key);
}
