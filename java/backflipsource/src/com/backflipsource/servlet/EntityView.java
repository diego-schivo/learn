package com.backflipsource.servlet;

import static com.backflipsource.Helpers.classFields;
import static com.backflipsource.Helpers.linkedHashMapCollector;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeStream;
import static java.util.function.Predicate.not;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.backflipsource.Helpers;

public class EntityView {

	private static Logger logger = getLogger(EntityView.class.getName());

	static {
		logger.setLevel(Level.ALL);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

	protected Class<?> class1;

	protected List<Field> fields;

	protected String uri;

	public EntityView(Class<?> class1) {
		this.class1 = class1;
		fields = safeStream(classFields(class1))
				.filter(field -> field.getAnnotationsByType(View.Field.class).length > 0).collect(toList());
		this.uri = uri(class1);
	}

	public Class<?> getClass1() {
		return class1;
	}

	public List<Field> getFields() {
		return fields;
	}

	public String getUri() {
		return uri;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Control.Factory> controlFactories(Class<?> view) {
		logger.fine("EntityView controlFactories");

		return viewFieldMap(view, (field, annotation) -> {
			Class<? extends Control.Factory> class1 = nonNullInstance(
					annotation != null ? annotation.controlFactory() : null, Control.Factory.class);

			if (Objects.equals(class1, Control.Factory.class)) {
				class1 = defaultControlFactory(field, annotation, view);
			}

			Class<? extends Control.Factory> class2 = class1;
			logger.fine(() -> "class1 " + class2);

			Control.Factory factory = safeGet(() -> (Control.Factory) class2
					.getDeclaredConstructor(Field.class, View.Field.class).newInstance(field, annotation));
			return factory;
		});
	}

	@SuppressWarnings("rawtypes")
	protected Class<? extends Control.Factory> defaultControlFactory(Field field, View.Field annotation,
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

	protected Map<String, StringConverter<?>> converters(Class<?> view) {
		logger.fine("EntityView converters");

		return viewFieldMap(view, (field, annotation) -> {
			return Control.Factory.converter(annotation);
		});
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
