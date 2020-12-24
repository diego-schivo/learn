package com.backflipsource.servlet;

import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.lang.Class.forName;
import static java.util.logging.Level.ALL;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;
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

	private static Logger logger = logger(EntityServlet.class, ALL);

	protected Class<?> class1;

	protected RequestHandler.Provider handlerProvider;

	public Class<?> getClass1() {
		return class1;
	}

	@Override
	public void init() throws ServletException {
		logger.fine("EntityServlet init");

		class1 = unsafeGet(() -> forName(getServletName()));

		Class<? extends RequestHandler.Provider> class2 = class1.getAnnotation(View.class).handlerProvider();
		handlerProvider = Objects.equals(class2, RequestHandler.Provider.class)
				? new DefaultRequestHandlerProvider(class1)
				: (RequestHandler.Provider) unsafeGet(() -> class2.getDeclaredConstructor().newInstance());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		logger.fine(() -> "requestURI " + requestURI);

		if (request.getAttribute("requestURI") == null) {
			request.setAttribute("requestURI", requestURI);
		}

		RequestHandler handler = handlerProvider.provide(request);
		if (handler == null) {
			return;
		}

		EntityContext context = new EntityContext();
		context.servlet = this;
		context.handler = handler;
		request.setAttribute(CONTEXT, context);

		handler.handle(request, response);
	}

	public static class EntityContext {

		protected EntityServlet servlet;

		protected RequestHandler handler;

		protected Stack<Control> controls = new Stack<>();

		public EntityServlet getServlet() {
			return servlet;
		}

		public RequestHandler getHandler() {
			return handler;
		}

		public Stack<Control> getControls() {
			return controls;
		}
	}
}
