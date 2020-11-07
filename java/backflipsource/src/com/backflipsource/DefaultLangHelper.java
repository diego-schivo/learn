package com.backflipsource;

import static com.backflipsource.Helpers.linkedHashMapCollector;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeStream;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class DefaultLangHelper implements LangHelper {

	protected static String EMPTY_STRING = "";

	protected static String GETTER_PREFIX = "get";

	protected static String SETTER_PREFIX = "set";

	@Override
	public String emptyString() {
		return EMPTY_STRING;
	}

	@Override
	public boolean emptyString(String string) {
		if (string == null) {
			return true;
		}
		return string.length() == 0;
	}

	@Override
	public String safeString(Object object) {
		String string = unsafeString(object);
		if (string == null) {
			return emptyString();
		}
		return string;
	}

	@Override
	public String unsafeString(Object object) {
		return Objects.toString(object, null);
	}

	@Override
	public String nonEmptyString(String string1, String string2) {
		if (!emptyString(string1)) {
			return string1;
		}
		return string2;
	}

	@Override
	public String trimString(String string) {
		if (string == null) {
			return null;
		}
		return string.trim();
	}

	@Override
	public String capitalizeString(String string) {
		if (emptyString(string)) {
			return string;
		}
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

	@Override
	public String[] splitString(String string, int character) {
		if (string == null) {
			return null;
		}
		List<String> list = new ArrayList<>();
		int index0 = 0;
		for (;;) {
			int index = string.indexOf(character, index0);
			if (index == -1) {
				list.add(string.substring(index0));
				break;
			}
			list.add(string.substring(index0, index));
			index0 = index + 1;
		}
		return list.toArray(String[]::new);
	}

	@Override
	public <T> T nonNullInstance(T t1, T t2) {
		if (t1 != null) {
			return t1;
		}
		return t2;
	}

	@Override
	public <T> T nonNullInstance(T t1, Supplier<T> t2) {
		if (t1 != null) {
			return t1;
		}
		return t2.get();
	}

	@Override
	public List<Field> getFields(Class<?> class1) {
		if (class1 == null) {
			return emptyList();
		}
		Field[] fields = class1.getDeclaredFields();
		return safeStream(fields).filter(field -> !Modifier.isStatic(field.getModifiers())).collect(toList());
	}

	@Override
	public String getGetterName(String field) {
		if (emptyString(field)) {
			return null;
		}
		return GETTER_PREFIX + capitalizeString(field);
	}

	@Override
	public String getSetterName(String field) {
		if (emptyString(field)) {
			return null;
		}
		return SETTER_PREFIX + capitalizeString(field);
	}

	@Override
	public Method getGetter(Field field) {
		if (field == null) {
			return null;
		}
		String name = getGetterName(field.getName());
		return safeGet(() -> field.getDeclaringClass().getMethod(name));
	}

	@Override
	public Method getSetter(Field field) {
		if (field == null) {
			return null;
		}
		String name = getSetterName(field.getName());
		return safeGet(() -> field.getDeclaringClass().getMethod(name));
	}

	@Override
	public Map<String, Method> getGetters(Class<?> class1) {
		if (class1 == null) {
			return emptyMap();
		}
		List<Field> fields = getFields(class1);
		return safeStream(fields).map(field -> {
			String getterName = getGetterName(field.getName());
			Method getter = safeGet(() -> class1.getMethod(getterName));
			if (getter == null) {
				return null;
			}
			return new Object[] { field.getName(), getter };
		}).filter(Objects::nonNull)
				.collect(linkedHashMapCollector(array -> (String) array[0], array -> (Method) array[1]));
	}

	@Override
	public Map<String, Method> getSetters(Class<?> class1) {
		if (class1 == null) {
			return emptyMap();
		}
		List<Field> fields = getFields(class1);
		return safeStream(fields).map(field -> {
			String setterName = getSetterName(field.getName());
			Method setter = safeStream(class1.getMethods())
					.filter(method -> Objects.equals(method.getName(), setterName)).findFirst().orElse(null);
			if (setter == null) {
				return null;
			}
			return new Object[] { field.getName(), setter };
		}).filter(Objects::nonNull)
				.collect(linkedHashMapCollector(array -> (String) array[0], array -> (Method) array[1]));
	}
}