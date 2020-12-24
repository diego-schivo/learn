package com.backflipsource.servlet;

import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.lang.Class.forName;
import static java.util.logging.Level.ALL;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings({ "serial" })
public class EntityServlet extends HttpServlet {

	public static String CONTEXT = EntityServlet.class.getName() + ".context";

	protected static ThreadLocal<EntityContextListener> CONTEXT_LISTENER = new ThreadLocal<>();

	private static Logger logger = logger(EntityServlet.class, ALL);

	public static EntityContextListener getContextListener() {
		return CONTEXT_LISTENER.get();
	}

	protected EntityContextListener contextListener;

	protected EntityView view;

	protected RequestHandler.Provider handlerProvider;

	public EntityView getView() {
		return view;
	}

	@Override
	public void init() throws ServletException {
		Class<?> entity = unsafeGet(() -> forName(getServletName()));
		logger.fine(() -> "entity " + entity);

		String className = getInitParameter(EntityContextListener.class.getName());
		contextListener = EntityContextListener.getInstance(className);
		logger.fine(() -> "contextListener " + contextListener);

		view = contextListener.getViews().get(entity);
		logger.fine(() -> "view " + view);

		Class<? extends RequestHandler.Provider> class1 = entity.getAnnotation(View.class).handlerProvider();
		handlerProvider = Objects.equals(class1, RequestHandler.Provider.class)
				? new DefaultRequestHandlerProvider(view)
				: (RequestHandler.Provider) unsafeGet(() -> class1.getConstructor().newInstance());
		logger.fine(() -> "handlerProvider " + handlerProvider);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		boolean first = CONTEXT_LISTENER.get() == null;
		if (first) {
			CONTEXT_LISTENER.set(contextListener);
		}

		try {
			String requestURI = request.getRequestURI();
			logger.fine(() -> "requestURI " + requestURI);

			if (request.getAttribute("requestURI") == null) {
				request.setAttribute("requestURI", requestURI);
			}

			RequestHandler handler = handlerProvider.provide(request);
			logger.fine(() -> "handler " + handler);

			if (handler == null) {
				return;
			}

			EntityContext context = new EntityContext();
			context.servlet = this;
			context.handler = handler;
			request.setAttribute(CONTEXT, context);

			handler.handle(request, response);
		} finally {
			if (first) {
				CONTEXT_LISTENER.remove();
			}
		}
	}
}
