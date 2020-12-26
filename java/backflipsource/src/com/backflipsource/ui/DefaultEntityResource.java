package com.backflipsource.ui;

import static com.backflipsource.Helpers.camelCaseWords;
import static com.backflipsource.Helpers.capitalizeString;
import static com.backflipsource.Helpers.emptyArray;
import static com.backflipsource.Helpers.joinStrings;
import static com.backflipsource.Helpers.linkedHashMapCollector;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.safeString;
import static com.backflipsource.Helpers.unsafeGet;
import static java.util.Map.entry;
import static java.util.function.Function.identity;
import static java.util.function.Predicate.not;
import static java.util.logging.Level.ALL;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;

import com.backflipsource.Control;
import com.backflipsource.Helpers;
import com.backflipsource.dynamic.DynamicAnnotated;
import com.backflipsource.dynamic.DynamicAnnotation;
import com.backflipsource.dynamic.DynamicClass;
import com.backflipsource.dynamic.DynamicField;
import com.backflipsource.servlet.DescriptionList;
import com.backflipsource.servlet.StringConverter;
import com.backflipsource.servlet.StringConverter.ForString;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI;
import com.backflipsource.ui.spec.EntityUI.Mode;

public class DefaultEntityResource implements EntityResource {

	private static Logger logger = logger(DefaultEntityResource.class, ALL);

	protected String uri;

	protected DynamicClass entity;

	protected EntityUI entityUI;

	protected Map<Class<? extends Mode>, DynamicClass> entities;

	protected Map<Class<? extends Mode>, Map<String, Control.Factory<?>>> controlFactories;

	protected Map<Class<? extends Mode>, Map<String, StringConverter<?>>> converters;

	public DefaultEntityResource(DynamicClass entity, EntityUI entityUI) {
		this.uri = uri(entity);
		this.entity = entity;
		this.entityUI = entityUI;

		entities = safeStream(entityUI.getModes())
				.collect(linkedHashMapCollector(identity(), mode -> nonNullInstance(modeEntity(entity, mode), entity)));

		controlFactories = safeStream(entityUI.getModes()).collect(linkedHashMapCollector(identity(),
				mode -> fieldMap(entity(mode), field -> controlFactory(field, mode))));

		converters = safeStream(entityUI.getModes()).collect(
				linkedHashMapCollector(identity(), mode -> fieldMap(entity(mode), field -> converter(field, mode))));
	}

