package com.backflipsource.menagerie;

import static com.backflipsource.Helpers.emptyString;
import static com.backflipsource.Helpers.safeStream;

import java.util.Objects;

import com.backflipsource.form.StringConverter;

public class OwnerStringConverter implements StringConverter<Owner> {

	@Override
	public String convertToString(Owner instance) {
		if (instance == null) {
			return null;
		}
		return instance.getName();
	}

	@Override
	public Owner convertFromString(String string) {
		if (emptyString(string)) {
			return null;
		}
		return safeStream(Owner.list).filter(item -> Objects.equals(item.getName(), string)).findFirst().orElse(null);
	}

}
