package com.backflipsource.entity.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.backflipsource.entity.annotation.Render.Repeat;
import com.backflipsource.servlet.Control;

@Target({ FIELD, TYPE })
@Retention(RUNTIME)
@Repeatable(Repeat.class)
public @interface Render {

	Class<?>[] view() default {};

	@SuppressWarnings("rawtypes")
	Class<? extends Control.Factory> controlFactory() default Control.Factory.class;

	String controlPage() default "";

	Class<?> presentation() default Object.class;

	@Target({ FIELD, TYPE })
	@Retention(RUNTIME)
	@interface Repeat {

		Render[] value();
	}
}
