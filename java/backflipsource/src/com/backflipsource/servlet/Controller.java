package com.backflipsource.servlet;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(TYPE)
@Retention(RUNTIME)
public @interface Controller {

	Class<? extends RequestMatcher> matcher() default RequestMatcher.class;

	String regex() default "";

	String[] parameter() default {};

	int score() default 0;
}
