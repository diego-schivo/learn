package com.backflipsource.form;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.backflipsource.form.StringConverter.Identity;

@Target(FIELD)
@Retention(RUNTIME)
public @interface FormField {

	Class<? extends Control> control() default Input.class;

	Class<? extends StringConverter<?>> converter() default Identity.class;
}
