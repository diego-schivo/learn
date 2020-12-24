package com.backflipsource.petclinic;

import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.servlet.AbstractHandler;
import com.backflipsource.servlet.Controller;
import com.backflipsource.servlet.View;

@Controller(regex = "/_uri_/find", score = 2)
public class FindHandler extends AbstractHandler {

	protected Pattern pattern;

	public FindHandler(Class<?> class1) {
		super(class1);
		pattern = compile(entityView.getUri() + "/find");
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		render(new OwnerFind.Factory(entityView).create(null), View.Edit.class, request, response);
	}

//	@Override
//	public int vote(HttpServletRequest request) {
//		String path = request.getRequestURI().substring(request.getContextPath().length());
//		Matcher matcher = pattern.matcher(path);
//		return matcher.matches() ? 2 : -1;
//	}
}