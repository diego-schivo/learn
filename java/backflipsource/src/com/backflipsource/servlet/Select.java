package com.backflipsource.servlet;

import static com.backflipsource.Helpers.getArgumentTypes;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeStream;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class Select extends Control {

	protected static String PAGE = "select.jsp";

	protected boolean multiple;

	protected List<String> options;

	public Select(String name, List<String> values, boolean multiple, List<String> options) {
		super(name, values);
		this.multiple = multiple;
		this.options = options;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public List<String> getOptions() {
		return options;
	}

	@Override
	public String getPage() {
		return PAGE;
	}

	@Target(FIELD)
	@Retention(RUNTIME)
	public @interface Options {

		String[] value();
	}

	@SuppressWarnings("rawtypes")
	public static class Factory extends Control.Factory {

		public Factory(Field field, StringConverter converter) {
			super(field, converter);
		}

		@Override
		public Control control(Object object) {
			boolean multiple = Collection.class.isAssignableFrom(field.getType());

			List<String> options;
			Options options2 = field.getAnnotation(Options.class);
			if (options2 != null) {
				options = safeList(options2.value());
			} else {
				Class<?> type = multiple ? (Class<?>) getArgumentTypes(field.getGenericType()).get(0) : field.getType();
				List<?> list = safeGet(() -> (List) type.getDeclaredField("list").get(null));
				options = safeStream(list)
						.map(item -> safeGet(() -> (String) item.getClass().getMethod("getName").invoke(item)))
						.collect(toList());
			}

			return new Select(name(), values(object), multiple, options);
		}
	}
}
