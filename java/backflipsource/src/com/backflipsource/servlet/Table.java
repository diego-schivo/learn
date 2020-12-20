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
