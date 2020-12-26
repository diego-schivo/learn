package com.backflipsource.ui;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.backflipsource.Converter;
import com.backflipsource.DynamicClass;
import com.backflipsource.RequestHandler;
import com.backflipsource.RequestMatcher;
import com.backflipsource.servlet.DefaultRequestHandlerProvider;
import com.backflipsource.servlet.StringConverter;
import com.backflipsource.servlet.StringConverter.ForString;

@Target(TYPE)
@Retention(RUNTIME)
public @interface Entity {

	String uri() default "";

	Class<? extends RequestHandler.Provider> handlerProvider() default DefaultRequestHandlerProvider.class;

	Class<? extends DynamicClass> descriptor() default DynamicClass.class;

	@Target(FIELD)
	@Retention(RUNTIME)
	@Repeatable(Field.Repeated.class)
	@SuppressWarnings("rawtypes")
	@interface Field {

		boolean identifier() default false;

		Class<?>[] mode() default {};

		Class<? extends StringConverter<?>> converter() default ForString.class;

		Class<? extends Converter> converter2() default Converter.class;

		@Target(FIELD)
		@Retention(RUNTIME)
		@interface Repeated {

			Field[] value();
		}
	}

	@Target(TYPE)
	@Retention(RUNTIME)
	public @interface Controller {

		Class<? extends RequestMatcher> matcher() default RequestMatcher.class;

		String regex() default "";

		String[] parameter() default {};

		int score() default 0;
	}
}
