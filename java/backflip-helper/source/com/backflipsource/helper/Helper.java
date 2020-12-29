package com.backflipsource.helper;

import java.nio.file.Path;
import java.util.Collection;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.net.URL;
import java.util.function.Consumer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;

public class Helper {

	public static Iterable<String> javaCompilerOptions(Path currentDir, Path sourceDir, Path classDir, Path javacDir) {
		return StaticCompileHelper.javaCompilerOptions(currentDir, sourceDir, classDir, javacDir);
	}

	public static Collection<Path> collectSourceFiles(Path source) {
		return StaticCompileHelper.collectSourceFiles(source);
	}

	public static void callJavaCompilerTask(Iterable<String> options, Collection<Path> sourceFiles) {
		StaticCompileHelper.callJavaCompilerTask(options, sourceFiles);
	}

	public static void copyResourceFiles(Path source, Path javac) {
		StaticCompileHelper.copyResourceFiles(source, javac);
	}

	public static String string(Object object) {
		return StaticLangHelper.string(object);
	}

	public static String emptyString() {
		return StaticLangHelper.emptyString();
	}

	public static boolean emptyString(String string) {
		return StaticLangHelper.emptyString(string);
	}

	public static String unsafeString(Object object) {
		return StaticLangHelper.unsafeString(object);
	}

	public static String nonEmptyString(String string1, String string2) {
		return StaticLangHelper.nonEmptyString(string1, string2);
	}

	public static String nonEmptyString(String string1, Supplier<String> string2) {
		return StaticLangHelper.nonEmptyString(string1, string2);
	}

	public static String trimString(String string) {
		return StaticLangHelper.trimString(string);
	}

	public static String capitalizeString(String string) {
		return StaticLangHelper.capitalizeString(string);
	}

	public static String uncapitalizeString(String string) {
		return StaticLangHelper.uncapitalizeString(string);
	}

	public static String[] splitString(String string, String delimiter) {
		return StaticLangHelper.splitString(string, delimiter);
	}

	public static String joinStrings(Stream<String> strings, String delimiter) {
		return StaticLangHelper.joinStrings(strings, delimiter);
	}

	public static String stringWithoutPrefix(String string, String prefix) {
		return StaticLangHelper.stringWithoutPrefix(string, prefix);
	}

	public static String stringWithoutSuffix(String string, String suffix) {
		return StaticLangHelper.stringWithoutSuffix(string, suffix);
	}

	public static String substringBeforeFirst(String string, String substr) {
		return StaticLangHelper.substringBeforeFirst(string, substr);
	}

	public static String substringAfterFirst(String string, String substr) {
		return StaticLangHelper.substringAfterFirst(string, substr);
	}

	public static String substringBeforeLast(String string, String substr) {
		return StaticLangHelper.substringBeforeLast(string, substr);
	}

	public static String substringAfterLast(String string, String substr) {
		return StaticLangHelper.substringAfterLast(string, substr);
	}

	public static String camelCaseString(String[] words) {
		return StaticLangHelper.camelCaseString(words);
	}

	public static Stream<String> camelCaseWords(String string) {
		return StaticLangHelper.camelCaseWords(string);
	}

	public static <T> T nonNullInstance(T t1, T t2) {
		return StaticLangHelper.nonNullInstance(t1, t2);
	}

	public static <T> T nonNullInstance(T t1, Supplier<T> t2) {
		return StaticLangHelper.nonNullInstance(t1, t2);
	}

	public static Stream<Field> classFields(Class<?> class1) {
		return StaticLangHelper.classFields(class1);
	}

	public static String getGetterName(String field) {
		return StaticLangHelper.getGetterName(field);
	}

	public static String getSetterName(String field) {
		return StaticLangHelper.getSetterName(field);
	}

	public static Method getGetter(Field field) {
		return StaticLangHelper.getGetter(field);
	}

	public static Method getSetter(Field field) {
		return StaticLangHelper.getSetter(field);
	}

	public static Map<String, Method> getGetters(Class<?> class1) {
		return StaticLangHelper.getGetters(class1);
	}

	public static Map<String, Method> getSetters(Class<?> class1) {
		return StaticLangHelper.getSetters(class1);
	}

	public static Stream<Class<?>> packageClasses(String package1) {
		return StaticLangHelper.packageClasses(package1);
	}

