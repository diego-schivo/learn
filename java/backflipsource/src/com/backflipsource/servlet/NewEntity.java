package com.backflipsource.servlet;

import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.unsafeRun;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.ui.Entity;
import com.backflipsource.ui.spec.EntityResource;

@Entity.Controller(regex = "_uri_/new", score = 2)
public class NewEntity extends EntityRequestHandler {

	private static Logger logger = logger(NewEntity.class, ALL);

	public NewEntity(EntityResource entityView) {
		super(entityView);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		Object item = resource.getEntity().newInstance();

		if ("post".equalsIgnoreCase(request.getMethod())) {
			save(item, request);
			unsafeRun(() -> response.sendRedirect((String) request.getAttribute("requestURI")));
			return;
		}

		// render(new Form.Factory(entityView).create(item),
		// com.backflipsource.ui.EntityForm.class, request, response);
	}
}
