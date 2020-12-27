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
import static java.util.function.Predicate.not;
import static java.util.logging.Level.ALL;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;

import com.backflipsource.Control;
import com.backflipsource.Control.Factory;
import com.backflipsource.Helpers;
import com.backflipsource.dynamic.DefaultDynamicClass;
import com.backflipsource.dynamic.DynamicAnnotated;
import com.backflipsource.dynamic.DynamicAnnotation;
import com.backflipsource.dynamic.DynamicClass;
import com.backflipsource.dynamic.DynamicField;
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
		if (entities == null) {
			// entities =
			// safeStream(entityUI.getModes()).collect(linkedHashMapCollector(identity(),
			// null));
			entities = new LinkedHashMap<>();
			safeStream(entityUI.getModes()).forEach(mode2 -> entities.put(mode2, null));
		}
		DynamicClass entity = entities.get(mode);
		if (entity == null) {
			entity = nonNullInstance(modeEntity(this.entity, mode), this.entity);
			entities.put(mode, entity);
		}
		return entity;
	}

	@Override
	public Map<String, Control.Factory<?>> controlFactories(Class<? extends Mode> mode) {
		if (controlFactories == null) {
			// controlFactories =
			// safeStream(entityUI.getModes()).collect(linkedHashMapCollector(identity(),
			// null));
			controlFactories = new LinkedHashMap<>();
			safeStream(entityUI.getModes()).forEach(mode2 -> controlFactories.put(mode2, null));
		}
		Map<String, Factory<?>> factories = controlFactories.get(mode);
		if (factories == null) {
			factories = fieldMap(entity(mode), field -> controlFactory(field, mode));
			controlFactories.put(mode, factories);
		}
		return factories;
	}

	@Override
	public Map<String, StringConverter<?>> converters(Class<? extends Mode> mode) {
		if (converters == null) {
			// converters =
			// safeStream(entityUI.getModes()).collect(linkedHashMapCollector(identity(),
			// null));
			converters = new LinkedHashMap<>();
			safeStream(entityUI.getModes()).forEach(mode2 -> converters.put(mode2, null));
		}
		Map<String, StringConverter<?>> converters2 = converters.get(mode);
		if (converters2 == null) {
			converters2 = fieldMap(entity(mode), field -> converter(field, mode));
			converters.put(mode, converters2);
		}
		return converters2;
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public Control.Factory<?> controlFactory(DynamicAnnotated entityOrField, Class<? extends Mode> mode) {
		Class<? extends Control.Factory> factoryClass = controlFactoryClass(entityOrField, mode);

		Control.Factory<?> instance = null;
		if (factoryClass != null) {
			instance = unsafeGet(() -> factoryClass.getConstructor().newInstance());
			((AbstractEntityControl.Factory<?>) instance).init(entityUI, entityOrField, mode);
		}

		Control.Factory<?> factory = instance;
		logger.fine(() -> "entityOrField " + entityOrField + " mode " + mode + " factory " + factory);
		return factory;
	}

	protected String uri(DynamicClass entity) {
		String uri = entity.annotations("Entity").map(annotation -> safeString(annotation.getValue("uri")))
				.filter(not(Helpers::emptyString)).findFirst().orElse("/" + entity.getName().toLowerCase());
		logger.fine(() -> "uri " + uri);
		return uri;
	}

	public static DynamicAnnotation modeAnnotation(DynamicAnnotated annotated, String annotation,
			Class<? extends Mode> mode) {
		DynamicAnnotation field = annotated.annotations(annotation).filter(field2 -> {
			Class<?>[] mode2 = (Class[]) field2.getValue("mode");
			return safeList(mode2).contains(mode);
		}).findFirst().orElseGet(() -> annotated.annotations(annotation)
				.filter(field2 -> emptyArray((Class[]) field2.getValue("mode"))).findFirst().orElse(null));
		return field;
	}

	@SuppressWarnings("unchecked")
	public static StringConverter<?> converter(DynamicField field, Class<? extends Mode> mode) {
		DynamicAnnotation field2 = modeAnnotation(field, "Entity.Field", mode);

		Class<? extends StringConverter<?>> converterClass = nonNullInstance(
				field2 != null ? (Class<? extends StringConverter<?>>) field2.getValue("converter") : null,
				ForString.class);

		StringConverter<?> converter = (StringConverter<?>) safeGet(
				() -> converterClass.getConstructor().newInstance());

		logger.fine(() -> "field = " + field + ", field2 = " + field2 + ", converter = " + converter);
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
		return nonEmptyString((render != null) ? render.getValue("heading") : null,
				() -> joinStrings(camelCaseWords(capitalizeString(annotated.getName())), " "));
	}

	public static DynamicClass modeEntity(DynamicClass entity, Class<? extends Mode> mode) {
		DynamicAnnotation render = modeAnnotation(entity, "Render", mode);

		Class<?> entity2 = (render != null) ? (Class<?>) render.getValue("entity") : null;
		DynamicClass entity3;
		if ((entity2 != null) && DynamicClass.class.isAssignableFrom(entity2)) {
			entity3 = (DynamicClass) unsafeGet(() -> entity2.getConstructor().newInstance());
		} else if ((entity2 != null) && entity2 != Object.class) {
			entity3 = new DefaultDynamicClass(entity2);
		} else {
			entity3 = null;
		}

		return entity3;
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
	protected Class<? extends Control.Factory> controlFactoryClass(DynamicAnnotated entityOrField,
			Class<? extends Mode> mode) {
		DynamicAnnotation render = modeAnnotation(entityOrField, "Render", mode);
		if (render == null) {
			return null;
		}

		Class<? extends Control.Factory> controlFactory = (render != null) ? render.getValue("controlFactory") : null;
		if ((controlFactory == null) || (controlFactory == Control.Factory.class)) {
			controlFactory = defaultControlFactoryClass(entityOrField, mode);
		}

		return controlFactory;
	}

	@SuppressWarnings("rawtypes")
	protected Class<? extends Control.Factory> defaultControlFactoryClass(DynamicAnnotated entityOrField,
			Class<? extends Mode> mode) {

		boolean list = EntityList.class.isAssignableFrom(mode);
		boolean detail = EntityDetail.class.isAssignableFrom(mode);
		boolean form = EntityForm.class.isAssignableFrom(mode);

		if (entityOrField instanceof DynamicClass) {
			if (list) {
				return Table.Factory.class;
			}
			if (detail) {
				return DescriptionList.Factory.class;
			}
			if (form) {
				return Form.Factory.class;
			}
			return null;
		}

		if (entityOrField instanceof DynamicField) {
			DynamicAnnotation field = modeAnnotation(entityOrField, "Entity.Field", mode);
			boolean identifier = (field != null) ? field.getValue("identifier") : false;

			if (identifier) {
				return form ? Span.Factory.class : Anchor.Factory.class;
			}

			if (Iterable.class.isAssignableFrom(((DynamicField) entityOrField).getType())) {
				return Table.Factory.class;
			}

			return form ? Input.Factory.class : Span.Factory.class;
		}

		return null;
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
