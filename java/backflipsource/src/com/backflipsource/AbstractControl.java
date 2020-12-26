package com.backflipsource;

import static com.backflipsource.Helpers.classEnclosingName;
import static com.backflipsource.Helpers.emptyCollection;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static java.util.Collections.singleton;
import static java.util.logging.Level.ALL;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

import com.backflipsource.servlet.StringConverter;

public abstract class AbstractControl implements Control {

	private static Logger logger = logger(AbstractControl.class, ALL);

	protected Factory<?> factory;

	protected Object target;

	protected List<String> values;

	@Override
	public Object getTarget() {
		return target;
	}

	@Override
	public String getName() {
		return factory.getName();
	}

	@Override
	public List<String> getValues() {
		return values;
	}

	@Override
	public String getPage() {
		return factory.getPage();
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
		return factory.getParent();
	}

	@Override
	public String getHeading() {
		return factory.getHeading();
	}

	@SuppressWarnings("rawtypes")
	public static abstract class Factory<T extends AbstractControl> implements Control.Factory {

		protected Class<T> control;

		protected String name;

		protected Function<Object, Object> valueGetter;

		protected StringConverter converter;

		protected Control parent;

		protected String page;

		protected String heading;

		public Class<T> getControl() {
			return control;
		}

		public void setControl(Class<T> control) {
			this.control = control;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Function<Object, Object> getValueGetter() {
			return valueGetter;
		}

		public void setValueGetter(Function<Object, Object> valueGetter) {
			this.valueGetter = valueGetter;
		}

		public StringConverter getConverter() {
			return converter;
		}

		public void setConverter(StringConverter converter) {
			this.converter = converter;
		}

		public Control getParent() {
			return parent;
		}

		public void setParent(Control parent) {
			this.parent = parent;
		}

		public String getPage() {
			return page;
		}

		public void setPage(String page) {
			this.page = page;
		}

		public String getHeading() {
			return heading;
		}

		public void setHeading(String heading) {
			this.heading = heading;
		}

		@Override
		public T create(Object target) {
			logger.fine(() -> "this = " + this);
			T control = unsafeGet(() -> this.control.getConstructor().newInstance());
			control.factory = this;
			control.target = target;
			control.values = (converter != null) ? values(target) : null;

			init(control);

			return control;
		}

		@Override
		public String toString() {
			return classEnclosingName(getClass()) + "(name = " + name + ", converter = " + converter + ")";
		}

		@SuppressWarnings("unchecked")
		protected List<String> values(Object object) {
			Object value = (valueGetter != null) ? valueGetter.apply(object) : object;

			Collection<?> collection = (value instanceof Collection) ? (Collection<?>) value
					: ((value != null) ? singleton(value) : null);
			logger.fine(() -> "collection = " + collection);

			List<String> list = safeStream(collection).map(converter::convertToString).collect(toList());
			logger.fine(() -> "list = " + list);

			return list;
		}

		protected void init(T control) {
		}
	}
}
