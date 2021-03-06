package com.egova.web.annotation;

import java.lang.annotation.*;

/**
 * 如果 {@link org.springframework.web.bind.annotation.RestController} 的方法增加该注解，会将返回值包装成{@link com.egova.web.rest.ResponseResult}
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Api {
}
