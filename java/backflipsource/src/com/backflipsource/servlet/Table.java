package com.backflipsource.servlet;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class Table extends Control {

	protected List<?> items;

	protected Class<?> view;

	@SuppressWarnings("rawtypes")
	private Map<String, Control.Factory> factories;

	public List<?> getItems() {
		return items;
	}

	public Class<?> getView() {
		return view;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Control.Factory> getFactories() {
		if (factories == null) {
			factories = getEntityView().controlFactories(getView());
		}
		return factories;
	}

	public static class Factory extends Control.Factory<Table> {

		public Factory(Field field, View.Field annotation) {
			super(Table.class, field, annotation);
		}

		@Override
		public Table control(Object object) {
			Table control = super.control(object);
			control.items = (List<?>) getFieldValue(object);
			control.view = View.List.class;
			return control;
		}
	}
}
