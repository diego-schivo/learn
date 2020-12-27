package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.string;
import static java.util.Collections.singleton;

import java.util.Set;
import java.util.stream.Stream;

import com.backflipsource.dynamic.DefaultDynamicClass;
import com.backflipsource.dynamic.DynamicField;

public class OwnerFilter extends DefaultDynamicClass {

	public OwnerFilter() {
		super(Owner.class);
	}

	private static Set<String> includedFields = singleton("lastName");

	@Override
	public Stream<DynamicField> fields() {
		return super.fields().filter(field -> includedFields.contains(string(field.getName())));
	}
}
