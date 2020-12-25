package com.backflipsource.ui;

import java.lang.reflect.AnnotatedElement;
import java.util.List;

import com.backflipsource.ui.Table.Factory;
import com.backflipsource.ui.spec.EntityUI.Mode;

public class Table extends AbstractEntityControl<Factory> {

	protected List<?> items;

	public List<?> getItems() {
		return items;
	}

	@Override
	protected Class<? extends Mode> getMode() {
		return EntityList.class;
	}

	public static class Factory extends AbstractEntityControl.Factory<Table> {

		public Factory(AnnotatedElement annotated, Class<? extends Mode> mode) {
			super(Table.class, annotated, mode);
		}

		@Override
		public Table create(Object target) {
			Table control = super.create(target);
			control.items = (List<?>) ((getValue != null) ? getValue.apply(target) : target);
			return control;
		}
	}
}
