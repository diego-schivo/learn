package com.backflipsource;

import static com.backflipsource.Helpers.camelCaseWords;
import static com.backflipsource.Helpers.emptyCollection;
import static com.backflipsource.Helpers.joinStrings;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.ui.DefaultEntityResource.modeRender;
import static java.util.Collections.singleton;
import static java.util.logging.Level.ALL;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

import com.backflipsource.AbstractControl.Factory;
import com.backflipsource.servlet.StringConverter;
import com.backflipsource.ui.Render;
import com.backflipsource.ui.spec.EntityUI.Mode;

public abstract class AbstractControl<F extends Factory<?>> implements Control {

	private static Logger logger = logger(AbstractControl.class, ALL);

	protected F factory;

	protected Object target;

	protected List<String> values;

	@Override
	public Object getTarget() {
		return target;
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
		return factory.heading;
	}

	@SuppressWarnings("rawtypes")
	public static abstract class Factory<T extends AbstractControl> implements Control.Factory {

		protected Class<T> control;

		protected String name;

		protected String page;

		protected StringConverter converter;

		protected Function<Object, Object> getValue;

		protected Control parent;

		protected String heading;

		protected Factory(Class<T> control, String name, Function<Object, Object> getValue,
				StringConverter valueConverter, String page, String heading) {
			this.control = control;
			this.name = name;
			this.getValue = getValue;
			this.converter = valueConverter;
			this.page = nonEmptyString(page, "/" + control.getSimpleName().toLowerCase() + ".jsp");
			this.heading = heading;
		}

		protected Factory(Class<T> class1) {
			this(class1, null, null, null, null, null);
		}

		public String getName() {
			return name;
		}

		@Override
		public String getHeading() {
			return heading;
		}

		@Override
		public void setParent(Control parent) {
			this.parent = parent;
		}

		@Override
		public T create(Object target) {
			T control = unsafeGet(() -> this.control.getConstructor().newInstance());
			control.factory = this;
			control.target = target;
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

		public static String name(AnnotatedElement annotated) {
			if (annotated instanceof Class) {
				return ((Class) annotated).getSimpleName();
			}
			if (annotated instanceof Field) {
				return ((Field) annotated).getName();
			}
			return null;
		}
	}
}
