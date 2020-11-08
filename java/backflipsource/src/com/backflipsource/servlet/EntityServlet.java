package com.backflipsource.servlet;

import static com.backflipsource.Helpers.forwardServletRequest;
import static com.backflipsource.Helpers.getFields;
import static com.backflipsource.Helpers.getGetter;
import static com.backflipsource.Helpers.getSetters;
import static com.backflipsource.Helpers.linkedHashMapCollector;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.servlet.EntityContextListener.getServlets;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.servlet.Control.Factory;

@SuppressWarnings("serial")
public class EntityServlet extends HttpServlet {

	protected String mapping;
	protected Class<?> class1;
	protected List<Field> fields;
	protected Field idField;
	protected List<?> items;

	@Override
	public void init() throws ServletException {
		mapping = getServlets().get(getServletName()).getMappings().iterator().next();
		class1 = unsafeGet(() -> Class.forName(getServletName()));
		fields = getFields(class1);
		idField = safeStream(fields).filter(field -> field.getAnnotation(View.Field.class).identifier()).findFirst()
				.orElseGet(() -> fields.iterator().next());
		items = unsafeGet(() -> (List<?>) class1.getDeclaredField("list").get(null));
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		request.setAttribute("requestURI", requestURI);

		String substring = requestURI.substring(mapping.length());

		Class<?> view;
		if (substring.startsWith("/")) {
			view = substring.endsWith("/edit") ? View.Edit.class : View.Show.class;
			request.setAttribute("item", item(request));
		} else {
			view = View.List.class;
			request.setAttribute("items", items);
		}
		Map<String, Factory> controlFactories = controlFactories(view);
		request.setAttribute("controlFactories", controlFactories);

		View view2 = safeStream(class1.getAnnotationsByType(View.class))
				.filter(item -> safeList(item.value()).contains(view)).findFirst().orElse(null);
		String path = nonEmptyString(view2 != null ? view2.page() : null,
				"/" + view.getSimpleName().toLowerCase() + ".jsp");
		forwardServletRequest(path, request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Object item = item(request);

		Map<String, StringConverter<?>> converters = converters(View.Edit.class);
		Map<String, Method> setters = getSetters(class1);

		safeStream(fields).filter(field -> !Objects.equals(field, idField)).forEach(field -> {
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
			unsafeRun(() -> setters.get(field.getName()).invoke(item, value));
		});

		response.sendRedirect(request.getRequestURI());
	}

	protected Object item(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		String substring = requestURI.substring(mapping.length());

		String id;
		if (substring.startsWith("/")) {
			int end = substring.indexOf('/', 1);
			id = (end == -1) ? substring.substring(1) : substring.substring(1, end);
		} else {
			id = request.getParameter(idField.getName());
		}
		return safeStream(items)
				.filter(item -> Objects.equals(safeGet(() -> getGetter(idField).invoke(item)).toString(), id))
				.findFirst().orElse(null);
	}

	protected Map<String, Control.Factory> controlFactories(Class<?> view) {
		return viewFieldMap(view, (field, annotation) -> {
			Class<? extends Control> class1 = nonNullInstance(annotation != null ? annotation.control() : null,
					Control.class);
			if (Objects.equals(class1, Control.class)) {
				if (Objects.equals(view, View.Edit.class)) {
					class1 = annotation.identifier() ? Span.class : Input.class;
				} else {
					class1 = annotation.identifier() ? Anchor.class : Span.class;
				}
			}
			Class<? extends Control> class2 = class1;
			Class<?> class3 = safeGet(() -> Class.forName(class2.getName() + "$Factory"));

			Control.Factory factory = safeGet(
					() -> (Control.Factory) class3.getDeclaredConstructor(Field.class, StringConverter.class)
							.newInstance(field, converter(annotation)));
			return factory;
		});
	}

	protected Map<String, StringConverter<?>> converters(Class<?> view) {
		return viewFieldMap(view, (field, annotation) -> {
			return converter(annotation);
		});
	}

	@SuppressWarnings("unchecked")
	protected <T> Map<String, T> viewFieldMap(Class<?> view, BiFunction<Field, View.Field, T> function) {
		return safeStream(fields).map(field -> {
			View.Field[] annotations = field.getAnnotationsByType(View.Field.class);
			View.Field annotation = safeStream(annotations).filter(item -> safeList(item.view()).contains(view))
					.findFirst().orElseGet(() -> safeStream(annotations).filter(item -> item.view().length == 0)
							.findFirst().orElse(null));
			if (annotation == null) {
				return null;
			}
			T t = function.apply(field, annotation);
			return new Object[] { field.getName(), t };
		}).filter(Objects::nonNull).collect(linkedHashMapCollector(array -> (String) array[0], array -> (T) array[1]));
	}

	protected StringConverter<?> converter(View.Field annotation) {
		Class<? extends StringConverter<?>> class1 = nonNullInstance(annotation != null ? annotation.converter() : null,
				StringConverter.ForString.class);
		StringConverter<?> converter = safeGet(
				() -> (StringConverter<?>) class1.getDeclaredConstructor().newInstance());
		return converter;
	}
}
