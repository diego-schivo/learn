package com.backflipsource.ui;

import static com.backflipsource.Helpers.classFields;
import static com.backflipsource.Helpers.getGetterName;
import static com.backflipsource.Helpers.getSetter;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.stringWithoutSuffix;
import static com.backflipsource.Helpers.substringAfterLast;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.ui.DefaultEntityResource.controlFactory;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.Control;
import com.backflipsource.Converter;
import com.backflipsource.servlet.EntityRequestHandler;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI.Mode;

@Entity.Controller(regex = "_uri_/([^/]+)", score = 1)
public class ShowEntity extends EntityRequestHandler {

	private static Logger logger = logger(ShowEntity.class, ALL);

	public ShowEntity(EntityResource resource) {
		super(resource);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		String id = substringAfterLast(stringWithoutSuffix(path, "/edit"), "/");
		Object item = item(id);
		logger.fine(() -> "id " + id + " item " + item);

		Class<? extends Mode> mode = EntityDetail.class;
		Class<?> modeEntity = resource.entity(mode);

		Object modeItem = null;
		if (modeEntity != resource.getEntity()) {
			Class<?> class2 = modeEntity;

			Object instance = unsafeGet(() -> class2.getConstructor().newInstance());
			safeStream(classFields(class2)).filter(field -> field.getAnnotation(Entity.Field.class) != null)
					.forEach(field -> {
						Entity.Field annotation = field.getAnnotation(Entity.Field.class);
						Class<? extends Converter> converterClass = annotation.converter2();
						if (converterClass == Converter.class) {
							Object value = unsafeGet(
									() -> item.getClass().getMethod(getGetterName(field.getName())).invoke(item));
							unsafeRun(() -> getSetter(field).invoke(instance, value));
						} else {
							Converter converter = (Converter) unsafeGet(
									() -> converterClass.getConstructor().newInstance());
							converter.convertModel(item, instance);
						}
					});
			modeItem = instance;
		}

		Control.Factory<?> factory = controlFactory(resource.getEntity(), mode);

		Control control = factory.create(nonNullInstance(modeItem, item));
		logger.fine(() -> "factory " + factory + " control " + control);

		render(control, mode, request, response);
	}
}
