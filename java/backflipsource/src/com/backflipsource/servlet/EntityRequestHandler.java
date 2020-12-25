package com.backflipsource.servlet;

import static com.backflipsource.Helpers.arrayGet;
import static com.backflipsource.Helpers.classFields;
import static com.backflipsource.Helpers.forwardServletRequest;
import static com.backflipsource.Helpers.getGetter;
import static com.backflipsource.Helpers.getSetters;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.servlet.EntityServlet.CONTEXT;
import static java.util.logging.Level.ALL;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.Control;
import com.backflipsource.RequestHandler;
import com.backflipsource.ui.Entity;
import com.backflipsource.ui.EntityForm;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI;
import com.backflipsource.ui.spec.EntityUI.Mode;

public abstract class EntityRequestHandler implements RequestHandler {

	private static Logger logger = logger(EntityRequestHandler.class, ALL);

	protected EntityResource resource;

	protected List<Field> fields;

	protected Field idField;

	protected EntityData entityData;

	protected Object item0;

	public EntityRequestHandler(EntityResource resource) {
		this.resource = resource;
		fields = safeStream(classFields(resource.getEntity()))
				.filter(field -> field.getAnnotationsByType(Entity.Field.class).length > 0).collect(toList());
		idField = safeStream(fields).filter(
				field -> safeStream(field.getAnnotationsByType(Entity.Field.class)).anyMatch(Entity.Field::identifier))
				.findFirst().orElse(null);
		entityData = safeGet(() -> (EntityData) resource.getEntity().getDeclaredField("data").get(null));
		item0 = safeGet(() -> resource.getEntity().getDeclaredField("instance").get(null));
	}

	protected void render(Control control, Class<? extends Mode> mode, HttpServletRequest request, HttpServletResponse response) {
		logger.fine(() -> "control " + control + " view " + mode);

		EntityUI.Context context = (EntityUI.Context) request.getAttribute(CONTEXT);
		((DefaultEntityContext) context).getControls().push(control);

//		Entity view2 = safeStream(entityView.getEntity().getAnnotationsByType(Entity.class))
//				.filter(item -> safeList(item.value()).contains(view)).findFirst().orElse(null);
//
//		String path2 = nonEmptyString(view2 != null ? view2.page() : null, "/entity.jsp");
		String path2 = "/entity.jsp";
		forwardServletRequest(path2, request, response);
	}

	protected void save(Object item, HttpServletRequest request) {
		Map<String, StringConverter<?>> converters = resource.converters(EntityForm.class);
		Map<String, Method> setters = getSetters(resource.getEntity());

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
		return safeStream(entityData.list())
				.filter(item -> Objects.equals(safeGet(() -> getGetter(idField).invoke(item)).toString(), id))
				.findFirst().orElse(null);
	}
}