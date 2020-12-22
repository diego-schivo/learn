package com.backflipsource.servlet;

import static com.backflipsource.Helpers.unsafeRun;
import static java.util.logging.Logger.getLogger;
import static java.util.regex.Pattern.compile;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditHandler extends AbstractHandler {

	private static Logger logger = getLogger(EditHandler.class.getName());

	static {
		logger.setLevel(Level.ALL);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

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

		render(new Form.Factory(entityView).control(item), View.Edit.class, request, response);
	}

	@Override
	public int vote(HttpServletRequest request) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		Matcher matcher = pattern.matcher(path);
		return matcher.matches() ? 1 : -1;
	}
}
