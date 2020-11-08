package com.backflipsource.servlet;

import static com.backflipsource.Helpers.getArgumentTypes;
import static com.backflipsource.servlet.EntityContextListener.getViews;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class Grid extends Control {

	protected static String PAGE = "grid.jsp";

	protected String uri;

	protected List<?> items;

	protected Map<String, Control.Factory> factories;

	protected Grid(String name, List<String> values, String uri, List<?> items,
			Map<String, Control.Factory> factories) {
		super(name, values);
		this.uri = uri;
		this.items = items;
		this.factories = factories;
	}

	public String getUri() {
		return uri;
	}

	public List<?> getItems() {
		return items;
	}

	public Map<String, Control.Factory> getFactories() {
		return factories;
	}

	@Override
	public String getPage() {
		return PAGE;
	}

	@SuppressWarnings("rawtypes")
	public static class Factory extends Control.Factory {

		public Factory(Field field, StringConverter converter) {
			super(field, converter);
		}

		@Override
		public Control control(Object object) {
			Class<?> type = (Class<?>) getArgumentTypes(field.getGenericType()).get(0);
			EntityView view = getViews().get(type.getName());
			Map<String, Control.Factory> factories = view.controlFactories(View.List.class);
			return new Grid(name(), values(object), view.getUri(), (List<?>) getFieldValue(object), factories);
		}
	}
}
