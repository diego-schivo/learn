package com.backflipsource.menagerie;

import static com.backflipsource.Helpers.forwardServletRequest;
import static com.backflipsource.Helpers.getFields;
import static com.backflipsource.Helpers.getSetters;
import static com.backflipsource.Helpers.linkedHashMapCollector;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeRun;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.DefaultLangHelper;
import com.backflipsource.DefaultServletHelper;
import com.backflipsource.DefaultTomcatHelper;
import com.backflipsource.DefaultUtilHelper;
import com.backflipsource.Helpers;
import com.backflipsource.form.Control;
import com.backflipsource.form.FormField;
import com.backflipsource.form.Input;
import com.backflipsource.form.StringConverter;

@WebServlet("/index.html")
@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class Index extends HttpServlet {

	public static ThreadLocal<HttpServletRequest> REQUEST = new ThreadLocal<>();

	static {
		Helpers.langHelper = new DefaultLangHelper();
		Helpers.utilHelper = new DefaultUtilHelper();
		Helpers.servletHelper = new DefaultServletHelper();
		Helpers.tomcatHelper = new DefaultTomcatHelper();
	}

	// Class<?> class1 = Pet.class;
	Class<?> class1 = Owner.class;
	List<?> items = safeGet(() -> (List<?>) class1.getDeclaredField("list").get(null));

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		REQUEST.set(request);
		try {
			super.service(request, response);
		} finally {
			REQUEST.remove();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setAttribute("items", items);

		List<Field> fields = getFields(class1);
		request.setAttribute("controlFactories", controlFactories(fields));

		forwardServletRequest("/index.jsp", request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Object object = safeStream(items).filter(item -> Objects
				.equals(safeGet(() -> class1.getMethod("getName").invoke(item)), request.getParameter("_name")))
				.findFirst().orElse(null);

		List<Field> fields = getFields(class1);
		Map<String, StringConverter<?>> converters = converters(fields);
		Map<String, Method> setters = getSetters(class1);

		safeStream(fields).forEach(field -> {
			StringConverter<?> converter = converters.get(field.getName());
			boolean list = List.class.isAssignableFrom(field.getType());
			Object value;
			if (list) {
				String[] parameters = request.getParameterValues(field.getName());
				value = safeStream(parameters).map(converter::convertFromString).collect(toList());
			} else {
				String parameter = request.getParameter(field.getName());
				value = converter.convertFromString(parameter);
			}
			unsafeRun(() -> setters.get(field.getName()).invoke(object, value));
		});

		response.sendRedirect(nonEmptyString(request.getContextPath(), "/"));
	}

	protected Map<String, Control.Factory> controlFactories(List<Field> fields) {
		return formFieldMap(fields, (field, annotation) -> {
			Class<? extends Control> class1 = nonNullInstance(annotation != null ? annotation.control() : null,
					Input.class);
			Class<?> class2 = safeGet(() -> Class.forName(class1.getName() + "$Factory"));

			Class<? extends StringConverter<?>> class3 = nonNullInstance(
					annotation != null ? annotation.converter() : null, StringConverter.Identity.class);
			StringConverter<?> converter = safeGet(
					() -> (StringConverter<?>) class3.getDeclaredConstructor().newInstance());

			Control.Factory factory = safeGet(() -> (Control.Factory) class2
					.getDeclaredConstructor(Field.class, StringConverter.class).newInstance(field, converter));
			return factory;
		});
	}

	protected Map<String, StringConverter<?>> converters(List<Field> fields) {
		return formFieldMap(fields, (field, annotation) -> {
			Class<? extends StringConverter<?>> class3 = nonNullInstance(
					annotation != null ? annotation.converter() : null, StringConverter.Identity.class);
			StringConverter<?> converter = safeGet(
					() -> (StringConverter<?>) class3.getDeclaredConstructor().newInstance());
			return converter;
		});
	}

	protected <T> Map<String, T> formFieldMap(List<Field> fields, BiFunction<Field, FormField, T> function) {
		return safeStream(fields).map(field -> {
			FormField formField = field.getAnnotation(FormField.class);
			T t = function.apply(field, formField);
			return new Object[] { field.getName(), t };
		}).filter(Objects::nonNull).collect(linkedHashMapCollector(array -> (String) array[0], array -> (T) array[1]));
	}
}
