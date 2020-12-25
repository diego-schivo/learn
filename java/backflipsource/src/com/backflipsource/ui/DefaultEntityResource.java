package com.backflipsource.ui;

import static com.backflipsource.AbstractControl.Factory.name;
import static com.backflipsource.Helpers.camelCaseWords;
import static com.backflipsource.Helpers.classFields;
import static com.backflipsource.Helpers.emptyArray;
import static com.backflipsource.Helpers.joinStrings;
import static com.backflipsource.Helpers.linkedHashMapCollector;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static java.util.function.Function.identity;
import static java.util.function.Predicate.not;
import static java.util.logging.Level.ALL;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;

import com.backflipsource.Control;
import com.backflipsource.Helpers;
import com.backflipsource.servlet.DescriptionList;
import com.backflipsource.servlet.StringConverter;
import com.backflipsource.servlet.StringConverter.ForString;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI;
import com.backflipsource.ui.spec.EntityUI.Mode;

public class DefaultEntityResource implements EntityResource {

	private static Logger logger = logger(DefaultEntityResource.class, ALL);

	protected String uri;

	protected Class<?> entity;

	protected EntityUI entityUI;

	protected Map<Class<? extends Mode>, Class<?>> entities;

	protected Map<Class<? extends Mode>, Map<String, Control.Factory<?>>> controlFactories;

	protected Map<Class<? extends Mode>, Map<String, StringConverter<?>>> converters;

	public DefaultEntityResource(Class<?> entity, EntityUI entityUI) {
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
	public Class<?> getEntity() {
		return entity;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public Class<?> entity(Class<? extends Mode> mode) {
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

	protected String uri(Class<?> class1) {
		String uri = safeStream(class1.getAnnotationsByType(Entity.class)).map(Entity::uri)
				.filter(not(Helpers::emptyString)).findFirst().orElse("/" + class1.getSimpleName().toLowerCase());
		logger.fine(() -> "uri " + uri);
		return uri;
	}

	public static Entity.Field modeField(AnnotatedElement annotated, Class<? extends Mode> mode) {
		Entity.Field[] fields = annotated.getAnnotationsByType(Entity.Field.class);
		Entity.Field field = safeStream(fields).filter(field2 -> safeList(field2.mode()).contains(mode)).findFirst()
				.orElseGet(
						() -> safeStream(fields).filter(field2 -> emptyArray(field2.mode())).findFirst().orElse(null));
		return field;
	}

	public static Render modeRender(AnnotatedElement annotated, Class<? extends Mode> mode) {
		if (annotated == null || mode == null) {
			return null;
		}

		Render[] renders = annotated.getAnnotationsByType(Render.class);
		Render render = safeStream(renders).filter(render2 -> safeList(render2.mode()).contains(mode)).findFirst()
				.orElseGet(() -> safeStream(renders).filter(render2 -> emptyArray(render2.mode())).findFirst()
						.orElse(null));
		return render;
	}

	public static StringConverter<?> converter(AnnotatedElement annotated, Class<? extends Mode> view) {
		return converter(modeField(annotated, view));
	}

	public static StringConverter<?> converter(Entity.Field field) {
		if (field == null) {
			return null;
		}

		logger.fine(() -> "field " + field);

		Class<? extends StringConverter<?>> converterClass = nonNullInstance(field != null ? field.converter() : null,
				ForString.class);
		logger.fine(() -> "converterClass " + converterClass);

		StringConverter<?> converter = safeGet(
				() -> (StringConverter<?>) converterClass.getConstructor().newInstance());
		return converter;
	}

	public static String controlPage(AnnotatedElement annotated, Class<? extends Mode> mode) {
		Render render = modeRender(annotated, mode);
		if (render == null) {
			return null;
		}
		return render.controlPage();
	}

	public static String heading(AnnotatedElement annotated, Class<? extends Mode> mode) {
		Render render = modeRender(annotated, mode);
		if (render == null) {
			return null;
		}
		return nonEmptyString(render.heading(), () -> joinStrings(camelCaseWords(name(annotated)), " "));
	}

	public static Class<?> modeEntity(AnnotatedElement annotated, Class<? extends Mode> mode) {
		Render render = modeRender(annotated, mode);

		Class<?> entity = (render != null) ? render.entity() : null;
		if (entity == Object.class) {
			entity = null;
		}

		return entity;
	}

	@SuppressWarnings("unchecked")
	protected static <T> Map<String, T> fieldMap(Class<?> entity, Function<Field, T> function) {

		Map<String, T> map = safeStream(classFields(entity)).map(field -> {
			T result = function.apply(field);
			logger.fine(() -> "field " + field + " result " + result);

			return (result != null) ? new Object[] { field, result } : null;
		}).filter(Objects::nonNull)
				.collect(linkedHashMapCollector(entry -> ((Field) entry[0]).getName(), entry -> (T) entry[1]));
		logger.fine(() -> "map " + map);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Control.Factory<?> controlFactory(AnnotatedElement annotated, Class<? extends Mode> mode) {
		Class<? extends Control.Factory> factoryClass = controlFactoryClass(annotated, mode);
		if (factoryClass == null) {
			return null;
		}

		Control.Factory<?> factory = unsafeGet(() -> {
			Object instance = factoryClass.getConstructor(AnnotatedElement.class, Class.class).newInstance(annotated,
					mode);
			return (Control.Factory) instance;
		});

		logger.fine(() -> "annotated " + annotated + " mode " + mode + " factory " + factory);

		return factory;
	}

	@SuppressWarnings("rawtypes")
	protected static Class<? extends Control.Factory> controlFactoryClass(AnnotatedElement annotated,
			Class<? extends Mode> mode) {
		Render render = modeRender(annotated, mode);

		Class<? extends Control.Factory> controlFactory = (render != null) ? render.controlFactory() : null;
		if (controlFactory == Control.Factory.class) {
			controlFactory = null;
		}

		if (controlFactory == null) {
			controlFactory = defaultControlFactoryClass(annotated, mode);
		}
		return controlFactory;
	}

	@SuppressWarnings("rawtypes")
	protected static Class<? extends Control.Factory> defaultControlFactoryClass(AnnotatedElement annotated,
			Class<? extends Mode> mode) {
		if (!(annotated instanceof Field)) {
			return DescriptionList.Factory.class;
		}

		Entity.Field field = modeField(annotated, mode);
		boolean identifier = (field != null) && field.identifier();

		boolean edit = (mode == EntityForm.class);

		if (identifier) {
			return edit ? Span.Factory.class : Anchor.Factory.class;
		}

		if (Iterable.class.isAssignableFrom(((Field) annotated).getType())) {
			return Table.Factory.class;
		}

		return edit ? Input.Factory.class : Span.Factory.class;
	}
}
