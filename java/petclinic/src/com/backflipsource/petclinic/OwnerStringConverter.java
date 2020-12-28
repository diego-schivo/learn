package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.emptyString;
import static com.backflipsource.Helpers.safeStream;
import static java.lang.Integer.parseInt;

import java.util.Objects;

import com.backflipsource.ui.StringConverter;

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
		int id = parseInt(string);
		return safeStream(Owner.data.list()).filter(item -> Objects.equals(item.getId(), id)).findFirst().orElse(null);
	}

}
