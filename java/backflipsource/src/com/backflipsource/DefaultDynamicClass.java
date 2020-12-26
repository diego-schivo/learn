package com.backflipsource;

import static com.backflipsource.Helpers.classFields;
import static com.backflipsource.Helpers.safeStream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DefaultDynamicClass implements DynamicClass {

	protected Class<?> target;

	protected List<DefaultDynamicField> fields = new ArrayList<>();

	public void setTarget(Class<?> target) {
		this.target = target;
		fields.clear();
		classFields(target).map(DefaultDynamicField::new).forEach(fields::add);
	}

	@Override
	public Stream<? extends DynamicField> fields() {
		return safeStream(fields);
	}
}
