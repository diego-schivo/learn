package com.backflipsource.servlet;

import static com.backflipsource.Helpers.unsafeGet;
import static java.lang.Class.forName;
import static java.util.logging.Logger.getLogger;

import java.io.IOException;
import java.util.Objects;
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

	protected HttpHandler handler;

	@Override
	public void init() throws ServletException {
		logger.fine("EntityServlet init");

		class1 = unsafeGet(() -> forName(getServletName()));

		Class<? extends HttpHandler> handlerClass = class1.getAnnotation(View.class).handler();
		handler = Objects.equals(handlerClass, HttpHandler.class) ? new DefaultEntityHandler(class1)
				: (HttpHandler) unsafeGet(() -> handlerClass.getDeclaredConstructor().newInstance());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		logger.fine(() -> "requestURI " + requestURI);

		if (request.getAttribute("requestURI") == null) {
			request.setAttribute("requestURI", requestURI);
		}

		handler.handle(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		logger.fine(() -> "requestURI " + requestURI);

		if (request.getAttribute("requestURI") == null) {
			request.setAttribute("requestURI", requestURI);
		}

		handler.handle(request, response);
	}
}
