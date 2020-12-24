package com.backflipsource.servlet;

import static com.backflipsource.Helpers.arrayGet;
import static com.backflipsource.Helpers.classFields;
import static com.backflipsource.Helpers.forwardServletRequest;
import static com.backflipsource.Helpers.getGetter;
import static com.backflipsource.Helpers.getSetters;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.servlet.EntityContextListener.getViews;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static com.backflipsource.servlet.EntityServlet.CONTEXT;
import static java.util.logging.Level.ALL;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.servlet.EntityServlet.EntityContext;

public abstract class AbstractHandler implements RequestHandler {

	private static Logger logger = logger(AbstractHandler.class, ALL);

	protected Class<?> class1;

	protected EntityView entityView;

	protected List<Field> fields;

	protected Field idField;

	protected EntityData entityData;

	protected Object item0;

	public AbstractHandler(Class<?> class1) {
		this.class1 = class1;
		entityView = getViews().get(class1.getName());
		fields = safeStream(classFields(class1))
				.filter(field -> field.getAnnotationsByType(View.Field.class).length > 0).collect(toList());
		idField = safeStream(fields).filter(
				field -> safeStream(field.getAnnotationsByType(View.Field.class)).anyMatch(View.Field::identifier))
				.findFirst().orElse(null);
		// pattern = compile(entityView.getUri() + "(" + (idField != null ? "(/[^/]+)" :
		// "") + "(/edit)?)?");
		entityData = safeGet(() -> (EntityData) class1.getDeclaredField("data").get(null));
		item0 = safeGet(() -> class1.getDeclaredField("instance").get(null));
	}

	protected void render(Control control, Class<?> view, HttpServletRequest request, HttpServletResponse response) {
		EntityContext context = (EntityContext) request.getAttribute(CONTEXT);
		context.getControls().push(control);

		View view2 = safeStream(class1.getAnnotationsByType(View.class))
				.filter(item -> safeList(item.value()).contains(view)).findFirst().orElse(null);

		String path2 = nonEmptyString(view2 != null ? view2.page() : null, "/entity.jsp");
		forwardServletRequest(path2, request, response);
	}

	protected void save(Object item, HttpServletRequest request) {
		Map<String, StringConverter<?>> converters = entityView.converters(View.Edit.class);
		Map<String, Method> setters = getSetters(class1);

		safeStream(fields).filter(field -> !Objects.equals(field, idField)).forEach(field -> {
			StringConverter<?> converter = converters.get(field.getName());
			if (converter == null) {
				return;
			}
			boolean list = List.class.isAssignableFrom(field.getType());
			String[] parameters = request.getParameterValues(field.getName());
			Object value;
			if (list) {
				value = safeStream(parameters).map(converter::convertFromString).collect(toList());
			} else {
				String parameter = arrayGet(parameters, 0);
				value = converter.convertFromString(parameter);
			}
			unsafeRun(() -> setters.get(field.getName()).invoke(item, value));
		});

		entityData.save(item);
	}

	@SuppressWarnings("unchecked")
	protected Object item(String id) {
		return safeStream(entityData.list())
				.filter(item -> Objects.equals(safeGet(() -> getGetter(idField).invoke(item)).toString(), id))
				.findFirst().orElse(null);
	}
}