	@Override
	public DynamicClass getEntity() {
		return entity;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public DynamicClass entity(Class<? extends Mode> mode) {
		return entities.get(mode);
	}

	@Override
	public Map<String, Control.Factory<?>> controlFactories(Class<? extends Mode> mode) {
		return controlFactories.get(mode);
	}

	@Override
	public Map<String, StringConverter<?>> converters(Class<? extends Mode> mode) {
		return converters.get(mode);
	}

	protected String uri(DynamicClass entity) {
		String uri = entity.annotations("Entity").map(annotation -> safeString(annotation.getValue("uri")))
				.filter(not(Helpers::emptyString)).findFirst().orElse("/" + entity.getName().toLowerCase());
		logger.fine(() -> "uri " + uri);
		return uri;
	}

	public static DynamicAnnotation modeAnnotation(DynamicAnnotated annotated, String annotation,
			Class<? extends Mode> mode) {
		DynamicAnnotation field = annotated.annotations(annotation)
				.filter(field2 -> safeList((Class[]) field2.getValue("mode")).contains(mode)).findFirst()
				.orElseGet(() -> annotated.annotations(annotation)
						.filter(field2 -> emptyArray((Class[]) field2.getValue("mode"))).findFirst().orElse(null));
		return field;
	}

	public static StringConverter<?> converter(DynamicAnnotated annotated, Class<? extends Mode> mode) {
		return converter(modeAnnotation(annotated, "Entity.Field", mode));
	}

	@SuppressWarnings("unchecked")
	public static StringConverter<?> converter(DynamicAnnotation field) {
		if (field == null) {
			return null;
		}

		logger.fine(() -> "field " + field);

		Class<? extends StringConverter<?>> converterClass = nonNullInstance(
				field != null ? (Class<? extends StringConverter<?>>) field.getValue("converter") : null,
				ForString.class);
		logger.fine(() -> "converterClass " + converterClass);

		StringConverter<?> converter = safeGet(
				() -> (StringConverter<?>) converterClass.getConstructor().newInstance());
		return converter;
	}

	public static String controlPage(DynamicAnnotated annotated, Class<? extends Mode> mode) {
		DynamicAnnotation render = modeAnnotation(annotated, "Render", mode);
		if (render == null) {
			return null;
		}
		return (String) render.getValue("controlPage");
	}

	public static String heading(DynamicAnnotated annotated, Class<? extends Mode> mode) {
		DynamicAnnotation render = modeAnnotation(annotated, "Render", mode);
		if (render == null) {
			return null;
		}
		return nonEmptyString((String) render.getValue("heading"),
				() -> joinStrings(camelCaseWords(capitalizeString(annotated.getName())), " "));
	}

	public static DynamicClass modeEntity(DynamicAnnotated annotated, Class<? extends Mode> mode) {
		DynamicAnnotation render = modeAnnotation(annotated, "Render", mode);

		DynamicClass entity = (render != null) ? (DynamicClass) render.getValue("entity") : null;
		if ((entity != null) && safeString(entity.getName()).equals("Object")) {
			entity = null;
		}

		return entity;
	}

	protected static <T> Map<String, T> fieldMap(DynamicClass entityClass, Function<DynamicField, T> function) {

//		DynamicClass descriptor = annotationTypeInstance(entityClass.getAnnotation(Entity.class), Entity::descriptor,
//				DefaultDynamicClass.class);
//		((DefaultDynamicClass) descriptor).setTarget(entityClass);

		Map<String, T> map = entityClass.fields().map(field -> {
			T result = function.apply(field);
			logger.fine(() -> "field " + field + " result " + result);

			Entry<DynamicField, T> object = (result != null) ? entry(field, result) : null;
			return object;
		}).filter(Objects::nonNull)
				.collect(linkedHashMapCollector(entry -> entry.getKey().getName(), entry -> entry.getValue()));
		logger.fine(() -> "map " + map);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Control.Factory<?> controlFactory(DynamicAnnotated annotated, Class<? extends Mode> mode) {
		Class<? extends Control.Factory> factoryClass = controlFactoryClass(annotated, mode);
		if (factoryClass == null) {
			return null;
		}

		Control.Factory<?> factory = unsafeGet(() -> {
			Object instance = factoryClass.getConstructor(/* AnnotatedElement.class, Class.class */)
					.newInstance(/*
									 * annotated, mode
									 */);
			return (Control.Factory) instance;
		});

		logger.fine(() -> "annotated " + annotated + " mode " + mode + " factory " + factory);

		return factory;
	}

	@SuppressWarnings("rawtypes")
	protected static Class<? extends Control.Factory> controlFactoryClass(DynamicAnnotated annotated,
			Class<? extends Mode> mode) {
		DynamicAnnotation render = modeAnnotation(annotated, "Render", mode);
		if (render == null) {
			return null;
		}

		Class<? extends Control.Factory> controlFactory = render.getValue("controlFactory");
		if (controlFactory == Control.Factory.class) {
			controlFactory = defaultControlFactoryClass(annotated, mode);
		}
		return controlFactory;
	}

	@SuppressWarnings("rawtypes")
	protected static Class<? extends Control.Factory> defaultControlFactoryClass(DynamicAnnotated annotated,
			Class<? extends Mode> mode) {

		if (!(annotated instanceof DynamicField)) {
			return DescriptionList.Factory.class;
		}

		DynamicAnnotation field = modeAnnotation(annotated, "Entity.Field", mode);
		boolean identifier = (field != null) ? field.getValue("identifier") : false;

		boolean edit = (mode == EntityForm.class);

		if (identifier) {
			return edit ? Span.Factory.class : Anchor.Factory.class;
		}

		if (Iterable.class.isAssignableFrom(((DynamicField) annotated).getType())) {
			return Table.Factory.class;
		}

		return edit ? Input.Factory.class : Span.Factory.class;
	}

//	public static AnnotatedElement annotatedElement(Object object) {
//		if (object instanceof DefaultDynamicField) {
//			object = getFieldValue(object, "target", DefaultDynamicField.class);
//		}
//
//		AnnotatedElement annotated = (object instanceof AnnotatedElement) ? (AnnotatedElement) object : null;
//		return annotated;
//	}
	
	@Override
	public String toString() {
		return "DefaultEntityResource(uri=" + uri + ")";
	}
}
