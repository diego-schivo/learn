package com.backflipsource;

import java.lang.reflect.Field;

public class DefaultDynamicField implements DynamicField {

	protected Field target;

	public DefaultDynamicField() {
	}

	public DefaultDynamicField(Field target) {
		this.target = target;
	}

	public void setTarget(Field target) {
		this.target = target;
	}
}