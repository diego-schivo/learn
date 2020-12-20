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
import java.util.List;

public class Select extends AbstractControl {

	public boolean isMultiple() {
		return ((Factory) factory).multiple;
	}

	public List<String> getOptions() {
		return ((Factory) factory).options;
	}

	@Target(FIELD)
	@Retention(RUNTIME)
	public @interface Options {

		String[] value();
	}

	@SuppressWarnings("rawtypes")
	public static class Factory extends AbstractFactory<Select> {

		protected boolean multiple;

		protected List<String> options;

		public Factory(Field field, View.Field annotation) {
			super(Select.class, field, annotation);

			multiple = Iterable.class.isAssignableFrom(field.getType());

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
		}
	}
}
