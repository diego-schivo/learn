package com.backflipsource.ui;

import java.util.List;

public class Table extends AbstractEntityControl {

	protected List<?> items;

	public List<?> getItems() {
		return items;
	}

	public static class Factory extends AbstractEntityControl.Factory<Table> {

		public Factory() {
			setControl(Table.class);
		}

		@Override
		public Table create(Object target) {
			Table control = super.create(target);
			control.items = (List<?>) ((valueGetter != null) ? valueGetter.apply(target) : target);
			return control;
		}
	}
}
