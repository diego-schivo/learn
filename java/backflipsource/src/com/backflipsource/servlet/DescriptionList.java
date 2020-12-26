package com.backflipsource.servlet;

import com.backflipsource.servlet.DescriptionList.Factory;
import com.backflipsource.ui.AbstractEntityControl;
import com.backflipsource.ui.EntityDetail;
import com.backflipsource.ui.spec.EntityUI.Mode;

public class DescriptionList extends AbstractEntityControl<Factory> {

//	@Override
//	protected Class<? extends Mode> getMode() {
//		return EntityDetail.class;
//	}

	public static class Factory extends AbstractEntityControl.Factory<DescriptionList> {

		public Factory() {
			setControl(DescriptionList.class);
		}
	}
}
