package com.backflipsource.servlet;

import static com.backflipsource.Helpers.emptyCollection;
import static com.backflipsource.Helpers.getArgumentTypes;
import static com.backflipsource.Helpers.getGetter;
import static com.backflipsource.Helpers.listGet;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.servlet.EntityContextListener.getViews;
import static java.util.Collections.singleton;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.backflipsource.servlet.StringConverter.ForString;

public abstract class Control {

	private static Logger logger = getLogger(Control.class.getName());

	static {
		logger.setLevel(Level.ALL);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

	protected EntityView entityView;

	protected Object item;

	protected String name;

	protected List<String> values;

	protected String page;

	public EntityView getEntityView() {
		return entityView;
	}

	public Object getItem() {
		return item;
	}

	public String getName() {
		return name;
	}

	public List<String> getValues() {
		return values;
	}

	public String getPage() {
		return page;
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

		protected View.Field annotation;

		protected Factory(Class<T> class1, Field field, View.Field annotation) {
			this.class1 = class1;
			this.field = field;
			this.annotation = annotation;
		}

		public T control(Object object) {
			T control = unsafeGet(() -> class1.getDeclaredConstructor().newInstance());
			Class<?> type = (Class<?>) listGet(getArgumentTypes(field.getGenericType()), 0);
			control.entityView = (type != null) ? getViews().get(type.getName()) : null;
			control.item = object;
			control.name = name();
			control.values = values(object);
			control.page = nonEmptyString(annotation.controlPage(), class1.getSimpleName().toLowerCase() + ".jsp");
			return control;
		}

		public String name() {
			return field.getName();
		}

		@SuppressWarnings("unchecked")
		protected List<String> values(Object object) {
			logger.fine(() -> "object " + object);

			Object value = getFieldValue(object);
			Collection<?> collection = (value instanceof Collection) ? (Collection<?>) value
					: ((value != null) ? singleton(value) : null);
			StringConverter converter = converter(annotation);

			List<String> list = safeStream(collection).map(converter::convertToString).collect(toList());
			logger.fine(() -> "list " + list);

			return list;
		}

		protected Object getFieldValue(Object object) {
			return unsafeGet(() -> getGetter(field).invoke(object));
		}

		protected static StringConverter<?> converter(View.Field annotation) {
			logger.fine(() -> "annotation " + annotation);

			Class<? extends StringConverter<?>> class1 = nonNullInstance(
					annotation != null ? annotation.converter() : null, ForString.class);
			logger.fine(() -> "class1 " + class1);

			StringConverter<?> converter = safeGet(
					() -> (StringConverter<?>) class1.getDeclaredConstructor().newInstance());
			return converter;
		}
	}
}
