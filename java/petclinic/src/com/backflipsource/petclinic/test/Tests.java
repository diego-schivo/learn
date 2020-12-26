package com.backflipsource.petclinic.test;

import static com.backflipsource.Helpers.getFieldValue;

import java.lang.annotation.Annotation;
import java.util.Objects;

import com.backflipsource.Control;
import com.backflipsource.DefaultDynamicClass;
import com.backflipsource.petclinic.Owner;
import com.backflipsource.ui.Anchor;
import com.backflipsource.ui.DefaultEntityResource;
import com.backflipsource.ui.Entity;
import com.backflipsource.ui.Table;
import com.backflipsource.ui.spec.EntityResource;

public class Tests {

	public static void main(String[] args) {
		// test1();
		foo();
	}

	@SuppressWarnings("rawtypes")
	protected static void test1() {
		EntityResource entityView = new DefaultEntityResource(new DefaultDynamicClass(Owner.class), null);
		// Table table = new Table.Factory(entityView).create(singletonList(new
		// Owner()));
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

	public static void foo() {
		Entity annotation = Owner.class.getAnnotation(Entity.class);
		Class<? extends Annotation> type = annotation.annotationType();
		Class<? extends Entity> class1 = annotation.getClass();

		Object annotationType = getFieldValue(type, "annotationType", type.getClass());
		Object members = getFieldValue(annotationType, "members", annotationType.getClass());
		System.out.println("x");
	}
}
