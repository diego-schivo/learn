package com.backflipsource.servlet;

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

	// public static String CONTROL_STACK = EntityServlet.class.getName() +
	// ".controlStack";
	public static String CONTEXT = EntityServlet.class.getName() + ".context";

	private static Logger logger = getLogger(EntityServlet.class.getName());

	static {
		logger.setLevel(Level.ALL);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

	protected Class<?> class1;

	protected HttpHandler.Resolver handlerResolver;

	public Class<?> getClass1() {
		return class1;
	}

	@Override
	public void init() throws ServletException {
		logger.fine("EntityServlet init");

		class1 = unsafeGet(() -> forName(getServletName()));

		Class<? extends HttpHandler.Resolver> handlerClass = class1.getAnnotation(View.class).handlerResolver();
		handlerResolver = Objects.equals(handlerClass, HttpHandler.Resolver.class) ? new DefaultEntityHandler(class1)
				: (HttpHandler.Resolver) unsafeGet(() -> handlerClass.getDeclaredConstructor().newInstance());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		logger.fine(() -> "requestURI " + requestURI);

		if (request.getAttribute("requestURI") == null) {
			request.setAttribute("requestURI", requestURI);
		}

		EntityContext context = new EntityContext();
		context.servlet = this;

		HttpHandler handler = handlerResolver.handler(request);
		context.handler = handler;

		request.setAttribute(CONTEXT, context);

		handler.handle(request, response);
	}

	public static class EntityContext {

		protected EntityServlet servlet;

		protected HttpHandler handler;

		protected Stack<Control> controls = new Stack<>();

		public EntityServlet getServlet() {
			return servlet;
		}

		public HttpHandler getHandler() {
			return handler;
		}

		public Stack<Control> getControls() {
			return controls;
		}
	}
}
