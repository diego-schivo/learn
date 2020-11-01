package com.backflipsource.form;

import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeStream;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public class Select extends Control {

	protected static String PAGE = "select.jsp";

	public Select(Field field) {
		super(field);
	}

	@Override
	public String getPage() {
		return PAGE;
	}

	public boolean isMultiple() {
		return Collection.class.isAssignableFrom(field.getType());
	}

	public String[] getOptions() {
		Options options = field.getAnnotation(Options.class);
		if (options != null) {
			return options.value();
		}
		Class<?> type = isMultiple()
				? (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]
				: field.getType();
		List<?> list = safeGet(() -> (List) type.getDeclaredField("list").get(null));
		return safeStream(list).map(item -> safeGet(() -> item.getClass().getMethod("getName").invoke(item)))
				.toArray(String[]::new);
	}

	@Target(FIELD)
	@Retention(RUNTIME)
	public @interface Options {

		String[] value();
	}
}
