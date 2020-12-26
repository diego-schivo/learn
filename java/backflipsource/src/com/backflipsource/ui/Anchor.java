package com.backflipsource.ui;

import com.backflipsource.ui.spec.EntityUI.Mode;

public class Anchor extends AbstractEntityControl {

	@Override
	protected Class<? extends Mode> getMode() {
		return EntityDetail.class;
	}

	public static class Factory extends AbstractEntityControl.Factory<Anchor> {

		public Factory() {
			setControl(Anchor.class);
		}
	}
}
