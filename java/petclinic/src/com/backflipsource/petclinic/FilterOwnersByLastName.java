package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.unsafeRun;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.ui.Entity;
import com.backflipsource.ui.EntityList;
import com.backflipsource.ui.ListEntities;
import com.backflipsource.ui.Table;
import com.backflipsource.ui.spec.EntityResource;

@Entity.Controller(regex = "_uri_", parameter = "lastName", score = 2)
public class FilterOwnersByLastName extends ListEntities {

	public FilterOwnersByLastName(EntityResource entityView) {
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
		Class<EntityList> mode = EntityList.class;
		render(new Table.Factory(resource.getEntity(), mode).create(find), mode, request, response);
	}
}