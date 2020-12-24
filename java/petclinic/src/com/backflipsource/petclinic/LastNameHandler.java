package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.emptyString;
import static com.backflipsource.Helpers.unsafeRun;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.servlet.BaseHandler;
import com.backflipsource.servlet.Controller;
import com.backflipsource.servlet.Table;
import com.backflipsource.servlet.View;

@Controller(regex = "/_uri_", parameter = "lastName", score = 1)
public class LastNameHandler extends BaseHandler {

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
		render(new Table.Factory(entityView).create(find), View.List.class, request, response);
	}

//	@Override
//	public int vote(HttpServletRequest request) {
//		int vote = super.vote(request);
//		return (vote > 0 && !emptyString(request.getParameter("lastName"))) ? (vote + 1) : -1;
//	}
}