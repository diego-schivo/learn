package com.backflipsource.servlet;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.backflipsource.servlet.StringConverter.ForString;

@Target(TYPE)
@Retention(RUNTIME)
@Repeatable(Views.class)
public @interface View {

	Class<?>[] value() default List.class;

	String uri() default "";

	String page() default "";

	@Target(FIELD)
	@Retention(RUNTIME)
	@interface Fields {

		Field[] value();
	}

	@Target(FIELD)
	@Retention(RUNTIME)
	@Repeatable(Fields.class)
	@interface Field {

		boolean identifier() default false;

		Class<?>[] view() default {};

		Class<? extends Control.Factory> controlFactory() default Control.Factory.class;

		Class<? extends StringConverter<?>> converter() default ForString.class;
	}

	interface List {
	}

	interface Show {
	}

	interface Edit {
	}
}
