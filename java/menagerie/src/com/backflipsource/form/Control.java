package com.backflipsource.form;
import java.lang.reflect.Field;

public abstract class Control {

	protected Field field;

	protected Control(Field field) {
		this.field = field;
	}

	public Field getField() {
		return field;
	}

	public abstract String getPage();
}
