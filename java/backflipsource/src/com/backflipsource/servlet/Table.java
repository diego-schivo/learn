package com.backflipsource.servlet;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class Table extends AbstractControl {

	protected List<?> items;

	@SuppressWarnings("rawtypes")
	private Collection<Control.Factory> factories;

	public List<?> getItems() {
		return items;
	}

	@SuppressWarnings("rawtypes")
	public Collection<Control.Factory> getFactories() {
		if (factories == null) {
			factories = getEntityView().controlFactories(View.List.class).values();
			factories.forEach(factory -> factory.setParent(this));
		}
		return factories;
	}

	public static class Factory extends AbstractFactory<Table> {

		public Factory(Field field, View.Field annotation) {
			super(Table.class, field, annotation);
		}

		public Factory(EntityView entityView) {
			super(Table.class, entityView);
		}

		@Override
		public Table control(Object target) {
			Table control = super.control(target);
			control.items = (List<?>) ((getValue != null) ? getValue.apply(target) : target);
			return control;
		}
	}
}
