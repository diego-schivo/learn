package com.backflipsource.ui;

import static com.backflipsource.Helpers.getArgumentTypes;
import static com.backflipsource.Helpers.listGet;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeMap;
import static com.backflipsource.Helpers.stringWithoutPrefix;
import static com.backflipsource.servlet.EntityServlet.getEntityUI;
import static com.backflipsource.ui.DefaultEntityResource.controlPage;
import static com.backflipsource.ui.DefaultEntityResource.converter;
import static com.backflipsource.ui.DefaultEntityResource.heading;
import static java.util.function.Function.identity;
import static java.util.logging.Level.ALL;

import java.util.Collection;
import java.util.Map;
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

		public EntityResource getResource() {
			return resource;
		}

		public void setResource(EntityResource resource) {
			this.resource = resource;
		}

		public void init(DynamicAnnotated entityOrField, Class<? extends Mode> mode) {
			setName(entityOrField.getName());
			setGetValue(getValue(entityOrField));
			setConverter(converter(entityOrField, mode));
			setPage(nonEmptyString(controlPage(entityOrField, mode),
					() -> "/" + getControl().getSimpleName().toLowerCase() + ".jsp"));
			setHeading(heading(entityOrField, mode));
			setResource(resource(entityOrField));
		}

		protected static Function<Object, Object> getValue(DynamicAnnotated annotated) {
			if (annotated instanceof DynamicField) {
				return object -> {
					if (object == null) {
						return null;
					}
					return ((DynamicField) annotated).getValue(object);
				};
			}
			if (annotated instanceof DynamicClass) {
				return identity();
			}
			return null;
		}

		protected static EntityResource resource(DynamicAnnotated entityOrField) {
			DynamicClass entity;
			if (entityOrField instanceof DynamicClass) {
				entity = (DynamicClass) entityOrField;
			} else if (entityOrField instanceof DynamicField) {
				entity = (DynamicClass) listGet(getArgumentTypes(((DynamicField) entityOrField).getGenericType()), 0);
			} else {
				entity = null;
			}
			DynamicClass key = entity;
			return safeGet(() -> getEntityUI().getResources().get(entity.getFullName()));
		}
	}
}
