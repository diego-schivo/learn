package com.backflipsource.petclinic.test;

import static com.backflipsource.Helpers.linkedHashSet;

import com.backflipsource.Control;
import com.backflipsource.dynamic.DefaultDynamicClass;
import com.backflipsource.dynamic.DynamicField;
import com.backflipsource.petclinic.Owner;
import com.backflipsource.ui.DefaultEntityUI;
import com.backflipsource.ui.EntityDetail;
import com.backflipsource.ui.EntityList;
import com.backflipsource.ui.Span;
import com.backflipsource.ui.Table;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI;

public class EntityResourceTest {

	protected static EntityUI ui = new DefaultEntityUI(() -> null,
			linkedHashSet("com.backflipsource.ui", "com.backflipsource.petclinic"), null);

	protected static EntityResource ownerResource = ui.resource(new DefaultDynamicClass(Owner.class));

	public static void main(String[] args) {
		testPetsControlFactory();
	}

	protected static void testPetsControlFactory() {
		DynamicField petsField = ownerResource.getEntity().fields().filter(field -> field.getName().equals("pets"))
				.findFirst().get();

		Control.Factory<?> factory;
		factory = ownerResource.controlFactory(petsField, EntityList.class);
		assert factory != null;
		assert factory instanceof Span.Factory;

		factory = ownerResource.controlFactory(petsField, EntityDetail.class);
		assert factory != null;
		assert factory instanceof Table.Factory;
	}
}
