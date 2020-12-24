package com.backflipsource.servlet;

import static com.backflipsource.Helpers.stringAfterLast;
import static com.backflipsource.Helpers.stringWithoutSuffix;
import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.servlet.View.Edit;

@Controller(regex = "/_uri_/([^/]+)/edit", score = 1)
public class EditEntity extends EntityRequestHandler {

	private static Logger logger = logger(EditEntity.class, ALL);

	public EditEntity(EntityView entityView) {
		super(entityView);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		String id = stringAfterLast(stringWithoutSuffix(path, "/edit"), "/");
		Object item = item(id);
		logger.fine(() -> "id " + id + " item " + item);

		if ("post".equalsIgnoreCase(request.getMethod())) {
			save(item, request);
			unsafeRun(() -> response.sendRedirect((String) request.getAttribute("requestURI")));
			return;
		}

		render(new Form.Factory(entityView).create(item), Edit.class, request, response);
	}
}
