package com.backflipsource.servlet;

import static com.backflipsource.Helpers.classFields;
import static com.backflipsource.Helpers.linkedHashMapCollector;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static com.backflipsource.servlet.EntityServlet.getContextListener;
import static java.util.function.Function.identity;
import static java.util.function.Predicate.not;
import static java.util.logging.Level.ALL;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;

import com.backflipsource.Helpers;
import com.backflipsource.entity.annotation.Render;
import com.backflipsource.servlet.AbstractControl.Factory;
import com.backflipsource.servlet.StringConverter.ForString;

public class DefaultEntityView implements EntityView {

	private static Logger logger = logger(DefaultEntityView.class, ALL);

	protected Class<?> entity;

	protected String uri;

	protected Map<Class<?>, Class<?>> entities;

	protected Map<Class<?>, Map<String, Control.Factory<?>>> controlFactories;

	protected Map<Class<?>, Map<String, StringConverter<?>>> converters;

	public DefaultEntityView(Class<?> entity) {
		this.entity = entity;
		this.uri = uri(entity);

		entities = safeStream(View.ALL)
				.collect(linkedHashMapCollector(identity(), view -> nonNullInstance(presentation(view), entity)));

		controlFactories = safeStream(View.ALL).collect(linkedHashMapCollector(identity(),
				view -> viewFieldMap(entities.get(view), view, field -> controlFactory(field, view))));

		converters = safeStream(View.ALL).collect(linkedHashMapCollector(identity(),
				view -> viewFieldMap(entities.get(view), view, field -> converter(field, view))));
	}

	@Override
	public Class<?> getEntity() {
		return entity;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public Class<?> entity(Class<?> view) {
		return entities.get(view);
	}

	@Override
	public Map<String, Control.Factory<?>> controlFactories(Class<?> view) {
		return controlFactories.get(view);
	}

	@Override
	public Map<String, StringConverter<?>> converters(Class<?> view) {
		return converters.get(view);
	}

	protected Class<?> presentation(Class<?> view) {
		Render render = safeStream(entity.getAnnotationsByType(Render.class))
				.filter(annotation -> safeList(annotation.view()).contains(view)).findFirst().orElse(null);
		Class<?> presentation = (render != null) ? render.presentation() : null;
		if (presentation == Object.class) {
			presentation = null;
		}
		return presentation;
	}

	@SuppressWarnings("rawtypes")
	protected static Control.Factory<?> controlFactory(AnnotatedElement annotated, Class<?> view) {
		Render annotation = fieldAnnotation(annotated, view, Render.class);
		Class<? extends Control.Factory> class1 = (annotation != null) ? annotation.controlFactory() : null;

		boolean undefined = (class1 == null) || Objects.equals(class1, Control.Factory.class);
		if (undefined) {
			class1 = defaultControlFactoryClass(annotated, view);
		}

		Class<? extends Control.Factory> factoryClass = class1;
		logger.fine(() -> "factoryClass " + factoryClass);

		Control.Factory<?> factory = unsafeGet(() -> {
			Object instance;
			if (annotated instanceof Field) {
				instance = factoryClass.getConstructor(Field.class, Class.class).newInstance(annotated, view);
			} else {
				instance = factoryClass.getConstructor(EntityView.class)
						.newInstance(getContextListener().getViews().get(annotated));
			}
			return (Control.Factory) instance;
		});

		return factory;
	}

	@SuppressWarnings("rawtypes")
	protected static Class<? extends Factory> defaultControlFactoryClass(AnnotatedElement annotated, Class<?> view) {
		if (!(annotated instanceof Field)) {
			return DescriptionList.Factory.class;
		}

		// boolean identifier = annotation.identifier();
		boolean identifier = false;
		boolean edit = Objects.equals(view, View.Edit.class);

		if (identifier) {
			return edit ? Span.Factory.class : Anchor.Factory.class;
		}

		if (Iterable.class.isAssignableFrom(((Field) annotated).getType())) {
			return Table.Factory.class;
		}

		return edit ? Input.Factory.class : Span.Factory.class;
	}

	protected String uri(Class<?> class1) {
		String uri = safeStream(class1.getAnnotationsByType(View.class)).map(View::uri)
				.filter(not(Helpers::emptyString)).findFirst().orElse("/" + class1.getSimpleName().toLowerCase());
		logger.fine(() -> "uri " + uri);
		return uri;
	}

	@SuppressWarnings("unchecked")
	protected static <T> Map<String, T> viewFieldMap(Class<?> class1, Class<?> view, Function<Field, T> function) {

		List<Field> fields = safeStream(classFields(class1))
				// .filter(field -> field.getAnnotationsByType(annotationClass).length > 0)
				.collect(toList());

		Map<String, T> map = safeStream(fields).map(field -> {
			logger.fine(() -> "field " + field);

//			A annotation = fieldAnnotation(field, view, annotationClass);
//			logger.fine(() -> "annotation " + annotation);
//
//			if (annotation == null) {
//				return null;
//			}

			T t = function.apply(field);
			logger.fine(() -> "t " + t);

			return new Object[] { field.getName(), t };
		}).filter(Objects::nonNull).collect(linkedHashMapCollector(array -> (String) array[0], array -> (T) array[1]));
		logger.fine(() -> "map " + map);
		return map;
	}

	protected static <A extends Annotation> A fieldAnnotation(AnnotatedElement annotated, Class<?> view,
			Class<A> annotationClass) {
		A[] annotations = annotated.getAnnotationsByType(annotationClass);
		A annotation = safeStream(annotations).filter(item -> safeList(view(item)).contains(view)).findFirst()
				.orElseGet(
						() -> safeStream(annotations).filter(item -> view(item).length == 0).findFirst().orElse(null));
		return annotation;
	}

	protected static <A extends Annotation> Class<?>[] view(A annotation) {
		if (annotation instanceof View.Field) {
			return ((View.Field) annotation).view();
		}
		if (annotation instanceof Render) {
			return ((Render) annotation).view();
		}
		return null;
	}

	protected static StringConverter<?> converter(Field field, Class<?> view) {
		View.Field annotation = fieldAnnotation(field, view, View.Field.class);
		return converter(annotation);
	}

	protected static StringConverter<?> converter(View.Field annotation) {
		logger.fine(() -> "annotation " + annotation);

		Class<? extends StringConverter<?>> class1 = nonNullInstance(annotation != null ? annotation.converter() : null,
				ForString.class);
		logger.fine(() -> "class1 " + class1);

		StringConverter<?> converter = safeGet(() -> (StringConverter<?>) class1.getConstructor().newInstance());
		return converter;
	}
}
