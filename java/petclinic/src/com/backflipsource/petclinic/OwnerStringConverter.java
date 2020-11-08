package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.emptyString;
import static com.backflipsource.Helpers.safeStream;

import java.util.Objects;

import com.backflipsource.servlet.StringConverter;

public class OwnerStringConverter implements StringConverter<Owner> {

	@Override
	public String convertToString(Owner instance) {
		if (instance == null) {
			return null;
		}
		return instance.getId().toString();
	}

	@Override
	public Owner convertFromString(String string) {
		if (emptyString(string)) {
			return null;
		}
		int id = Integer.parseInt(string);
		return safeStream(Owner.list).filter(item -> Objects.equals(item.getId(), id)).findFirst().orElse(null);
	}

}
