package com.backflipsource.servlet;

import static com.backflipsource.Helpers.emptyString;
import static com.backflipsource.Helpers.forwardServletRequest;
import static com.backflipsource.Helpers.nonEmptyString;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static java.lang.Class.forName;
import static java.util.logging.Logger.getLogger;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings({ "serial" })
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

	protected EntityHandler handler;

	@Override
	public void init() throws ServletException {
		logger.fine("EntityServlet init");

		class1 = unsafeGet(() -> forName(getServletName()));

		Class<? extends EntityHandler> handlerClass = class1.getAnnotation(View.class).handler();
		handler = Objects.equals(handlerClass, EntityHandler.class) ? new DefaultEntityHandler(class1)
				: (EntityHandler) unsafeGet(() -> handlerClass.getDeclaredConstructor().newInstance());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		logger.fine(() -> "requestURI " + requestURI);

		if (request.getAttribute("requestURI") == null) {
			request.setAttribute("requestURI", requestURI);
		}

		EntityHandler.Result result = handler.handle(request);

		if (!emptyString(result.getPath())) {
			forwardServletRequest(result.getPath(), request, response);
			return;
		}

		Stack<Control> stack = new Stack<>();
		stack.push(result.getControl());
		request.setAttribute(CONTROL_STACK, stack);

		View view2 = safeStream(class1.getAnnotationsByType(View.class))
				.filter(item -> safeList(item.value()).contains(result.getView())).findFirst().orElse(null);

		String path2 = nonEmptyString(view2 != null ? view2.page() : null, "/entity.jsp");
		forwardServletRequest(path2, request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		logger.fine(() -> "requestURI " + requestURI);

		if (request.getAttribute("requestURI") == null) {
			request.setAttribute("requestURI", requestURI);
		}

		EntityHandler.Result result = handler.handle(request);

		if ((result != null) && !emptyString(result.getPath())) {
			forwardServletRequest(result.getPath(), request, response);
			return;
		}

		response.sendRedirect((String) request.getAttribute("requestURI"));
	}
}
