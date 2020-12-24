package com.backflipsource.servlet;

import java.lang.reflect.Field;
import java.util.List;

public class Table extends AbstractControl {

	protected List<?> items;

	public List<?> getItems() {
		return items;
	}

	@Override
	protected Class<?> getView() {
		return View.List.class;
	}

	public static class Factory extends AbstractControl.Factory<Table> {

		public Factory(Field field, Class<?> view) {
			super(Table.class, field, view);
		}

		public Factory(EntityView entityView) {
			super(Table.class, entityView);
		}

		@Override
		public Table create(Object target) {
			Table control = super.create(target);
			control.items = (List<?>) ((getValue != null) ? getValue.apply(target) : target);
			return control;
		}
	}
}
