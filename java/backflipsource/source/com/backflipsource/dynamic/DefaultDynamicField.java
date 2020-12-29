package com.backflipsource.dynamic;

import static com.backflipsource.helper.Helper.collectionFill;
import static com.backflipsource.helper.Helper.getFieldValue;
import static com.backflipsource.helper.Helper.repeatedAnnotations;
import static com.backflipsource.helper.Helper.safeStream;
import static com.backflipsource.helper.Helper.setFieldValue;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class DefaultDynamicField extends AbstractDynamicAnnotated implements DynamicField {

	protected Field target;

	protected Class<?> type;

	protected Type genericType;

	public DefaultDynamicField() {
	}

	public DefaultDynamicField(Field target) {
		setTarget(target);
	}

	public void setTarget(Field target) {
		this.target = target;
		name = target.getName();
		type = target.getType();
		genericType = target.getGenericType();
		collectionFill(annotations, safeStream(target.getAnnotations())
				.flatMap(annotation -> repeatedAnnotations(annotation)).map(DefaultDynamicAnnotation::new));
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public Type getGenericType() {
		return genericType;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue(Object instance) {
		return (T) getFieldValue(instance, getName(), instance.getClass());
		// return (T) unsafeGet(() -> getGetter(target).invoke(instance));
	}

	@Override
	public <T> void setValue(Object instance, T value) {
		setFieldValue(instance, getName(), value, instance.getClass());
		// unsafeRun(() -> getSetter(target).invoke(instance, value));
	}

	@Override
	public String toString() {
		return "DefaultDynamicField(name=" + name + ")";
	}
}
