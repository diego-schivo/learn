package com.backflipsource.ui;

import com.backflipsource.ui.Form.Factory;
import com.backflipsource.ui.spec.EntityUI.Mode;

public class Form extends AbstractEntityControl<Factory> {

	@Override
	protected Class<? extends Mode> getMode() {
		return EntityForm.class;
	}

	public static class Factory extends AbstractEntityControl.Factory<Form> {

		public Factory() {
			setControl(Form.class);
		}
	}
}
