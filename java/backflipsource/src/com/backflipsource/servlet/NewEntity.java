package com.backflipsource.servlet;

import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller(regex = "/_uri_/new", score = 2)
public class NewEntity extends EntityRequestHandler {

	private static Logger logger = logger(NewEntity.class, ALL);

	public NewEntity(EntityView entityView) {
		super(entityView);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		Object item = unsafeGet(() -> entityView.getEntity().getDeclaredConstructor().newInstance());

		if ("post".equalsIgnoreCase(request.getMethod())) {
			save(item, request);
			unsafeRun(() -> response.sendRedirect((String) request.getAttribute("requestURI")));
			return;
		}

		render(new Form.Factory(entityView).create(item), View.Edit.class, request, response);
	}
}
