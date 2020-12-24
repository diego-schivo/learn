package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.unsafeRun;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.servlet.Controller;
import com.backflipsource.servlet.EntityView;
import com.backflipsource.servlet.ListEntities;
import com.backflipsource.servlet.Table;
import com.backflipsource.servlet.View;

@Controller(regex = "/_uri_", parameter = "lastName", score = 2)
public class FilterOwnersByLastName extends ListEntities {

	public FilterOwnersByLastName(EntityView entityView) {
		super(entityView);
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
}