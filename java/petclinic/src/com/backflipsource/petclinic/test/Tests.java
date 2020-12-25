package com.backflipsource.petclinic.test;

import static java.util.Collections.singletonList;

import java.util.Objects;

import com.backflipsource.Control;
import com.backflipsource.petclinic.Owner;
import com.backflipsource.ui.Anchor;
import com.backflipsource.ui.DefaultEntityResource;
import com.backflipsource.ui.Table;
import com.backflipsource.ui.spec.EntityResource;

public class Tests {

	public static void main(String[] args) {
		test1();
	}

	@SuppressWarnings("rawtypes")
	protected static void test1() {
		EntityResource entityView = new DefaultEntityResource(Owner.class, null);
		// Table table = new Table.Factory(entityView).create(singletonList(new Owner()));
		Table table = null;
		assert table != null;

		Control.Factory factory = (Control.Factory) table.getFactories().iterator().next();
		assert factory != null;

		Control control = factory.create(table.getItems().get(0));
		assert control != null;
		assert Objects.equals(control.getClass(), Anchor.class);
		// assert control.getEntityView() == null;
		assert Objects.equals(control.getParent(), table);
	}
}
