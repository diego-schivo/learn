package com.backflipsource.petclinic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.servlet.AbstractControl;
import com.backflipsource.servlet.EntityRequestHandler;
import com.backflipsource.servlet.Controller;
import com.backflipsource.servlet.EntityView;
import com.backflipsource.servlet.View.Edit;

@Controller(regex = "/_uri_/find", score = 2)
public class FindOwners extends EntityRequestHandler {

	public FindOwners(EntityView entityView) {
		super(entityView);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		render(new Control.Factory(entityView).create(null), Edit.class, request, response);
	}

	public static class Control extends AbstractControl {

		@Override
		protected Class<?> getView() {
			return Edit.class;
		}

		public static class Factory extends AbstractControl.Factory<Control> {

			protected Factory(EntityView entityView) {
				super(Control.class, entityView, null, null, null, "find-owners.jsp");
			}
		}
	}
}
