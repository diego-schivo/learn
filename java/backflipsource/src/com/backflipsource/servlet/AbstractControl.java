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
import java.util.function.Function;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.backflipsource.servlet.StringConverter.ForString;

public abstract class AbstractControl implements Control {

	private static Logger logger = getLogger(AbstractControl.class.getName());

	static {
		logger.setLevel(Level.ALL);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

	protected AbstractFactory<?> factory;

	// TODO target
	protected Object item;

	protected List<String> values;

	@Override
	public EntityView getEntityView() {
		return factory.entityView;
	}

	@Override
	public Object getItem() {
		return item;
	}

	@Override
	public String getName() {
		return factory.name;
	}

	@Override
	public List<String> getValues() {
		return values;
	}

	@Override
	public String getPage() {
		return factory.page;
	}

	@Override
	public String getValue() {
		if (emptyCollection(getValues())) {
			return null;
		}
		return getValues().get(0);
	}

	@Override
	public Control getParent() {
		return factory.parent;
	}

	@SuppressWarnings("rawtypes")
	public static abstract class AbstractFactory<T extends AbstractControl> implements Factory {

		protected Class<T> class1;

		protected EntityView entityView;

		protected String name;

		protected String page;

		protected StringConverter converter;

		protected Function<Object, Object> getValue;

		protected Control parent;

		protected AbstractFactory(Class<T> class1, Field field, View.Field annotation) {
			this(class1, safeGet(
					() -> getViews().get(((Class<?>) listGet(getArgumentTypes(field.getGenericType()), 0)).getName())),
					field.getName(), object -> unsafeGet(() -> object != null ? getGetter(field).invoke(object) : null),
					converter(annotation), annotation.controlPage());
		}

		protected AbstractFactory(Class<T> class1, EntityView entityView, String name,
				Function<Object, Object> getValue, StringConverter valueConverter, String page) {
			this.class1 = class1;
			this.entityView = entityView;
			this.name = name;
			this.getValue = getValue;
			this.converter = valueConverter;
			this.page = nonEmptyString(page, class1.getSimpleName().toLowerCase() + ".jsp");
		}

		protected AbstractFactory(Class<T> class1, EntityView entityView) {
			this(class1, entityView, null, null, null, null);
		}

		public String getName() {
			return name;
		}

		@Override
		public void setParent(Control parent) {
			this.parent = parent;
		}

		@Override
		public T control(Object target) {
			T control = unsafeGet(() -> class1.getDeclaredConstructor().newInstance());
			control.factory = this;
			control.item = target;
			control.values = (converter != null) ? values(target) : null;
			return control;
		}

		@SuppressWarnings("unchecked")
		protected List<String> values(Object object) {
			logger.fine(() -> "object " + object);

			Object value = (getValue != null) ? getValue.apply(object) : object;
			Collection<?> collection = (value instanceof Collection) ? (Collection<?>) value
					: ((value != null) ? singleton(value) : null);

			List<String> list = safeStream(collection).map(converter::convertToString).collect(toList());
			logger.fine(() -> "list " + list);

			return list;
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
