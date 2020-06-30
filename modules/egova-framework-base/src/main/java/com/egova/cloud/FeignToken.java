package com.egova.cloud;

import java.lang.annotation.*;

/**
 * @author chendb
 * @description: token认证处理
 * @date 2020-06-30 09:42:24
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeignToken {


    Obtain obtain() default Obtain.parent;

    enum Obtain {
        parent, client, local
    }
}
