package com.backflipsource.servlet;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.backflipsource.Converter;
import com.backflipsource.servlet.StringConverter.ForString;

@Target(TYPE)
@Retention(RUNTIME)
@Repeatable(Views.class)
public @interface View {

	Class<?>[] value() default List.class;

	String uri() default "";

	String page() default "";

	Class<? extends RequestHandler.Provider> handlerProvider() default RequestHandler.Provider.class;

	@Target(FIELD)
	@Retention(RUNTIME)
	@interface Fields {

		Field[] value();
	}

	@Target(FIELD)
	@Retention(RUNTIME)
	@Repeatable(Fields.class)
	@SuppressWarnings("rawtypes")
	@interface Field {

		boolean identifier() default false;

		Class<?>[] view() default {};

		Class<? extends StringConverter<?>> converter() default ForString.class;

		Class<? extends Converter> converter2() default Converter.class;
	}

	interface List {
	}

	interface Show {
	}

	interface Edit {
	}

	Class<?>[] ALL = { List.class, Show.class, Edit.class };
}
