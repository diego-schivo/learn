package com.backflipsource.menagerie;

import static com.backflipsource.Helpers.emptyString;
import static com.backflipsource.Helpers.safeStream;

import java.util.Objects;

import com.backflipsource.form.StringConverter;

public class PetStringConverter implements StringConverter<Pet> {

	@Override
	public String convertToString(Pet instance) {
		if (instance == null) {
			return null;
		}
		return instance.getName();
	}

	@Override
	public Pet convertFromString(String string) {
		if (emptyString(string)) {
			return null;
		}
		return safeStream(Pet.list).filter(item -> Objects.equals(item.getName(), string)).findFirst().orElse(null);
	}

}
