package com.egova.json.databind;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 名称注解
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
