package com.backflipsource.ui;

import static com.backflipsource.Helpers.logger;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.backflipsource.servlet.EntityRequestHandler;
import com.backflipsource.ui.spec.EntityUI.Mode;

@Entity.Controller(regex = "_uri_/new", score = 2)
public class NewEntity extends EntityRequestHandler {

	private static Logger logger = logger(NewEntity.class, ALL);

	@Override
	protected Object target(HttpServletRequest request) {
		Object item = resource.getEntity().newInstance();

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
