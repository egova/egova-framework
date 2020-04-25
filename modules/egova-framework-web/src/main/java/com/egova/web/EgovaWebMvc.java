package com.egova.web;

import com.egova.web.config.mvc.MvcConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author chendb
 * @description:
 * @date 2020-04-25 10:13:21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(MvcConfig.class)
public @interface EgovaWebMvc {
}
