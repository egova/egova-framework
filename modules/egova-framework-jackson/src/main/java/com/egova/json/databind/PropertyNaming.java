package com.egova.json.databind;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 名称映射注解，用于处理接受或写入的json结果与系统定义的不一样问题的处理
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD,  ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface PropertyNaming
{
	/**
	 * 名称
	 */
	String value();

	Access access() default Access.Read;

	enum Access
	{
		Read,
		Write,
		All
	}
}