	public static Stream<Class<?>> packageClasses(String package1, boolean subpackages) {
		return StaticLangHelper.packageClasses(package1, subpackages);
	}

	public static List<Type> getArgumentTypes(Type type) {
		return StaticLangHelper.getArgumentTypes(type);
	}

	public static Object getFieldValue(Object instance, String field, Class<?> class1) {
		return StaticLangHelper.getFieldValue(instance, field, class1);
	}

	public static void setFieldValue(Object instance, String field, Object value, Class<?> class1) {
		StaticLangHelper.setFieldValue(instance, field, value, class1);
	}

	public static <A extends Annotation> Stream<Entry<String, Method>> annotationMethods(A annotation) {
		return StaticLangHelper.annotationMethods(annotation);
	}

	public static <A extends Annotation> Stream<Entry<String, Object>> annotationEntries(A annotation) {
		return StaticLangHelper.annotationEntries(annotation);
	}

	public static Stream<Annotation> repeatedAnnotations(Annotation annotation) {
		return StaticLangHelper.repeatedAnnotations(annotation);
	}

	public static <A extends Annotation, T> T annotationTypeInstance(A annotation, Function<A, Class<? extends T>> getClass,
			Class<? extends T> defaultClass) {
		return StaticLangHelper.annotationTypeInstance(annotation, getClass, defaultClass);
	}

	public static String classEnclosingName(Class<?> class1) {
		return StaticLangHelper.classEnclosingName(class1);
	}

	public static String readResource(String name, Class<?> class1) {
		return StaticLangHelper.readResource(name, class1);
	}

	public static URL url(String spec) {
		return StaticNetHelper.url(spec);
	}

	public static void extractArchive(Path archive, Path directory) {
		StaticNioHelper.extractArchive(archive, directory);
	}

	public static void acceptDirectoryEntries(Path directory, String glob, Consumer<Path> consumer) {
		StaticNioHelper.acceptDirectoryEntries(directory, glob, consumer);
	}

	public static <T extends Collection<U>, U> T collection(T collection) {
		return StaticUtilHelper.collection(collection);
	}

	public static <T> boolean emptyArray(T[] array) {
		return StaticUtilHelper.emptyArray(array);
	}

	public static boolean emptyCollection(Collection<?> collection) {
		return StaticUtilHelper.emptyCollection(collection);
	}

	public static <T> T[] safeArray(Collection<T> collection, Class<T> class1) {
		return StaticUtilHelper.safeArray(collection, class1);
	}

	public static <T> T[] unsafeArray(Collection<T> collection, Class<T> class1) {
		return StaticUtilHelper.unsafeArray(collection, class1);
	}

	public static <T> List<T> safeList(T[] array) {
		return StaticUtilHelper.safeList(array);
	}

	public static <T> List<T> unsafeList(T[] array) {
		return StaticUtilHelper.unsafeList(array);
	}

	public static <T> Stream<T> safeStream(T[] array) {
		return StaticUtilHelper.safeStream(array);
	}

	public static <T> Stream<T> unsafeStream(T[] array) {
		return StaticUtilHelper.unsafeStream(array);
	}

	public static <T> Stream<T> safeStream(Collection<T> collection) {
		return StaticUtilHelper.safeStream(collection);
	}

	public static <T> Stream<T> unsafeStream(Collection<T> collection) {
		return StaticUtilHelper.unsafeStream(collection);
	}

	public static <T> Stream<T> safeStream(Enumeration<T> enumeration) {
		return StaticUtilHelper.safeStream(enumeration);
	}

	public static <T> Stream<T> unsafeStream(Enumeration<T> enumeration) {
		return StaticUtilHelper.unsafeStream(enumeration);
	}

	public static <T> Stream<T> safeStream(Iterable<T> iterable) {
		return StaticUtilHelper.safeStream(iterable);
	}

	public static <T> Stream<T> unsafeStream(Iterable<T> iterable) {
		return StaticUtilHelper.unsafeStream(iterable);
	}

	public static <T> Stream<T> safeStream(Iterator<T> iterator) {
		return StaticUtilHelper.safeStream(iterator);
	}

	public static <T> Stream<T> unsafeStream(Iterator<T> iterator) {
		return StaticUtilHelper.unsafeStream(iterator);
	}

