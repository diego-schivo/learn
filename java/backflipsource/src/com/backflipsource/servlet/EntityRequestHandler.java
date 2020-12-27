package com.backflipsource.servlet;

import static com.backflipsource.Helpers.arrayGet;
import static com.backflipsource.Helpers.forwardServletRequest;
import static com.backflipsource.Helpers.getFieldValue;
import static com.backflipsource.Helpers.getSetters;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.servlet.EntityServlet.CONTEXT;
import static java.util.logging.Level.ALL;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.Control;
import com.backflipsource.RequestHandler;
import com.backflipsource.dynamic.DefaultDynamicClass;
import com.backflipsource.dynamic.DynamicField;
import com.backflipsource.ui.AbstractEntityControl;
import com.backflipsource.ui.DefaultEntityResource;
import com.backflipsource.ui.EntityDetail;
import com.backflipsource.ui.EntityForm;
import com.backflipsource.ui.EntityList;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI;
import com.backflipsource.ui.spec.EntityUI.Mode;

public abstract class EntityRequestHandler implements RequestHandler {

	private static Logger logger = logger(EntityRequestHandler.class, ALL);

	protected EntityResource resource;

	protected List<DynamicField> fields;

	protected DynamicField idField;

	protected EntityData entityData;

	protected Object item0;

	public void setResource(EntityResource resource) {
		this.resource = resource;
		fields = resource.getEntity().fields().filter(field -> field.annotation("Entity.Field") != null)
				.collect(toList());
		idField = safeStream(fields).filter(field -> field.annotations("Entity.Field").anyMatch(field2 -> {
			boolean identifier = field2.getValue("identifier");
			return identifier;
		})).findFirst().orElse(null);
		Class<?> target = (Class<?>) getFieldValue(resource.getEntity(), "target", DefaultDynamicClass.class);
		entityData = safeGet(() -> (EntityData) target.getDeclaredField("data").get(null));
		item0 = safeGet(() -> target.getDeclaredField("instance").get(null));
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		Object target = target(request);
		logger.fine(() -> "target = " + target);

		Control.Factory<?> factory = controlFactory(target, request);
		logger.fine(() -> "factory = " + factory);

		if (factory instanceof AbstractEntityControl.Factory) {
			((AbstractEntityControl.Factory<?>) factory).init(EntityServlet.entityUI(), resource.getEntity(), mode(request));
		}

		Control control = (factory != null) ? factory.create(target) : null;
		logger.fine(() -> "control = " + control);

		render(control, request, response);
	}

	protected Object target(HttpServletRequest request) {
		Object target;
		if (entityData != null) {
			target = entityData.list();
		} else if (item0 != null) {
			target = item0;
		} else {
			target = null;
		}
		return target;
	}

	protected Control.Factory<?> controlFactory(Object target, HttpServletRequest request) {
		Control.Factory<?> factory;
//		if (target instanceof Iterable) {
//			factory = new Table.Factory();
//		} else if (target != null) {
//			factory = new DescriptionList.Factory();
//		} else {
//			factory  = null;
//		}
		factory = resource.controlFactory(resource.getEntity(), mode(request));
		return factory;
	}

	protected void render(Control control, HttpServletRequest request, HttpServletResponse response) {
		Class<? extends Mode> mode = mode(request);
		logger.fine(() -> "control " + control + " mode " + mode);

		EntityUI.Context context = (EntityUI.Context) request.getAttribute(CONTEXT);
		Stack<Control> stack = ((DefaultEntityContext) context).getControls();

		control.init();
		stack.push(control);

//		Entity view2 = safeStream(entityView.getEntity().getAnnotationsByType(Entity.class))
//				.filter(item -> safeList(item.value()).contains(view)).findFirst().orElse(null);
//
//		String path2 = nonEmptyString(view2 != null ? view2.page() : null, "/entity.jsp");
		String path2 = "/entity.jsp";
		forwardServletRequest(path2, request, response);
	}

	protected Class<? extends Mode> mode(HttpServletRequest request) {
		return (item0 != null) ? EntityDetail.class : EntityList.class;
	}

	protected void save(Object item, HttpServletRequest request) {
		Map<String, StringConverter<?>> converters = resource.converters(EntityForm.class);
		Class<?> target = (Class<?>) getFieldValue(resource.getEntity(), "target", DefaultDynamicClass.class);
		Map<String, Method> setters = getSetters(target);

		safeStream(fields).filter(field -> !Objects.equals(field, idField)).forEach(field -> {
			StringConverter<?> converter = converters.get(field.getName());
			if (converter == null) {
				return;
			}
			boolean list = List.class.isAssignableFrom(field.getType());
			String[] parameters = request.getParameterValues(field.getName());
			Object value;
			if (list) {
				value = safeStream(parameters).map(converter::convertFromString).collect(toList());
			} else {
				String parameter = arrayGet(parameters, 0);
				value = converter.convertFromString(parameter);
			}
			unsafeRun(() -> setters.get(field.getName()).invoke(item, value));
		});

		entityData.save(item);
	}

	@SuppressWarnings("unchecked")
	protected Object item(String id) {
		return safeStream(entityData.list()).filter(item -> Objects.equals(idField.getValue(item).toString(), id))
				.findFirst().orElse(null);
	}
}
