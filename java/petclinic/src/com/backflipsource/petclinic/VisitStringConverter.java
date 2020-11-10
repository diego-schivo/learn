package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.emptyString;
import static com.backflipsource.Helpers.safeStream;
import static java.lang.Integer.parseInt;

import java.util.Objects;

import com.backflipsource.servlet.StringConverter;

public class VisitStringConverter implements StringConverter<Visit> {

	@Override
	public String convertToString(Visit instance) {
		if (instance == null) {
			return null;
		}
		return instance.getId().toString();
	}

	@Override
	public Visit convertFromString(String string) {
		if (emptyString(string)) {
			return null;
		}
		Integer id = parseInt(string);
		return safeStream(Visit.list).filter(item -> Objects.equals(item.getId(), id)).findFirst().orElse(null);
	}

}
