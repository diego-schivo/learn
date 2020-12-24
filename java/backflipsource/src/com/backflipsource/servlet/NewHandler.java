package com.backflipsource.servlet;

import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.util.logging.Level.ALL;
import static java.util.regex.Pattern.compile;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller(regex = "/_uri_/new", score = 1)
public class NewHandler extends AbstractHandler {

	private static Logger logger = logger(NewHandler.class, ALL);

	protected Pattern pattern;

	public NewHandler(Class<?> class1) {
		super(class1);
		pattern = compile(entityView.getUri() + "/new");
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		Object item = unsafeGet(() -> class1.getDeclaredConstructor().newInstance());

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
