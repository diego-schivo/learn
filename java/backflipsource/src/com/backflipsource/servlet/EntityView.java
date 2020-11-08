package com.backflipsource.servlet;

import static com.backflipsource.Helpers.linkedHashMapCollector;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeStream;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import com.backflipsource.Helpers;

public class EntityView {

	protected Class<?> class1;
	protected List<Field> fields;
	protected String uri;

	public EntityView(Class<?> class1) {
		this.class1 = class1;
		fields = safeStream(Helpers.getFields(class1))
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

	protected String uri(Class<?> class1) {
		String urlPattern1 = safeStream(class1.getAnnotationsByType(View.class)).map(View::uri)
				.filter(not(Helpers::emptyString)).findFirst().orElse("/" + class1.getSimpleName().toLowerCase());
		return urlPattern1;
	}

	public Map<String, Control.Factory> controlFactories(Class<?> view) {
		return viewFieldMap(view, (field, annotation) -> {
			Class<? extends Control> class1 = nonNullInstance(annotation != null ? annotation.control() : null,
					Control.class);
			if (Objects.equals(class1, Control.class)) {
				if (Objects.equals(view, View.Edit.class)) {
					class1 = annotation.identifier() ? Span.class : Input.class;
				} else {
					class1 = annotation.identifier() ? Anchor.class : Span.class;
				}
			}
			Class<? extends Control> class2 = class1;
			Class<?> class3 = safeGet(() -> Class.forName(class2.getName() + "$Factory"));

			Control.Factory factory = safeGet(
					() -> (Control.Factory) class3.getDeclaredConstructor(Field.class, StringConverter.class)
							.newInstance(field, converter(annotation)));
			return factory;
		});
	}

	protected Map<String, StringConverter<?>> converters(Class<?> view) {
		return viewFieldMap(view, (field, annotation) -> {
			return converter(annotation);
		});
	}

	@SuppressWarnings("unchecked")
	protected <T> Map<String, T> viewFieldMap(Class<?> view, BiFunction<Field, View.Field, T> function) {
		return safeStream(fields).map(field -> {
			View.Field[] annotations = field.getAnnotationsByType(View.Field.class);
			View.Field annotation = safeStream(annotations).filter(item -> safeList(item.view()).contains(view))
					.findFirst().orElseGet(() -> safeStream(annotations).filter(item -> item.view().length == 0)
							.findFirst().orElse(null));
			if (annotation == null) {
				return null;
			}
			T t = function.apply(field, annotation);
			return new Object[] { field.getName(), t };
		}).filter(Objects::nonNull).collect(linkedHashMapCollector(array -> (String) array[0], array -> (T) array[1]));
	}

	protected StringConverter<?> converter(View.Field annotation) {
		Class<? extends StringConverter<?>> class1 = nonNullInstance(annotation != null ? annotation.converter() : null,
				StringConverter.ForString.class);
		StringConverter<?> converter = safeGet(
				() -> (StringConverter<?>) class1.getDeclaredConstructor().newInstance());
		return converter;
	}
}
