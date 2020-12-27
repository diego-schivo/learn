package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.unsafeRun;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.AbstractControl;
import com.backflipsource.Control;
import com.backflipsource.UtilHelper.RunnableThrowingException;
import com.backflipsource.ui.Entity;
import com.backflipsource.ui.ListEntities;

@Entity.Controller(regex = "_uri_", parameter = "lastName", score = 2)
public class FilterOwners extends ListEntities {

	@Override
	protected Object target(HttpServletRequest request) {
		String lastName = request.getParameter("lastName");
		List<Owner> owners = ((Owner.Data) entityData).find(lastName);
		return owners;
	}

	@Override
	protected Control.Factory<?> controlFactory(Object target, HttpServletRequest request) {
		if (target instanceof RunnableThrowingException) {
			return new AbstractControl.Factory();
		}
		return super.controlFactory(target, request);
	}

	@Override
	protected void render(Control control, HttpServletRequest request, HttpServletResponse response) {
		if (control.getTarget() instanceof RunnableThrowingException) {
			unsafeRun(((RunnableThrowingException) control.getTarget()));
		}
		super.render(control, request, response);
	}
}