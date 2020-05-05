package com.egova.web.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RequestDecorating
{
//	@AliasFor("state")
	String value();

//	@AliasFor("value")
//	String state() default "";

	String name() default "state";

}
