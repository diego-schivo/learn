package com.backflipsource.ui;

import java.util.List;

import com.backflipsource.Control;

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
		public Table newControl(Object target, Control parent) {
			Table control = super.newControl(target, parent);
			control.items = (List<?>) ((valueGetter != null) ? valueGetter.apply(target) : target);
			return control;
		}
	}
}
