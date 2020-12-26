package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.safeString;
import static java.util.stream.Collectors.toSet;

import java.util.Set;
import java.util.stream.Stream;

import com.backflipsource.dynamic.DefaultDynamicClass;
import com.backflipsource.dynamic.DynamicField;

public class OwnerRow extends DefaultDynamicClass {

	public OwnerRow() {
		super(Owner.class);
	}

	private static Set<String> excludedFields = Stream.of("firstName", "lastName", "pets").collect(toSet());

	@Override
	public Stream<DynamicField> fields() {
		return super.fields().filter(field -> !excludedFields.contains(safeString(field.getName())));
	}
}
