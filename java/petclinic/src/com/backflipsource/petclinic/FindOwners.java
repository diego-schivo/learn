package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.substringBeforeLast;
import static com.backflipsource.ui.EntityServlet.initialRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.Control;
import com.backflipsource.ui.Entity;
import com.backflipsource.ui.Form;
import com.backflipsource.ui.NewEntity;
import com.backflipsource.ui.spec.EntityUI.Mode;

@Entity.Controller(regex = "_uri_/find", score = 2)
public class FindOwners extends NewEntity {

	@Override
	protected Class<? extends Mode> mode(HttpServletRequest request) {
		return OwnerFind.class;
	}

	@Override
	protected void render(Control control, HttpServletRequest request, HttpServletResponse response) {
		Form form = (Form) control;
		form.setAction(substringBeforeLast(initialRequest().getRequestURI(), "/"));
		form.setMethod("get");

		super.render(control, request, response);
	}
}
