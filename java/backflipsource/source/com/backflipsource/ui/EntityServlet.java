package com.backflipsource.ui;

import static com.backflipsource.helper.Helper.logger;
import static com.backflipsource.helper.Helper.unsafeGet;
import static java.lang.Class.forName;
import static java.util.logging.Level.ALL;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.RequestHandler;
import com.backflipsource.dynamic.DynamicClass;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI;

@SuppressWarnings({ "serial" })
public class EntityServlet extends HttpServlet {

	public static String CONTEXT = EntityServlet.class.getName() + ".CONTEXT";

	protected static ThreadLocal<Context> threadContext = new ThreadLocal<>();

	private static Logger logger = logger(EntityServlet.class, ALL);

	public static EntityUI entityUI() {
		return threadContext.get().getUI();
	}

	public static HttpServletRequest initialRequest() {
		return threadContext.get().getRequest();
	}

	protected EntityUI ui;

	protected EntityResource resource;

	protected RequestHandler.Provider handlerProvider;

	public EntityResource getResource() {
		return resource;
	}

	@Override
	public void init() throws ServletException {
		Class<?> uiClass = unsafeGet(() -> forName(getInitParameter(EntityUI.class.getName())));
		ui = EntityUI.of(uiClass);
		logger.fine(() -> "ui " + ui);

		String key = getServletName();

		DynamicClass entity = ui.getEntities().get(key);
		logger.fine(() -> "entity " + entity);

		resource = ui.getResources().get(key);
		logger.fine(() -> "resource " + resource);

		Class<? extends RequestHandler.Provider> providerClass = entity.annotation("Entity")
				.getValue("handlerProvider");
		handlerProvider = unsafeGet(() -> (RequestHandler.Provider) providerClass
				.getConstructor(EntityResource.class, EntityUI.class).newInstance(resource, ui));
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		boolean first = (threadContext.get() == null);
		if (first) {
			Context context = new Context();
			context.ui = ui;
			context.request = request;
			context.response = response;

			threadContext.set(context);
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
				threadContext.remove();
			}
		}
	}

	public static class Context {

		protected EntityUI ui;

		protected HttpServletRequest request;

		protected HttpServletResponse response;

		public EntityUI getUI() {
			return ui;
		}

		public HttpServletRequest getRequest() {
			return request;
		}

		public HttpServletResponse getResponse() {
			return response;
		}
	}
}
