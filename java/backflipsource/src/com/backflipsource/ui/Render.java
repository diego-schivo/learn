package com.backflipsource.ui;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.backflipsource.Control;
import com.backflipsource.ui.Render.Repeated;
import com.backflipsource.ui.spec.EntityUI.Mode;

@Target({ FIELD, TYPE })
@Retention(RUNTIME)
@Repeatable(Repeated.class)
public @interface Render {

	Class<? extends Mode>[] mode() default {};

	@SuppressWarnings("rawtypes")
	Class<? extends Control.Factory> controlFactory() default Control.Factory.class;

	String controlPage() default "";

	Class<?> entity() default Object.class;

	String heading() default "";

	@Target({ FIELD, TYPE })
	@Retention(RUNTIME)
	@interface Repeated {

		Render[] value();
	}
}
