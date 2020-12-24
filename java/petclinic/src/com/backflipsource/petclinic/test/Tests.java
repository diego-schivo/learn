package com.backflipsource.petclinic.test;

import static java.util.Collections.singletonList;

import java.util.Objects;

import com.backflipsource.petclinic.Owner;
import com.backflipsource.servlet.Anchor;
import com.backflipsource.servlet.Control;
import com.backflipsource.servlet.DefaultEntityView;
import com.backflipsource.servlet.EntityView;
import com.backflipsource.servlet.Table;

public class Tests {

	public static void main(String[] args) {
		test1();
	}

	@SuppressWarnings("rawtypes")
	protected static void test1() {
		EntityView entityView = new DefaultEntityView(Owner.class);
		Table table = new Table.Factory(entityView).create(singletonList(new Owner()));
		assert table != null;

		Control.Factory factory = table.getFactories().iterator().next();
		assert factory != null;

		Control control = factory.create(table.getItems().get(0));
		assert control != null;
		assert Objects.equals(control.getClass(), Anchor.class);
		assert control.getEntityView() == null;
		assert Objects.equals(control.getParent(), table);
	}
}
