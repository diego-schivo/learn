package com.backflipsource.ui;

import static com.backflipsource.Helpers.camelCaseWords;
import static com.backflipsource.Helpers.getArgumentTypes;
import static com.backflipsource.Helpers.joinStrings;
import static com.backflipsource.Helpers.listGet;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeMap;
import static com.backflipsource.Helpers.stringWithoutPrefix;
import static com.backflipsource.Helpers.uncapitalizeString;
import static com.backflipsource.servlet.EntityServlet.getEntityUI;
import static com.backflipsource.ui.DefaultEntityResource.controlPage;
import static com.backflipsource.ui.DefaultEntityResource.converter;
import static com.backflipsource.ui.DefaultEntityResource.heading;
import static java.util.function.Function.identity;
import static java.util.logging.Level.ALL;

import java.util.Collection;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Logger;

import com.backflipsource.AbstractControl;
import com.backflipsource.Control;
import com.backflipsource.dynamic.DynamicAnnotated;
import com.backflipsource.dynamic.DynamicClass;
import com.backflipsource.dynamic.DynamicField;
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
			textKey = (getResource().getEntity().getName() + "."
					+ stringWithoutPrefix(factory.getMode().getSimpleName(), "Entity")).toLowerCase();
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
			factories = safeMap(getResource().controlFactories(factory.getMode())).values();
			factories.forEach(factory -> factory.setParent(this));
			logger.fine(() -> "factories " + factories);
		}
		return factories;
	}

	@SuppressWarnings("rawtypes")
	public static abstract class Factory<T extends AbstractEntityControl> extends AbstractControl.Factory<T> {

		protected Class<? extends Mode> mode;

		protected EntityResource resource;

		public Class<? extends Mode> getMode() {
			return mode;
		}

		public void setMode(Class<? extends Mode> mode) {
			this.mode = mode;
		}

		public EntityResource getResource() {
			return resource;
		}

		public void setResource(EntityResource resource) {
			this.resource = resource;
		}

		public void init(DynamicAnnotated entityOrField, Class<? extends Mode> mode) {
			setName(entityOrField.getName());
			setValueGetter(valueGetter(entityOrField));
			setConverter(
					(entityOrField instanceof DynamicField) ? converter((DynamicField) entityOrField, mode) : null);
			setPage(nonEmptyString(controlPage(entityOrField, mode), () -> "/"
					+ joinStrings(camelCaseWords(uncapitalizeString(getControl().getSimpleName())), "-") + ".jsp"));
			setHeading(heading(entityOrField, mode));
			setMode(mode);
			setResource(resource(entityOrField));
			logger.fine(() -> "this = " + this);
		}

		protected static Function<Object, Object> valueGetter(DynamicAnnotated entityOrField) {
			if (entityOrField instanceof DynamicField) {
				return object -> {
					if (object == null) {
						return null;
					}
					return ((DynamicField) entityOrField).getValue(object);
				};
			}
			if (entityOrField instanceof DynamicClass) {
				return identity();
			}
			return null;
		}

		protected static EntityResource resource(DynamicAnnotated entityOrField) {
			String key;
			if (entityOrField instanceof DynamicClass) {
				key = ((DynamicClass) entityOrField).getFullName();
			} else if (entityOrField instanceof DynamicField) {
				Class<?> class1 = (Class<?>) listGet(getArgumentTypes(((DynamicField) entityOrField).getGenericType()),
						0);
				key = (class1 != null) ? class1.getName() : null;
			} else {
				key = null;
			}
			return (key != null) ? getEntityUI().getResources().get(key) : null;
		}
	}
}
