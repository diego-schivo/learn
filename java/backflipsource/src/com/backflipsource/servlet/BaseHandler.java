package com.backflipsource.servlet;

import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.util.logging.Level.ALL;
import static java.util.regex.Pattern.compile;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller(regex = "/_uri_", score = 1)
public class BaseHandler extends AbstractHandler {

	private static Logger logger = logger(BaseHandler.class, ALL);

	protected Pattern pattern;

	public BaseHandler(Class<?> class1) {
		super(class1);
		pattern = compile(entityView.getUri());
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		Control control;
		if (entityData != null) {
			control = new Table.Factory(entityView).create(entityData.list());
		} else if (item0 != null) {
			control = new DescriptionList.Factory(entityView).create(item0);
		} else {
			control = null;
		}

		Class<?> view = (item0 != null) ? View.Show.class : View.List.class;

		render(control, view, request, response);
	}

//	@Override
//	public int vote(HttpServletRequest request) {
//		String path = request.getRequestURI().substring(request.getContextPath().length());
//		Matcher matcher = pattern.matcher(path);
//		return matcher.matches() ? 1 : -1;
//	}
}
