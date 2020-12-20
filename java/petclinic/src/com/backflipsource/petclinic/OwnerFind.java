package com.backflipsource.petclinic;

import com.backflipsource.servlet.AbstractControl;
import com.backflipsource.servlet.EntityView;
import com.backflipsource.servlet.View;

public class OwnerFind extends AbstractControl {

	@Override
	protected Class<?> getView() {
		return View.Edit.class;
	}

	public static class Factory extends AbstractControl.Factory<OwnerFind> {

		protected Factory(EntityView entityView) {
			super(OwnerFind.class, entityView, null, null, null, "owner-find.jsp");
		}
	}
}
