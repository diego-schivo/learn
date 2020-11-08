package com.backflipsource.servlet;

import static com.backflipsource.Helpers.emptyCollection;
import static com.backflipsource.Helpers.getGetter;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public abstract class Control {

	protected Object item;

	protected String name;

	protected List<String> values;

	protected String page;

	public Object getItem() {
		return item;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getValue() {
		if (emptyCollection(getValues())) {
			return null;
		}
		return getValues().get(0);
	}

	@SuppressWarnings("rawtypes")
	public static abstract class Factory<T extends Control> {

		protected Class<T> class1;

		protected Field field;

		protected StringConverter converter;

		protected Factory(Class<T> class1, Field field, StringConverter converter) {
			this.class1 = class1;
			this.field = field;
			this.converter = converter;
		}

		public T control(Object object) {
			T control = unsafeGet(() -> class1.getDeclaredConstructor().newInstance());
			control.setItem(object);
			control.setName(name());
			control.setValues(values(object));
			control.setPage(class1.getSimpleName().toLowerCase() + ".jsp");
			return control;
		}

		public String name() {
			return field.getName();
		}

		@SuppressWarnings("unchecked")
		protected List<String> values(Object object) {
			Object value = getFieldValue(object);
			Collection<?> collection = (value instanceof Collection) ? (Collection<?>) value
					: ((value != null) ? singleton(value) : null);
			return safeStream(collection).map(converter::convertToString).collect(toList());
		}

		protected Object getFieldValue(Object object) {
			return unsafeGet(() -> getGetter(field).invoke(object));
		}
	}
}
