package com.backflipsource.servlet;

import static com.backflipsource.Helpers.getArgumentTypes;
import static com.backflipsource.servlet.EntityContextListener.getViews;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class Table extends Control {

	protected String uri;

	protected List<?> items;

	protected Map<String, Control.Factory> factories;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}

	public Map<String, Control.Factory> getFactories() {
		return factories;
	}

	public void setFactories(Map<String, Control.Factory> factories) {
		this.factories = factories;
	}

	@SuppressWarnings("rawtypes")
	public static class Factory extends Control.Factory<Table> {

		public Factory(Field field, StringConverter converter) {
			super(Table.class, field, converter);
		}

		@Override
		public Table control(Object object) {
			Table control = super.control(object);
			Class<?> type = (Class<?>) getArgumentTypes(field.getGenericType()).get(0);
			EntityView view = getViews().get(type.getName());
			control.setUri(view.getUri());
			control.setItems((List<?>) getFieldValue(object));
			control.setFactories(view.controlFactories(View.List.class));
			return control;
		}
	}
}
