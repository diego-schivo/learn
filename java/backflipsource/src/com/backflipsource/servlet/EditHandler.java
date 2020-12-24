package com.backflipsource.servlet;

import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.util.logging.Level.ALL;
import static java.util.regex.Pattern.compile;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller(regex = "/_uri_/([^/]+)/edit", score = 1)
public class EditHandler extends AbstractHandler {

	private static Logger logger = logger(EditHandler.class, ALL);

	protected Pattern pattern;

	public EditHandler(Class<?> class1) {
		super(class1);
		pattern = compile(entityView.getUri() + "/([^/]+)/edit");
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		Matcher matcher = pattern.matcher(path);
		String id = matcher.matches() ? matcher.group(1) : null;
		Object item = item(id);

		if ("post".equalsIgnoreCase(request.getMethod())) {
			save(item, request);
			unsafeRun(() -> response.sendRedirect((String) request.getAttribute("requestURI")));
			return;
		}

		render(new Form.Factory(entityView).create(item), View.Edit.class, request, response);
	}

//	@Override
//	public int vote(HttpServletRequest request) {
//		String path = request.getRequestURI().substring(request.getContextPath().length());
//		Matcher matcher = pattern.matcher(path);
//		return matcher.matches() ? 1 : -1;
//	}
}
