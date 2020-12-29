package com.backflipsource.ui;

import static com.backflipsource.helper.Helper.getGetterName;
import static com.backflipsource.helper.Helper.logger;
import static com.backflipsource.helper.Helper.nonNullInstance;
import static com.backflipsource.helper.Helper.stringWithoutSuffix;
import static com.backflipsource.helper.Helper.substringAfterLast;
import static com.backflipsource.helper.Helper.unsafeGet;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.backflipsource.Converter;
import com.backflipsource.dynamic.DynamicAnnotation;
import com.backflipsource.dynamic.DynamicClass;
import com.backflipsource.ui.spec.EntityUI.Mode;

@Entity.Controller(regex = "_uri_/([^/]+)", score = 1)
public class ShowEntity extends EntityRequestHandler {

	private static Logger logger = logger(ShowEntity.class, ALL);

	@Override
	protected Object target(HttpServletRequest request) {
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

		return nonNullInstance(modeItem, item);
	}

	@Override
	protected Class<? extends Mode> mode(HttpServletRequest request) {
		return EntityDetail.class;
	}
}
