package com.egova.associative;

import java.lang.annotation.*;

/**
 * 联想注解
 */


@Repeatable(Associatives.class)
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Associative {


    String name();


    String provider();


    String extras() default "";
}
