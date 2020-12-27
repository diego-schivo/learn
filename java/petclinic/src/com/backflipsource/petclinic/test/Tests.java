package com.backflipsource.petclinic.test;

import static com.backflipsource.Helpers.linkedHashSet;
import static java.util.Collections.singletonList;

import java.util.Collection;
import java.util.List;

import com.backflipsource.Control;
import com.backflipsource.dynamic.DefaultDynamicClass;
import com.backflipsource.petclinic.Owner;
import com.backflipsource.ui.Anchor;
import com.backflipsource.ui.DefaultEntityUI;
import com.backflipsource.ui.EntityList;
import com.backflipsource.ui.Table;
import com.backflipsource.ui.spec.EntityUI;

public class Tests {

	protected static EntityUI ui = new DefaultEntityUI(() -> null,
			linkedHashSet("com.backflipsource.ui", "com.backflipsource.petclinic"), null);

	public static void main(String[] args) {
		test1();
	}

	protected static void test1() {
		DefaultDynamicClass entity = new DefaultDynamicClass(Owner.class);

		Control.Factory<?> factory = ui.resource(entity).controlFactory(entity, EntityList.class);
		assert factory != null;
		assert factory instanceof Table.Factory;
		Table.Factory tableFactory = (Table.Factory) factory;

		Owner owner = new Owner();
		owner.setId(123);

		Table table = tableFactory.create(singletonList(owner));
		assert table != null;

		List<?> items = table.getItems();
		assert items != null;
		assert items.size() == 1;
		assert items.get(0) == owner;

		Collection<Control.Factory<?>> factories = table.getFactories();
		assert factories != null;
		assert !factories.isEmpty();

		Control.Factory<?> factory2 = factories.iterator().next();
		assert factory2 != null;

		Control control = factory2.create(items.get(0));
		assert control != null;
		assert control.getClass() == Anchor.class;
		assert control.getParent() == table;
		assert control.getValue() != null;
		assert control.getValue().equals(owner.getId().toString());
	}
}
