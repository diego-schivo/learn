package com.backflipsource.servlet;

import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller(regex = "/_uri_", score = 1)
public class ListEntities extends EntityRequestHandler {

	private static Logger logger = logger(ListEntities.class, ALL);

	public ListEntities(EntityView entityView) {
		super(entityView);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		Control control;
		if (entityData != null) {
			control = new Table.Factory(entityView).create(entityData.list());
		} else if (item0 != null) {
			control = new DescriptionList.Factory(entityView).create(item0);
		} else {
			control = null;
		}

		Class<?> view = (item0 != null) ? View.Show.class : View.List.class;

		render(control, view, request, response);
	}
}
