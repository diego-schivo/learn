package com.backflipsource.servlet;

import static com.backflipsource.Helpers.emptyCollection;
import static com.backflipsource.Helpers.getArgumentTypes;
import static com.backflipsource.Helpers.getGetter;
import static com.backflipsource.Helpers.listGet;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.servlet.DefaultEntityView.converter;
import static com.backflipsource.servlet.DefaultEntityView.fieldAnnotation;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static com.backflipsource.servlet.EntityServlet.CONTEXT_LISTENER;
import static com.backflipsource.servlet.EntityServlet.getContextListener;
import static java.util.Collections.singleton;
import static java.util.logging.Level.ALL;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Logger;

import com.backflipsource.entity.annotation.Render;

public abstract class AbstractControl implements Control {

	private static Logger logger = logger(AbstractControl.class, ALL);

	protected Factory<?> factory;

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

	@Override
	public String getHeading() {
		ResourceBundle resourceBundle = CONTEXT_LISTENER.get().getResourceBundle();
		String key = (getEntityView().getEntity().getSimpleName() + "." + getView().getSimpleName()).toLowerCase();
		return safeGet(() -> resourceBundle.getString(key));
	}

	private Collection<Control.Factory<?>> factories;

	public Collection<Control.Factory<?>> getFactories() {
		if (factories == null) {
			factories = getEntityView().controlFactories(getView()).values();
			factories.forEach(factory -> factory.setParent(this));
		}
		return factories;
	}

	protected abstract Class<?> getView();

	@SuppressWarnings("rawtypes")
	public static abstract class Factory<T extends AbstractControl> implements Control.Factory {

		protected Class<T> class1;

		protected EntityView entityView;

		protected String name;

		protected String page;

		protected StringConverter converter;

		protected Function<Object, Object> getValue;

		protected Control parent;

		protected Factory(Class<T> class1, Field field, Class<?> view) {
			this(class1,
					safeGet(() -> getContextListener().getViews()
							.get(((Class<?>) listGet(getArgumentTypes(field.getGenericType()), 0)))),
					field.getName(), object -> unsafeGet(() -> object != null ? getGetter(field).invoke(object) : null),
					converter(field, view), controlPage(field, view));
		}

		protected Factory(Class<T> class1, EntityView entityView, String name, Function<Object, Object> getValue,
				StringConverter valueConverter, String page) {
			this.class1 = class1;
			this.entityView = entityView;
			this.name = name;
			this.getValue = getValue;
			this.converter = valueConverter;
			this.page = nonEmptyString(page, "/" + class1.getSimpleName().toLowerCase() + ".jsp");
		}

		protected Factory(Class<T> class1, EntityView entityView) {
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
		public T create(Object target) {
			T control = unsafeGet(() -> class1.getConstructor().newInstance());
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

		protected static String controlPage(Field field, Class<?> view) {
			Render annotation = fieldAnnotation(field, view, Render.class);
			logger.fine(() -> "annotation " + annotation);

			if (annotation == null) {
				return null;
			}

			return annotation.controlPage();
		}
	}
}
