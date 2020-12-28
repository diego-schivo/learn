package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.ui.EntityServlet.initialRequest;

import java.util.List;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.Control;
import com.backflipsource.DefaultControl;
import com.backflipsource.ui.Entity;
import com.backflipsource.ui.ListEntities;

@Entity.Controller(regex = "_uri_", parameter = "lastName", score = 2)
public class FilterOwners extends ListEntities {

	@Override
	protected Object target(HttpServletRequest request) {
		String lastName = request.getParameter("lastName");
		List<Owner> owners = ((Owner.Data) entityData).find(lastName);

		if (owners.size() == 1) {
			Redirect redirect = new Redirect();
			redirect.location = initialRequest().getRequestURI() + "/" + owners.get(0).getId();
			return redirect;
		}

		return owners;
	}

	@Override
	protected Control.Factory<?> controlFactory(Object target, HttpServletRequest request) {
		if (target instanceof Redirect) {
			return new DefaultControl.Factory<DefaultControl>();
		}
		return super.controlFactory(target, request);
	}

	@Override
	protected void render(Control control, HttpServletRequest request, HttpServletResponse response) {
		if (control.getTarget() instanceof Redirect) {
			((Redirect) control.getTarget()).accept(response);
			return;
		}
		super.render(control, request, response);
	}

	protected static class Redirect implements Consumer<HttpServletResponse> {

		protected String location;

		@Override
		public void accept(HttpServletResponse response) {
			unsafeRun(() -> response.sendRedirect(location));
		}
	}
}
