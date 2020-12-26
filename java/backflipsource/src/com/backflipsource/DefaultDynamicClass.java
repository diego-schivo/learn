package com.backflipsource;

import static com.backflipsource.Helpers.classFields;
import static com.backflipsource.Helpers.collectionFill;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.backflipsource.dynamic.AbstractDynamicAnnotated;
import com.backflipsource.dynamic.DynamicClass;
import com.backflipsource.dynamic.DynamicField;

public class DefaultDynamicClass extends AbstractDynamicAnnotated implements DynamicClass {

	protected Class<?> target;

	protected String fullName;

	protected List<DynamicField> fields = new ArrayList<>();

	public DefaultDynamicClass() {
	}

	public DefaultDynamicClass(Class<?> target) {
		setTarget(target);
	}

	public void setTarget(Class<?> target) {
		this.target = target;
		fullName = target.getName();
		name = target.getSimpleName();
		collectionFill(annotations, safeStream(target.getAnnotations()).map(DefaultDynamicAnnotation::new));
		collectionFill(fields, classFields(target).map(DefaultDynamicField::new));
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	@Override
	public Stream<DynamicField> fields() {
		return safeStream(fields);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T newInstance() {
		return unsafeGet(() -> (T) target.getConstructor().newInstance());
	}

	@Override
	public String toString() {
		return "DefaultDynamicClass(name=" + name + ")";
	}
}
