package com.backflipsource.servlet;

import static com.backflipsource.Helpers.forwardServletRequest;
import static com.backflipsource.Helpers.getFields;
import static com.backflipsource.Helpers.getGetter;
import static com.backflipsource.Helpers.getSetters;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.servlet.EntityContextListener.getViews;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.servlet.Control.Factory;

@SuppressWarnings("serial")
public class EntityServlet extends HttpServlet {

	protected Class<?> class1;
	protected List<Field> fields;
	protected Field idField;
	protected List<?> items;
	protected Object item0;
	protected EntityView bar;

	@Override
	public void init() throws ServletException {
		class1 = unsafeGet(() -> Class.forName(getServletName()));
		fields = safeStream(getFields(class1)).filter(field -> field.getAnnotationsByType(View.Field.class).length > 0)
				.collect(toList());
		idField = safeStream(fields).filter(field -> field.getAnnotation(View.Field.class).identifier()).findFirst()
				// .orElseGet(() -> fields.iterator().next());
				.orElse(null);
		items = safeGet(() -> (List<?>) class1.getDeclaredField("list").get(null));
		item0 = safeGet(() -> class1.getDeclaredField("instance").get(null));
		bar = getViews().get(getServletName());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("requestURI", request.getRequestURI());
		request.setAttribute("uri", bar.getUri());

		Class<?> view;
		String requestURI = request.getRequestURI();
		String substring = requestURI.substring(bar.getUri().length());
		if (substring.startsWith("/")) {
			view = substring.endsWith("/edit") ? View.Edit.class : View.Show.class;
			request.setAttribute("item", item(request));
		} else if (items != null) {
			view = View.List.class;
			request.setAttribute("items", items);
		} else if (item0 != null) {
			view = View.Show.class;
			request.setAttribute("item", item0);
		} else {
			view = null;
		}
		Map<String, Factory> controlFactories = bar.controlFactories(view);
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

		Map<String, StringConverter<?>> converters = bar.converters(View.Edit.class);
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
		String substring = requestURI.substring(bar.getUri().length());

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
}
