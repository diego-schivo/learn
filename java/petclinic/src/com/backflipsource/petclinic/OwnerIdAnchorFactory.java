package com.backflipsource.petclinic;

import java.lang.reflect.Field;

import com.backflipsource.servlet.Anchor;
import com.backflipsource.servlet.StringConverter;

@SuppressWarnings("rawtypes")
public class OwnerIdAnchorFactory extends Anchor.Factory {

	public OwnerIdAnchorFactory(Field field, StringConverter converter) {
		super(field, converter);
	}

	@Override
	public Anchor control(Object object) {
		Anchor control = super.control(object);
		control.setPage("owner-id-anchor.jsp");
		return control;
	}
}
