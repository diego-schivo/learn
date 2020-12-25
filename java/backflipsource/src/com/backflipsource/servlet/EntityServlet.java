package com.backflipsource.servlet;

import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.ui.spec.EntityUI.entityUI;
import static java.lang.Class.forName;
import static java.util.logging.Level.ALL;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.RequestHandler;
import com.backflipsource.ui.Entity;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI;

@SuppressWarnings({ "serial" })
public class EntityServlet extends HttpServlet {

	public static String CONTEXT = EntityServlet.class.getName() + ".CONTEXT";

	protected static ThreadLocal<EntityUI> threadUI = new ThreadLocal<>();

	private static Logger logger = logger(EntityServlet.class, ALL);

	public static EntityUI getEntityUI() {
		return threadUI.get();
	}

	protected EntityUI ui;

	protected EntityResource resource;

	protected RequestHandler.Provider handlerProvider;

	public EntityResource getResource() {
		return resource;
	}

	@Override
	public void init() throws ServletException {
		Class<?> entity = unsafeGet(() -> forName(getServletName()));
		logger.fine(() -> "entity " + entity);

		Class<?> uiClass = unsafeGet(() -> forName(getInitParameter(EntityUI.class.getName())));
		ui = entityUI(uiClass);
		logger.fine(() -> "ui " + ui);

		resource = ui.getResources().get(entity);
		logger.fine(() -> "resource " + resource);

		Class<? extends RequestHandler.Provider> providerClass = entity.getAnnotation(Entity.class).handlerProvider();
		handlerProvider = unsafeGet(() -> (RequestHandler.Provider) providerClass
				.getConstructor(EntityResource.class, EntityUI.class).newInstance(resource, ui));
		logger.fine(() -> "handlerProvider " + handlerProvider);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		boolean first = (threadUI.get() == null);
		if (first) {
			threadUI.set(ui);
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

			DefaultEntityContext context = new DefaultEntityContext();
			context.servlet = this;
			context.handler = handler;
			request.setAttribute(CONTEXT, context);

			handler.handle(request, response);
		} finally {
			if (first) {
				threadUI.remove();
			}
		}
	}
}
