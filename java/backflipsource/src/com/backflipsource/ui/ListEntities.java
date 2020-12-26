package com.backflipsource.ui;

import static com.backflipsource.Helpers.logger;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.AbstractControl;
import com.backflipsource.servlet.DescriptionList;
import com.backflipsource.servlet.EntityRequestHandler;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI.Mode;

@Entity.Controller(regex = "_uri_", score = 1)
public class ListEntities extends EntityRequestHandler {

	private static Logger logger = logger(ListEntities.class, ALL);

	public ListEntities(EntityResource resource) {
		super(resource);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		Class<? extends Mode> mode = (item0 != null) ? EntityDetail.class : EntityList.class;

		AbstractEntityControl.Factory factory;
		Object target;
		if (entityData != null) {
			factory = new Table.Factory();
			target = entityData.list();
		} else if (item0 != null) {
			factory = new DescriptionList.Factory();
			target = item0;
		} else {
			factory = null;
			target = null;
		}

		factory.init(resource.getEntity(), mode);
		AbstractControl control = factory.create(target);

		render(control, mode, request, response);
	}
}
