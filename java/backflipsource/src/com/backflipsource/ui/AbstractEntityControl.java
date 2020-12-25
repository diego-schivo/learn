package com.backflipsource.ui;

import static com.backflipsource.Helpers.getArgumentTypes;
import static com.backflipsource.Helpers.getGetter;
import static com.backflipsource.Helpers.listGet;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeMap;
import static com.backflipsource.Helpers.stringWithoutPrefix;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.servlet.EntityServlet.getEntityUI;
import static com.backflipsource.ui.DefaultEntityResource.controlPage;
import static com.backflipsource.ui.DefaultEntityResource.converter;
import static com.backflipsource.ui.DefaultEntityResource.heading;
import static java.util.function.Function.identity;
import static java.util.logging.Level.ALL;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Logger;

import com.backflipsource.AbstractControl;
import com.backflipsource.Control;
import com.backflipsource.servlet.StringConverter;
import com.backflipsource.ui.AbstractEntityControl.Factory;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI.Mode;

public abstract class AbstractEntityControl<F extends Factory<?>> extends AbstractControl<F> {

	private static Logger logger = logger(AbstractEntityControl.class, ALL);

	private String textKey;

	private Collection<Control.Factory<?>> factories;

	public EntityResource getResource() {
		return factory.resource;
	}

	@Override
	public String getTextKey() {
		if (textKey == null) {
			textKey = (getResource().getEntity().getSimpleName() + "."
					+ stringWithoutPrefix(getMode().getSimpleName(), "Entity")).toLowerCase();
		}
		return textKey;
	}

	@Override
	public String text(String key) {
		ResourceBundle resourceBundle = getEntityUI().getResourceBundle();
		return nonEmptyString(safeGet(() -> resourceBundle.getString(key)), key);
	}

	@Override
	public Collection<Control.Factory<?>> getFactories() {
		if (factories == null) {
			Map<String, Control.Factory<?>> map = safeGet(() -> getResource().controlFactories(getMode()));
			factories = safeMap(map).values();
			factories.forEach(factory -> factory.setParent(this));
			logger.fine(() -> "factories " + factories);
		}
		return factories;
	}

	protected abstract Class<? extends Mode> getMode();

	@SuppressWarnings("rawtypes")
	public static abstract class Factory<T extends AbstractEntityControl> extends AbstractControl.Factory<T> {

		protected EntityResource resource;

		protected Factory(Class<T> control, AnnotatedElement annotated, Class<? extends Mode> mode) {
			super(control, name(annotated), extracted(annotated), converter(annotated, mode),
					controlPage(annotated, mode), heading(annotated, mode));
			Class<?> entity;
			if (annotated instanceof Class) {
				entity = (Class<?>) annotated;
			} else if (annotated instanceof Field) {
				entity = (Class<?>) listGet(getArgumentTypes(((Field) annotated).getGenericType()), 0);
			} else {
				entity = null;
			}
			Class<?> key = entity;
			resource = safeGet(() -> getEntityUI().getResources().get(key));
		}

		protected Factory(Class<T> control, String name, Function<Object, Object> getValue,
				StringConverter valueConverter, String page, String heading, EntityResource resource) {
			super(control, name, getValue, valueConverter, page, heading);
			this.resource = resource;
		}

		protected static Function<Object, Object> extracted(AnnotatedElement annotated) {
			if (annotated instanceof Field) {
				return object -> {
					if (object == null) {
						return null;
					}
					return unsafeGet(() -> getGetter((Field) annotated).invoke(object));
				};
			}
			if (annotated instanceof Class) {
				return identity();
			}
			return null;
		}
	}
}
