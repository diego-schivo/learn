package com.backflipsource.ui;

import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.stringWithoutSuffix;
import static com.backflipsource.Helpers.substringAfterLast;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.backflipsource.ui.spec.EntityUI.Mode;

@Entity.Controller(regex = "_uri_/([^/]+)/edit", score = 1)
public class EditEntity extends EntityRequestHandler {

	private static Logger logger = logger(EditEntity.class, ALL);

	@Override
	protected Object target(HttpServletRequest request) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		String id = substringAfterLast(stringWithoutSuffix(path, "/edit"), "/");
		Object item = item(id);
		logger.fine(() -> "id " + id + " item " + item);

//		if ("post".equalsIgnoreCase(request.getMethod())) {
//			save(item, request);
//			unsafeRun(() -> response.sendRedirect((String) request.getAttribute("requestURI")));
//			return;
//		}

		return item;
	}

	@Override
	protected Class<? extends Mode> mode(HttpServletRequest request) {
		return EntityForm.class;
	}
}
