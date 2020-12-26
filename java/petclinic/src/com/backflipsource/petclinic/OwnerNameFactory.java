package com.backflipsource.petclinic;

import static com.backflipsource.servlet.EntityServlet.getInitialRequest;

import com.backflipsource.ui.Anchor;

public class OwnerNameFactory extends Anchor.Factory {

	@Override
	protected void init(Anchor control) {
		control.setHref(getInitialRequest().getRequestURI() + "/" + control.getValue());

		Owner owner = (Owner) control.getTarget();
		control.setText(owner.getFirstName() + " " + owner.getLastName());
	}
}
