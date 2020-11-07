package com.backflipsource.form;

import static com.backflipsource.Helpers.getGetter;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public abstract class Control {

	protected String name;

	protected List<String> values;

	protected Control(String name, List<String> values) {
		this.name = name;
		this.values = values;
	}

	public String getName() {
		return name;
	}

	public List<String> getValues() {
		return values;
	}

	public abstract String getPage();

	public String getValue() {
		return getValues().get(0);
	}

	@SuppressWarnings("rawtypes")
	public static abstract class Factory {

		protected Field field;

		protected StringConverter converter;

		public Factory(Field field, StringConverter converter) {
			this.field = field;
			this.converter = converter;
		}

		public abstract Control control(Object object);

		public String name() {
			return field.getName();
		}

		@SuppressWarnings("unchecked")
		public List<String> values(Object object) {
			Object value = unsafeGet(() -> getGetter(field).invoke(object));
			Collection<?> collection = (value instanceof Collection) ? (Collection<?>) value
					: ((value != null) ? singleton(value) : null);
			return safeStream(collection).map(converter::convertToString).collect(toList());
		}
	}
}