	public static <K, V> Stream<Entry<K, V>> safeStream(Map<K, V> map) {
		return StaticUtilHelper.safeStream(map);
	}

	public static <K, V> Stream<Entry<K, V>> unsafeStream(Map<K, V> map) {
		return StaticUtilHelper.unsafeStream(map);
	}

	public static <T> Collector<T, ?, Set<T>> linkedHashSetCollector() {
		return StaticUtilHelper.linkedHashSetCollector();
	}

	public static <K, V> Map<K, V> safeMap(Map<K, V> map) {
		return StaticUtilHelper.safeMap(map);
	}

	public static <T, K, U> Collector<T, ?, Map<K, U>> linkedHashMapCollector(Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends U> valueMapper) {
		return StaticUtilHelper.linkedHashMapCollector(keyMapper, valueMapper);
	}

	public static void safeRun(RunnableThrowingException runnable) {
		StaticUtilHelper.safeRun(runnable);
	}

	public static void unsafeRun(RunnableThrowingException runnable) {
		StaticUtilHelper.unsafeRun(runnable);
	}

	public static <T> T safeGet(SupplierThrowingException<T> supplier) {
		return StaticUtilHelper.safeGet(supplier);
	}

	public static <T> T unsafeGet(SupplierThrowingException<T> supplier) {
		return StaticUtilHelper.unsafeGet(supplier);
	}

	public static <T, R> R safeApply(FunctionThrowingException<T, R> function, T t) {
		return StaticUtilHelper.safeApply(function, t);
	}

	public static <T, R> R unsafeApply(FunctionThrowingException<T, R> function, T t) {
		return StaticUtilHelper.unsafeApply(function, t);
	}

	public static <T, R> Function<T, R> safeFunction(FunctionThrowingException<T, R> function) {
		return StaticUtilHelper.safeFunction(function);
	}

	public static <T, R> Function<T, R> unsafeFunction(FunctionThrowingException<T, R> function) {
		return StaticUtilHelper.unsafeFunction(function);
	}

	public static Map<String, Function<Object, Object>> getGetterFunctions(Class<?> class1) {
		return StaticUtilHelper.getGetterFunctions(class1);
	}

	public static Map<String, BiConsumer<Object, Object>> getSetterConsumers(Class<?> class1) {
		return StaticUtilHelper.getSetterConsumers(class1);
	}

	public static Future<?>[] startExecutorService(ExecutorService executor, Callable<?>... tasks) {
		return StaticUtilHelper.startExecutorService(executor, tasks);
	}

	public static void stopExecutorService(ExecutorService executor, Future<?>... futures) {
		StaticUtilHelper.stopExecutorService(executor, futures);
	}

	public static void watchDirectories(Path start, Consumer<Path[]> consumer) {
		StaticUtilHelper.watchDirectories(start, consumer);
	}

	public static <T> T arrayGet(T[] array, int index) {
		return StaticUtilHelper.arrayGet(array, index);
	}

	public static <T> void arraySet(T[] array, int index, T value) {
		StaticUtilHelper.arraySet(array, index, value);
	}

	public static <T> T listGet(List<T> list, int index) {
		return StaticUtilHelper.listGet(list, index);
	}

	public static <T> T listSet(List<T> list, int index, T value) {
		return StaticUtilHelper.listSet(list, index, value);
	}

	public static <T> void collectionFill(Collection<T> collection, Stream<T> stream) {
		StaticUtilHelper.collectionFill(collection, stream);
	}

	public static <K, V> void mapFill(Map<K, V> map, Stream<Entry<K, V>> stream) {
		StaticUtilHelper.mapFill(map, stream);
	}

	public static Logger logger(Class<?> class1, Level level) {
		return StaticUtilHelper.logger(class1, level);
	}

	public static <T> Set<T> linkedHashSet(T... values) {
		return StaticUtilHelper.linkedHashSet(values);
	}

	public static <T> Iterator<T> iterator(BooleanSupplier hasNext, Supplier<T> next) {
		return StaticUtilHelper.iterator(hasNext, next);
	}

	public static <K, V> Map<K, V> linkedHashMap(Entry<K, V>... entries) {
		return StaticUtilHelper.linkedHashMap(entries);
	}

}
