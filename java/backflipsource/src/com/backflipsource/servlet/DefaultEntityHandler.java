package com.backflipsource.servlet;

import static com.backflipsource.Helpers.arrayGet;
import static com.backflipsource.Helpers.classFields;
import static com.backflipsource.Helpers.emptyString;
import static com.backflipsource.Helpers.forwardServletRequest;
import static com.backflipsource.Helpers.getGetter;
import static com.backflipsource.Helpers.getSetters;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.servlet.EntityContextListener.getViews;
import static com.backflipsource.servlet.EntityServlet.CONTROL_STACK;
import static java.util.logging.Logger.getLogger;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.Helpers;

public class DefaultEntityHandler implements EntityHandler {

	private static Logger logger = getLogger(DefaultEntityHandler.class.getName());

	static {
		logger.setLevel(Level.ALL);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

	protected Class<?> class1;
	protected List<Field> fields;
	protected Field idField;
	protected EntityData entityData;
	protected Object item0;
	protected EntityView entityView;
	protected Pattern pattern;

	@SuppressWarnings("rawtypes")
	public DefaultEntityHandler(Class<?> class1) {
		this.class1 = class1;
		fields = safeStream(classFields(class1))
				.filter(field -> field.getAnnotationsByType(View.Field.class).length > 0).collect(toList());
		idField = safeStream(fields)
				.filter(field -> safeStream(field.getAnnotationsByType(View.Field.class))
						.anyMatch(View.Field::identifier))
				.findFirst()
				// .orElseGet(() -> fields.iterator().next());
				.orElse(null);
		entityData = safeGet(() -> (EntityData) class1.getDeclaredField("data").get(null));
		item0 = safeGet(() -> class1.getDeclaredField("instance").get(null));
		entityView = getViews().get(class1.getName());
		pattern = compile(entityView.getUri() + "(" + (idField != null ? "(/[^/]+)" : "") + "(/edit)?)?");
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		Matcher matcher = pattern.matcher(path);

		EntityHandler.Result result = matcher.matches() ? handleMatch(matcher, request) : subpath(path);

		if (!emptyString(result.getForward())) {
			forwardServletRequest(result.getForward(), request, response);
			return;
		}

		if (!emptyString(result.getRedirect())) {
			Helpers.unsafeRun(() -> response.sendRedirect(result.getRedirect()));
			return;
		}

		if ("get".equalsIgnoreCase(request.getMethod())) {
			Stack<Control> stack = new Stack<>();
			stack.push(result.getControl());
			request.setAttribute(CONTROL_STACK, stack);

			View view2 = safeStream(class1.getAnnotationsByType(View.class))
					.filter(item -> safeList(item.value()).contains(result.getView())).findFirst().orElse(null);

			String path2 = nonEmptyString(view2 != null ? view2.page() : null, "/entity.jsp");
			forwardServletRequest(path2, request, response);
		}

		if ("post".equalsIgnoreCase(request.getMethod())) {
			unsafeRun(() -> response.sendRedirect((String) request.getAttribute("requestURI")));
		}
	}

	protected EntityHandler.Result handleMatch(Matcher matcher, HttpServletRequest request) {
		boolean base = emptyString(matcher.group(1));
		if (base) {
			return base(request);
		}

		boolean new1 = matcher.group(1).equals("/new");
		if (new1) {
			return new1(request);
		}

		String id = matcher.group(2).substring(1);

		boolean edit = !emptyString(matcher.group(3));
		if (edit) {
			return edit(id, request);
		}

		return show(id);
	}

	protected EntityHandler.Result subpath(String path) {
		String substring = path.substring(entityView.getUri().length());
		if (idField != null) {
			substring = substring.substring(substring.indexOf('/', 1));
		}

		String path2 = substring;
		logger.fine(() -> "path2 " + path2);

		Result result = new Result();
		result.forward = path2;
		return result;
	}

	protected EntityHandler.Result base(HttpServletRequest request) {
		Result result = new Result();
		if (entityData != null) {
			result.setView(View.List.class);
			result.setControl(new Table.Factory(entityView).control(entityData.list()));
		} else if (item0 != null) {
			result.setView(View.Show.class);
			result.setControl(new DescriptionList.Factory(entityView).control(item0));
		}
		return result;
	}

	protected EntityHandler.Result new1(HttpServletRequest request) {
		Object item = unsafeGet(() -> class1.getDeclaredConstructor().newInstance());

		if ("post".equalsIgnoreCase(request.getMethod())) {
			save(item, request);
			return null;
		}

		Result result = new Result();
		result.setView(View.Edit.class);
		result.setControl(new Form.Factory(entityView).control(item));
		return result;
	}

	protected EntityHandler.Result edit(String id, HttpServletRequest request) {
		Object item = item(id);

		if ("post".equalsIgnoreCase(request.getMethod())) {
			save(item, request);
			return null;
		}

		Result result = new Result();
		result.setView(View.Edit.class);
		result.setControl(new Form.Factory(entityView).control(item));
		return result;
	}

	protected EntityHandler.Result show(String id) {
		Object item = item(id);

		Result result = new Result();
		result.setView(View.Show.class);
		result.setControl(new DescriptionList.Factory(entityView).control(item));

		return result;
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

	public static class Result implements EntityHandler.Result {

		protected Class<?> view;

		protected Control control;

		protected String forward;

		protected String redirect;

		@Override
		public Class<?> getView() {
			return view;
		}

		public void setView(Class<?> view) {
			this.view = view;
		}

		@Override
		public Control getControl() {
			return control;
		}

		public void setControl(Control control) {
			this.control = control;
		}

		@Override
		public String getForward() {
			return forward;
		}

		public void setForward(String forward) {
			this.forward = forward;
		}

		@Override
		public String getRedirect() {
			return redirect;
		}

		public void setRedirect(String redirect) {
			this.redirect = redirect;
		}
	}
}