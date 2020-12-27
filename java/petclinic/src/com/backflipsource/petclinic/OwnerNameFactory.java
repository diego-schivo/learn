package com.backflipsource.petclinic;

import static com.backflipsource.servlet.EntityServlet.initialRequest;

import com.backflipsource.Control;
import com.backflipsource.ui.Anchor;

public class OwnerNameFactory extends Anchor.Factory {

	@Override
	protected void init(Control control) {
		Anchor anchor = (Anchor) control;

		anchor.setHref(initialRequest().getRequestURI() + "/" + control.getValue());

		Owner owner = (Owner) control.getTarget();
		anchor.setText(owner.getFirstName() + " " + owner.getLastName());
	}
}
