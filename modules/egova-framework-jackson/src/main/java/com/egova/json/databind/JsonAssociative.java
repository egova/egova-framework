package com.egova.json.databind;

import java.lang.annotation.*;

/**
 * 联想注解
 */
@Repeatable(JsonAssociatives.class)
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonAssociative {


    String name();

    String source();

    String extras() default "";
}
