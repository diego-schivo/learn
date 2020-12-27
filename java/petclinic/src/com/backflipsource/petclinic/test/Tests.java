package com.backflipsource.petclinic.test;

import static com.backflipsource.Helpers.linkedHashSet;
import static java.util.Collections.singletonList;

import java.util.Collection;
import java.util.List;

import com.backflipsource.Control;
import com.backflipsource.Control.Factory;
import com.backflipsource.dynamic.DefaultDynamicClass;
import com.backflipsource.petclinic.Owner;
import com.backflipsource.petclinic.OwnerFind;
import com.backflipsource.ui.AbstractEntityControl;
import com.backflipsource.ui.Anchor;
import com.backflipsource.ui.DefaultEntityUI;
import com.backflipsource.ui.EntityList;
import com.backflipsource.ui.Form;
import com.backflipsource.ui.Input;
import com.backflipsource.ui.Span;
import com.backflipsource.ui.Table;
import com.backflipsource.ui.spec.EntityUI;

public class Tests {

	protected static EntityUI ui = new DefaultEntityUI(() -> null,
			linkedHashSet("com.backflipsource.ui", "com.backflipsource.petclinic"), null);

	public static void main(String[] args) {
		testOwnerFind();
		testEntityList();
	}

	protected static void testOwnerFind() {
		DefaultDynamicClass entity = new DefaultDynamicClass(Owner.class);

		Control.Factory<?> factory = ui.resource(entity).controlFactory(entity, OwnerFind.class);
		assert factory != null;
		assert factory instanceof Form.Factory;
		Form.Factory formFactory = (Form.Factory) factory;

		Form form = formFactory.newControl(null, null);
		assert form != null;

		Collection<Control.Factory<?>> factories = form.childFactories();
		assert factories != null;
		assert !factories.isEmpty();

		Control.Factory<?> factory2 = factories.iterator().next();
		assert factory2 != null;

		Control control = factory2.newControl(null, form);
		assert control != null;
		assert control.getClass() == Input.class;
		assert control.getParent() == form;
		assert control.getValue() == null;
	}

	protected static void testEntityList() {
		Owner george = Owner.data.list().get(0);

		DefaultDynamicClass entity = new DefaultDynamicClass(george.getClass());

		Control.Factory<?> factory = ui.resource(entity).controlFactory(entity, EntityList.class);
		assert factory != null;
		assert factory instanceof Table.Factory;
		Table.Factory tableFactory = (Table.Factory) factory;

		Table table = tableFactory.newControl(singletonList(george), null);
		assert table != null;

		List<?> items = table.getItems();
		assert items != null;
		assert items.size() == 1;
		assert items.get(0) == george;

		Collection<Control.Factory<?>> factories = table.childFactories();
		assert factories != null;
		assert !factories.isEmpty();

		Control.Factory<?> factory2 = factories.iterator().next();
		assert factory2 != null;

		Control control = factory2.newControl(george, table);
		assert control != null;
		assert control.getClass() == Anchor.class;
		assert control.getParent() == table;
		assert control.getValue() != null;
		assert control.getValue().equals(george.getId().toString());

		Factory<?> factory3 = factories.stream()
				.filter(factory4 -> ((AbstractEntityControl.Factory<?>) factory4).getName().equals("pets")).findFirst()
				.orElse(null);
		assert factory3 != null;

		Control control2 = factory3.newControl(george, table);
		assert control2 != null;
		assert control2.getClass() == Span.class;
		assert control2.getParent() == table;
		assert control2.getValue() != null;
		assert control2.getValue().equals(george.getId().toString());
	}
}
