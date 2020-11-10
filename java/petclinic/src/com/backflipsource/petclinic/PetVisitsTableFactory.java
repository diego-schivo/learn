package com.backflipsource.petclinic;

import java.lang.reflect.Field;

import com.backflipsource.servlet.StringConverter;
import com.backflipsource.servlet.Table;

@SuppressWarnings("rawtypes")
public class PetVisitsTableFactory extends Table.Factory {

	public PetVisitsTableFactory(Field field, StringConverter converter) {
		super(field, converter);
	}

	@Override
	public Table control(Object object) {
		Table control = super.control(object);
		control.setPage("pet-visits-table.jsp");
		return control;
	}
}
