package com.backflipsource.petclinic;

import java.lang.reflect.Field;

import com.backflipsource.servlet.StringConverter;
import com.backflipsource.servlet.Table;

@SuppressWarnings("rawtypes")
public class OwnerPetsTableFactory extends Table.Factory {

	public OwnerPetsTableFactory(Field field, StringConverter converter) {
		super(field, converter);
	}

	@Override
	public Table control(Object object) {
		Table control = super.control(object);
		control.setPage("owner-pets-table.jsp");
		return control;
	}
}
