package com.backflipsource.menagerie;

import static com.backflipsource.Helpers.forwardServletRequest;
import static com.backflipsource.Helpers.getFieldNames;
import static com.backflipsource.Helpers.getSetterConsumers;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.trimString;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.DefaultLangHelper;
import com.backflipsource.DefaultServletHelper;
import com.backflipsource.DefaultUtilHelper;
import com.backflipsource.Helpers;
import com.backflipsource.form.Control;
import com.backflipsource.form.FormField;
import com.backflipsource.form.Input;
import com.backflipsource.form.StringConverter;

@WebServlet("/index.html")
public class Index extends HttpServlet {

	static {
		Helpers.langHelper = new DefaultLangHelper();
		Helpers.utilHelper = new DefaultUtilHelper();
		Helpers.servletHelper = new DefaultServletHelper();
	}

	Class<?> class1 = Pet.class;
	// Class<?> class1 = Owner.class;
	List<?> items = safeGet(() -> (List<?>) class1.getDeclaredField("list").get(null));

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setAttribute("items", items);

		List<String> fields = getFieldNames(class1);
		request.setAttribute("fields", fields);
		request.setAttribute("controls", controls(fields));
		request.setAttribute("converters", converters(fields));

		forwardServletRequest("/index.jsp", request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Object object = safeStream(items).filter(item -> Objects
				.equals(safeGet(() -> class1.getMethod("getName").invoke(item)), request.getParameter("_name")))
				.findFirst().orElse(null);

		List<String> fields = getFieldNames(class1);
		Map<String, StringConverter<?>> converters = converters(fields);

		Map<String, BiConsumer<Object, Object>> setters = getSetterConsumers(class1);
		safeStream(request.getParameterNames()).forEach(name -> {
			BiConsumer<Object, Object> setter = setters.get(name);
			if (setter != null) {
				String parameter = request.getParameter(name);
				String string = trimString(parameter);
				Object value = converters.get(name).convertFromString(string);
				setter.accept(object, value);
			}
		});

		response.sendRedirect(request.getContextPath());
	}

	protected Map<String, Control> controls(List<String> fields) {
		return formFieldMap(fields, (field, annotation) -> {
			Class<? extends Control> class1 = nonNullInstance(annotation != null ? annotation.control() : null,
					Input.class);
			Control control = safeGet(() -> (Control) class1.getDeclaredConstructor(Field.class).newInstance(field));
			return control;
		});
	}

	protected Map<String, StringConverter<?>> converters(List<String> fields) {
		return formFieldMap(fields, (field, annotation) -> {
			Class<? extends StringConverter<?>> class1 = nonNullInstance(
					annotation != null ? annotation.converter() : null, StringConverter.Identity.class);
			StringConverter<?> converter = safeGet(
					() -> (StringConverter<?>) class1.getDeclaredConstructor().newInstance());
			return converter;
		});
	}

	protected <T> Map<String, T> formFieldMap(List<String> fields, BiFunction<Field, FormField, T> function) {
		return safeStream(fields).map(name -> {
			Field field = safeGet(() -> class1.getDeclaredField(name));
			FormField formField = field.getAnnotation(FormField.class);
			T t = function.apply(field, formField);
			return new Object[] { name, t };
		}).filter(Objects::nonNull).collect(toMap(array -> (String) array[0], array -> (T) array[1]));
	}
}
