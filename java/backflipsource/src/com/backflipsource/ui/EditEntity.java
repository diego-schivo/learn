package com.backflipsource.ui;

import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.stringWithoutSuffix;
import static com.backflipsource.Helpers.substringAfterLast;
import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.ui.DefaultEntityResource.controlFactory;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.Control;
import com.backflipsource.servlet.EntityRequestHandler;
import com.backflipsource.ui.spec.EntityUI.Mode;

@Entity.Controller(regex = "_uri_/([^/]+)/edit", score = 1)
public class EditEntity extends EntityRequestHandler {

	private static Logger logger = logger(EditEntity.class, ALL);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		String id = substringAfterLast(stringWithoutSuffix(path, "/edit"), "/");
		Object item = item(id);
		logger.fine(() -> "id " + id + " item " + item);

		if ("post".equalsIgnoreCase(request.getMethod())) {
			save(item, request);
			unsafeRun(() -> response.sendRedirect((String) request.getAttribute("requestURI")));
			return;
		}

		Control.Factory<?> factory = controlFactory(resource.getEntity(), mode(request));
		Control control = factory.create(item);
		logger.fine(() -> "factory = " + factory + ", control = " + control);

		render(control, request, response);
	}

	@Override
	protected Class<? extends Mode> mode(HttpServletRequest request) {
		return EntityForm.class;
	}
}
