package com.backflipsource.servlet;

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
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings({ "rawtypes", "serial" })
public class EntityServlet extends HttpServlet {

	public static String CONTROL_STACK = EntityServlet.class.getName() + ".controlStack";

	private static Logger logger = getLogger(EntityServlet.class.getName());

	static {
		logger.setLevel(Level.ALL);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

	protected Class<?> class1;
	protected List<Field> fields;
	protected Field idField;
	protected List items;
	protected Object item0;
	protected EntityView entityView;
	protected Pattern pattern;

	@Override
	public void init() throws ServletException {
		logger.fine("EntityServlet init");

		class1 = unsafeGet(() -> Class.forName(getServletName()));
		fields = safeStream(classFields(class1))
				.filter(field -> field.getAnnotationsByType(View.Field.class).length > 0).collect(toList());
		idField = safeStream(fields)
				.filter(field -> safeStream(field.getAnnotationsByType(View.Field.class))
						.anyMatch(View.Field::identifier))
				.findFirst()
				// .orElseGet(() -> fields.iterator().next());
				.orElse(null);
		items = safeGet(() -> (List) class1.getDeclaredField("list").get(null));
		item0 = safeGet(() -> class1.getDeclaredField("instance").get(null));
		entityView = getViews().get(getServletName());
		pattern = Pattern.compile(entityView.getUri() + "(" + (idField != null ? "(/[^/]+)" : "") + "(/edit)?)?");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.fine("EntityServlet doGet");

		if (request.getAttribute("requestURI") == null) {
			request.setAttribute("requestURI", request.getRequestURI());
		}

		// request.setAttribute("entityView", entityView);
		Stack<Control> stack = new Stack<>();
		request.setAttribute(CONTROL_STACK, stack);

		String path = request.getRequestURI().substring(request.getContextPath().length());
		Matcher matcher = pattern.matcher(path);
		if (!matcher.matches()) {
			String substring = path.substring(entityView.getUri().length());
			if (idField != null) {
				substring = substring.substring(substring.indexOf('/', 1));
			}
			forwardServletRequest(substring, request, response);
			return;
		}

		Class<?> view;
		if (!emptyString(matcher.group(1))) {
			boolean new1 = matcher.group(1).equals("/new");
			view = new1 || !emptyString(matcher.group(3)) ? View.Edit.class : View.Show.class;
			Object item = new1 ? unsafeGet(() -> class1.getDeclaredConstructor().newInstance())
					: item(matcher.group(2).substring(1));
			// request.setAttribute("item", item);
			stack.push(new DescriptionList.Factory(entityView).control(item));
		} else if (items != null) {
			view = View.List.class;
			// request.setAttribute("items", items);
			stack.push(new Table.Factory(entityView).control(items));
		} else if (item0 != null) {
			view = View.Show.class;
			// request.setAttribute("item", item0);
			stack.push(new DescriptionList.Factory(entityView).control(item0));
		} else {
			view = null;
		}

		// request.setAttribute("view", view);

		View view2 = safeStream(class1.getAnnotationsByType(View.class))
				.filter(item -> safeList(item.value()).contains(view)).findFirst().orElse(null);
		String path2 = nonEmptyString(view2 != null ? view2.page() : null,
				"/" + view.getSimpleName().toLowerCase() + ".jsp");
		forwardServletRequest(path2, request, response);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		Matcher matcher = pattern.matcher(path);
		if (!matcher.matches()) {
			String substring = path.substring(entityView.getUri().length());
			if (idField != null) {
				substring = substring.substring(substring.indexOf('/', 1));
			}
			forwardServletRequest(substring, request, response);
			return;
		}
		boolean new1 = matcher.group(1).equals("/new");
		Object item = new1 ? unsafeGet(() -> class1.getDeclaredConstructor().newInstance())
				: item(matcher.group(2).substring(1));

		Map<String, StringConverter<?>> converters = entityView.converters(View.Edit.class);
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

		if (new1) {
			items.add(item);
		}

		response.sendRedirect(request.getRequestURI());
	}

	@SuppressWarnings("unchecked")
	protected Object item(String id) {
		return safeStream(items)
				.filter(item -> Objects.equals(safeGet(() -> getGetter(idField).invoke(item)).toString(), id))
				.findFirst().orElse(null);
	}
}
