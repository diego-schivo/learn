package com.backflipsource.ui;

import static com.backflipsource.helper.Helper.camelCaseWords;
import static com.backflipsource.helper.Helper.joinStrings;
import static com.backflipsource.helper.Helper.logger;
import static com.backflipsource.helper.Helper.nonEmptyString;
import static com.backflipsource.helper.Helper.safeGet;
import static com.backflipsource.helper.Helper.stringWithoutPrefix;
import static com.backflipsource.helper.Helper.uncapitalizeString;
import static com.backflipsource.ui.DefaultEntityResource.controlPage;
import static com.backflipsource.ui.DefaultEntityResource.converter;
import static com.backflipsource.ui.DefaultEntityResource.heading;
import static com.backflipsource.ui.EntityServlet.entityUI;
import static java.util.function.Function.identity;
import static java.util.logging.Level.ALL;

import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Logger;

import com.backflipsource.DefaultControl;
import com.backflipsource.dynamic.DynamicAnnotated;
import com.backflipsource.dynamic.DynamicClass;
import com.backflipsource.dynamic.DynamicField;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI;
import com.backflipsource.ui.spec.EntityUI.Mode;

public abstract class AbstractEntityControl extends DefaultControl {

	private static Logger logger = logger(AbstractEntityControl.class, ALL);

	private String textKey;

//	private Collection<Control.Factory<?>> factories;

	public EntityResource getResource() {
		return ((Factory<?>) factory).getResource();
	}

	public Class<? extends Mode> getMode() {
		return ((Factory<?>) factory).getMode();
	}

	@Override
	public String getTextKey() {
		if (textKey == null) {
			textKey = (getResource().getEntity().getName() + "."
					+ stringWithoutPrefix(((Factory<?>) factory).getMode().getSimpleName(), "Entity")).toLowerCase();
		}
		return textKey;
	}

	@Override
	public String text(String key) {
		ResourceBundle resourceBundle = entityUI().getResourceBundle();
		return nonEmptyString(safeGet(() -> resourceBundle.getString(key)), key);
	}

//	@Override
//	public Collection<Control.Factory<?>> childFactories() {
//		if (factories == null) {
//			Class<? extends Mode> mode = ((Factory<?>) factory).getMode();
//			factories = safeMap(getResource().controlFactories(mode)).values();
//			logger.fine(() -> "factories " + factories);
//		}
//		return factories;
//	}

	public static abstract class Factory<T extends AbstractEntityControl> extends DefaultControl.Factory<T> {

		private EntityUI ui;

		private Class<? extends Mode> mode;

		private EntityResource resource;

		public EntityUI getUI() {
//			if (ui == null) {
//				ui = getEntityUI();
//			}
			return ui;
		}

		public void setUI(EntityUI ui) {
			this.ui = ui;
		}

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

		public void init(EntityUI ui, DynamicAnnotated entityOrField, Class<? extends Mode> mode) {
			setUI(ui);
			setName(entityOrField.getName());
			setValueGetter(valueGetter(entityOrField));
			setConverter(
					(entityOrField instanceof DynamicField) ? converter((DynamicField) entityOrField, mode) : null);
			setPage(nonEmptyString(controlPage(entityOrField, mode), () -> "/"
					+ joinStrings(camelCaseWords(uncapitalizeString(getControl().getSimpleName())), "-") + ".jsp"));
			setHeading(heading(entityOrField, mode));
			setMode(mode);
			setResource(getUI().resource(entityOrField));
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
	}
}
