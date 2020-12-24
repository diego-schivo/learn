package com.backflipsource.servlet;

import static com.backflipsource.Helpers.stringAfterLast;
import static com.backflipsource.Helpers.stringWithoutSuffix;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.servlet.View.Show;

@Controller(regex = "/_uri_/([^/]+)", score = 1)
public class ShowEntity extends EntityRequestHandler {

	private static Logger logger = logger(ShowEntity.class, ALL);

	public ShowEntity(EntityView entityView) {
		super(entityView);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		String id = stringAfterLast(stringWithoutSuffix(path, "/edit"), "/");
		Object item = item(id);
		logger.fine(() -> "id " + id + " item " + item);

		render(new DescriptionList.Factory(entityView).create(item), Show.class, request, response);
	}
}
