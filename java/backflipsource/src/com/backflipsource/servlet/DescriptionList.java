package com.backflipsource.servlet;

import com.backflipsource.ui.AbstractEntityControl;

public class DescriptionList extends AbstractEntityControl {

	public static class Factory extends AbstractEntityControl.Factory<DescriptionList> {

		public Factory() {
			setControl(DescriptionList.class);
		}
	}
}
