package com.backflipsource.servlet;

import static com.backflipsource.Helpers.classFields;
import static com.backflipsource.Helpers.getGetterName;
import static com.backflipsource.Helpers.getSetter;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.stringWithoutSuffix;
import static com.backflipsource.Helpers.substringAfterLast;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.Helpers.unsafeRun;
import static com.backflipsource.servlet.DefaultEntityView.controlFactory;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backflipsource.Converter;
import com.backflipsource.servlet.View.Show;

@Controller(regex = "/_uri_/([^/]+)", score = 1)
public class ShowEntity extends EntityRequestHandler {

	private static Logger logger = logger(ShowEntity.class, ALL);

	public ShowEntity(EntityView entityView) {
		super(entityView);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		String id = substringAfterLast(stringWithoutSuffix(path, "/edit"), "/");
		Object item = item(id);
		logger.fine(() -> "id " + id + " item " + item);

		Class<?> view = Show.class;
		Class<?> presentationClass = entityView.entity(view);

		Object presentation = null;
		if (presentationClass != entityView.getEntity()) {
			Class<?> class2 = presentationClass;

			Object item2 = unsafeGet(() -> class2.getConstructor().newInstance());
			safeStream(classFields(class2)).filter(field -> field.getAnnotation(View.Field.class) != null)
					.forEach(field -> {
						View.Field annotation = field.getAnnotation(View.Field.class);
						Class<? extends Converter> converterClass = annotation.converter2();
						if (converterClass == Converter.class) {
							Object value = unsafeGet(
									() -> item.getClass().getMethod(getGetterName(field.getName())).invoke(item));
							unsafeRun(() -> getSetter(field).invoke(item2, value));
						} else {
							Converter converter = (Converter) unsafeGet(
									() -> converterClass.getConstructor().newInstance());
							converter.convertModel(item, item2);
						}
					});
			presentation = item2;
		}

		Control.Factory<?> factory = controlFactory(entityView.getEntity(), view);

		Control control = factory.create(nonNullInstance(presentation, item));
		logger.fine(() -> "factory " + factory + " control " + control);

		render(control, view, request, response);
	}
}
