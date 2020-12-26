package com.backflipsource.ui;

import static com.backflipsource.Helpers.getGetterName;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.stringWithoutSuffix;
import static com.backflipsource.Helpers.substringAfterLast;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.ui.DefaultEntityResource.controlFactory;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.Control;
import com.backflipsource.Converter;
import com.backflipsource.dynamic.DynamicAnnotation;
import com.backflipsource.dynamic.DynamicClass;
import com.backflipsource.servlet.EntityRequestHandler;
import com.backflipsource.ui.spec.EntityUI.Mode;

@Entity.Controller(regex = "_uri_/([^/]+)", score = 1)
public class ShowEntity extends EntityRequestHandler {

	private static Logger logger = logger(ShowEntity.class, ALL);

	@Override
	@SuppressWarnings("rawtypes")
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		String id = substringAfterLast(stringWithoutSuffix(path, "/edit"), "/");
		Object item = item(id);
		logger.fine(() -> "id " + id + " item " + item);

		Class<? extends Mode> mode = mode(request);
		DynamicClass modeEntity = resource.entity(mode);

		Object modeItem = null;
		if (modeEntity != resource.getEntity()) {
			DynamicClass class2 = modeEntity;

			Object instance = class2.newInstance();
			class2.fields().forEach(field -> {
				DynamicAnnotation annotation = field.annotation("Entity.Field");
				if (annotation == null) {
					return;
				}

				Class<? extends Converter> converterClass = annotation.getValue("converter2");
				if (converterClass == Converter.class) {
					Object value = unsafeGet(
							() -> item.getClass().getMethod(getGetterName(field.getName())).invoke(item));
					field.setValue(instance, value);
				} else {
					Converter converter = (Converter) unsafeGet(() -> converterClass.getConstructor().newInstance());
					converter.convertModel(item, instance);
				}
			});
			modeItem = instance;
		}

		Control.Factory<?> factory = controlFactory(resource.getEntity(), mode);

		Control control = factory.create(nonNullInstance(modeItem, item));
		logger.fine(() -> "factory " + factory + " control " + control);

		render(control, request, response);
	}

	@Override
	protected Class<? extends Mode> mode(HttpServletRequest request) {
		return EntityDetail.class;
	}
}
