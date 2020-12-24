package com.backflipsource.servlet;

import static com.backflipsource.Helpers.classFields;
import static com.backflipsource.Helpers.linkedHashMapCollector;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.servlet.AbstractControl.Factory.converter;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.util.function.Predicate.not;
import static java.util.logging.Level.ALL;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.logging.Logger;

import com.backflipsource.Helpers;
import com.backflipsource.servlet.AbstractControl.Factory;

public class DefaultEntityView implements EntityView {

	private static Logger logger = logger(DefaultEntityView.class, ALL);

	protected Class<?> class1;

	protected List<Field> fields;

	protected String uri;

	public DefaultEntityView(Class<?> class1) {
		this.class1 = class1;
		fields = safeStream(classFields(class1))
				.filter(field -> field.getAnnotationsByType(View.Field.class).length > 0).collect(toList());
		this.uri = uri(class1);
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, Control.Factory> controlFactories(Class<?> view) {
		logger.fine("EntityView controlFactories");

		return viewFieldMap(view, (field, annotation) -> controlFactory(field, annotation, view));
	}

	@Override
	public Map<String, StringConverter<?>> converters(Class<?> view) {
		logger.fine("EntityView converters");

		return viewFieldMap(view, (field, annotation) -> converter(annotation));
	}

	@SuppressWarnings("rawtypes")
	protected Control.Factory controlFactory(Field field, View.Field annotation, Class<?> view) {
		Class<? extends Control.Factory> class1 = (annotation != null) ? annotation.controlFactory() : null;

		boolean undefined = (class1 == null) || Objects.equals(class1, Control.Factory.class);
		if (undefined) {
			class1 = defaultControlFactoryClass(field, annotation, view);
		}

		Class<? extends Control.Factory> factoryClass = class1;
		logger.fine(() -> "factoryClass " + factoryClass);

		Control.Factory factory = unsafeGet(() -> (Control.Factory) factoryClass
				.getDeclaredConstructor(Field.class, View.Field.class).newInstance(field, annotation));

		return factory;
	}

	@SuppressWarnings("rawtypes")
	protected Class<? extends Factory> defaultControlFactoryClass(Field field, View.Field annotation,
			Class<?> view) {
		boolean identifier = annotation.identifier();
		boolean edit = Objects.equals(view, View.Edit.class);

		if (identifier) {
			return edit ? Span.Factory.class : Anchor.Factory.class;
		}

		if (Iterable.class.isAssignableFrom(field.getType())) {
			return Table.Factory.class;
		}

		return edit ? Input.Factory.class : Span.Factory.class;
	}

	protected String uri(Class<?> class1) {
		logger.fine("EntityView uri");

		String urlPattern1 = safeStream(class1.getAnnotationsByType(View.class)).map(View::uri)
				.filter(not(Helpers::emptyString)).findFirst().orElse("/" + class1.getSimpleName().toLowerCase());
		return urlPattern1;
	}

	@SuppressWarnings("unchecked")
	protected <T> Map<String, T> viewFieldMap(Class<?> view, BiFunction<Field, View.Field, T> function) {
		logger.fine("EntityView viewFieldMap");

		return safeStream(fields).map(field -> {
			logger.fine(() -> "field " + field);

			View.Field[] annotations = field.getAnnotationsByType(View.Field.class);
			View.Field annotation = safeStream(annotations).filter(item -> safeList(item.view()).contains(view))
					.findFirst().orElseGet(() -> safeStream(annotations).filter(item -> item.view().length == 0)
							.findFirst().orElse(null));
			logger.fine(() -> "annotation " + annotation);

			if (annotation == null) {
				return null;
			}

			T t = function.apply(field, annotation);
			logger.fine(() -> "t " + t);

			return new Object[] { field.getName(), t };
		}).filter(Objects::nonNull).collect(linkedHashMapCollector(array -> (String) array[0], array -> (T) array[1]));
	}
}
