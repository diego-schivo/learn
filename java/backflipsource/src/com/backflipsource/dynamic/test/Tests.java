package com.backflipsource.dynamic.test;

import com.backflipsource.DefaultDynamicClass;
import com.backflipsource.dynamic.DynamicClass;
import com.backflipsource.dynamic.DynamicField;

public class Tests {

	protected static DynamicClass class1 = new DefaultDynamicClass(Greeting.class);

	public static void main(String[] args) {
		test1();
	}

	protected static void test1() {
		assert class1.getName() != null;
		assert class1.getName().equals("Greeting");
		assert class1.fields().findFirst().isPresent();

		Object instance = class1.newInstance();
		assert instance instanceof Greeting;
		Greeting greeting = (Greeting) instance;

		DynamicField field = class1.fields().findFirst().get();
		assert field.getName() != null;
		assert field.getName().equals("message");

		String message = field.getValue(instance);
		assert message != null;
		assert message.equals("Hello, World!");

		field.setValue(instance, "Hello, Dynamic!");
		assert greeting.getMessage() != null;
		assert greeting.getMessage().equals("Hello, Dynamic!");
	}

	public static class Greeting {

		private String message = "Hello, World!";

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
