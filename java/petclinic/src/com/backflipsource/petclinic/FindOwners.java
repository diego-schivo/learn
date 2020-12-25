package com.backflipsource.petclinic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.petclinic.FindOwners.Control.Factory;
import com.backflipsource.servlet.EntityRequestHandler;
import com.backflipsource.ui.AbstractEntityControl;
import com.backflipsource.ui.Entity;
import com.backflipsource.ui.EntityForm;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI.Mode;

@Entity.Controller(regex = "_uri_/find", score = 2)
public class FindOwners extends EntityRequestHandler {

	public FindOwners(EntityResource entityView) {
		super(entityView);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		render(new Control.Factory(resource).create(null), EntityForm.class, request, response);
	}

	public static class Control extends AbstractEntityControl<Factory> {

		@Override
		protected Class<? extends Mode> getMode() {
			return EntityForm.class;
		}

		public static class Factory extends AbstractEntityControl.Factory<Control> {

			protected Factory(EntityResource entityView) {
				super(Control.class, null, null, null, "find-owners.jsp", null, entityView);
			}
		}
	}
}
