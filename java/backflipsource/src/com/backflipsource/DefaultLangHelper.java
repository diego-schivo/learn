package com.backflipsource;

import static com.backflipsource.Helpers.linkedHashMapCollector;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static java.lang.Thread.currentThread;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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
	public String uncapitalizeString(String string) {
		if (emptyString(string)) {
			return string;
		}
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}

	@Override
	public String[] splitString(String string, String delimiter) {
		if (string == null) {
			return null;
		}
		List<String> list = new ArrayList<>();
		int index0 = 0;
		for (;;) {
			int index = string.indexOf(delimiter, index0);
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
	public String joinStrings(Stream<String> strings, String delimiter) {
		if (strings == null) {
			return null;
		}
		return strings.collect(joining(delimiter));
	}

	@Override
	public String stringWithoutPrefix(String string, String prefix) {
		if (emptyString(string) || emptyString(prefix) || !string.startsWith(prefix)) {
			return string;
		}
		return string.substring(prefix.length());
	}

	@Override
	public String stringWithoutSuffix(String string, String suffix) {
		if (emptyString(string) || emptyString(suffix) || !string.endsWith(suffix)) {
			return string;
		}
		return string.substring(0, string.length() - suffix.length());
	}

	@Override
	public String stringBeforeLast(String string, String substr) {
		if (emptyString(string) || emptyString(substr)) {
			return string;
		}
		int index = string.lastIndexOf(substr);
		if (index == -1) {
			return null;
		}
		return string.substring(0, index);
	}

	@Override
	public String stringAfterLast(String string, String substr) {
		if (emptyString(string) || emptyString(substr)) {
			return string;
		}
		int index = string.lastIndexOf(substr);
		if (index == -1) {
			return null;
		}
		return string.substring(index + substr.length());
	}

	@Override
	public String camelCaseString(String[] words) {
		if (words == null) {
			return null;
		}
		return Arrays.stream(words).filter(not(this::emptyString)).map(word -> capitalizeString(word.toLowerCase()))
				.collect(joining());
	}

	private static Pattern capitalizedLetterOrEnd = Pattern.compile("[A-Z]|$");

	@Override
	public String[] camelCaseWords(String string) {
		if (string == null) {
			return null;
		}
		Matcher matcher = capitalizedLetterOrEnd.matcher(string);
		List<String> list = new ArrayList<>();
		int index = 0;
		int length = string.length();
		while (index < length && matcher.find(index + 1)) {
			int index2 = matcher.start();
			String substring = string.substring(index, index2);
			if (index != 0) {
				substring = uncapitalizeString(substring);
			}
			list.add(substring);
			index = index2;
		}
		return list.toArray(new String[list.size()]);
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
	public List<Field> classFields(Class<?> class1) {
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
		List<Field> fields = classFields(class1);
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
		List<Field> fields = classFields(class1);
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

	@Override
	public Stream<Class<?>> getClasses(String packageName) {
		ClassLoader classLoader = currentThread().getContextClassLoader();
		Enumeration<URL> resources = unsafeGet(() -> classLoader.getResources(packageName.replace('.', '/')));
		Stream<Path> paths = safeStream(resources).map(resource -> unsafeGet(() -> resource.toURI())).map(Paths::get);
		return paths.map(path -> getClasses(path, packageName)).flatMap(identity());
	}

	private static Stream<Class<?>> getClasses(Path path, String packageName) {
		if (!Files.isDirectory(path)) {
			return Stream.empty();
		}
		Stream<Path> entries = unsafeGet(() -> Files.list(path));
		return entries.map(entry -> {
			String name = packageName + "." + entry.getFileName();
			if (name.endsWith(".class")) {
				return Stream.of(unsafeGet(() -> Class.forName(name.substring(0, name.lastIndexOf('.')))));
			}
			return getClasses(entry, name);
		}).flatMap(identity());
	}

	@Override
	public List<Type> getArgumentTypes(Type type) {
		ParameterizedType type2 = (type instanceof ParameterizedType) ? (ParameterizedType) type : null;
		if (type2 == null) {
			return emptyList();
		}
		return safeList(type2.getActualTypeArguments());
	}
}
