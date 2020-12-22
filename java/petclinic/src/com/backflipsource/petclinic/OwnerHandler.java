package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.emptyString;
import static com.backflipsource.Helpers.unsafeRun;
import static java.util.regex.Pattern.compile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.servlet.AbstractHandler;
import com.backflipsource.servlet.BaseHandler;
import com.backflipsource.servlet.DefaultEntityHandler;
import com.backflipsource.servlet.Table;
import com.backflipsource.servlet.View;

public class OwnerHandler extends DefaultEntityHandler {

	public OwnerHandler() {
		super(Owner.class);
		handlers.add(new FindHandler(Owner.class));
		handlers.add(new LastNameHandler(Owner.class));
	}

	protected static class FindHandler extends AbstractHandler {

		protected Pattern pattern;

		public FindHandler(Class<?> class1) {
			super(class1);
			pattern = compile(entityView.getUri() + "/find");
		}

		@Override
		public void handle(HttpServletRequest request, HttpServletResponse response) {
			render(new OwnerFind.Factory(entityView).control(null), View.Edit.class, request, response);
		}

		@Override
		public int vote(HttpServletRequest request) {
			String path = request.getRequestURI().substring(request.getContextPath().length());
			Matcher matcher = pattern.matcher(path);
			return matcher.matches() ? 1 : -1;
		}
	}

	protected static class LastNameHandler extends BaseHandler {

		public LastNameHandler(Class<?> class1) {
			super(class1);
		}

		@Override
		public void handle(HttpServletRequest request, HttpServletResponse response) {
			String lastName = request.getParameter("lastName");
			List<Owner> find = ((Owner.Data) entityData).find(lastName);
			if (find.size() == 1) {
				unsafeRun(() -> response
						.sendRedirect((String) request.getAttribute("requestURI") + "/" + find.get(0).getId()));
				return;
			}
			render(new Table.Factory(entityView).control(find), View.List.class, request, response);
		}

		@Override
		public int vote(HttpServletRequest request) {
			int vote = super.vote(request);
			return (vote > 0 && !emptyString(request.getParameter("lastName"))) ? (vote + 1) : -1;
		}
	}
}
