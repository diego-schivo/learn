package com.backflipsource.servlet;

import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.util.logging.Level.ALL;
import static java.util.regex.Pattern.compile;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public interface RequestMatcher {

	int NO_MATCH = -1;

	int match(HttpServletRequest request);

	abstract class Abstract implements RequestMatcher {

		protected Class<?> entity;

		public Abstract(Class<?> entity) {
			this.entity = entity;
		}
	}

	abstract class FixedScore extends Abstract {

		private static Logger logger = logger(FixedScore.class, ALL);

		protected int score;

		public FixedScore(Class<?> entity, int score) {
			super(entity);
			this.score = score;
		}

		@Override
		public int match(HttpServletRequest request) {
			int score = matches(request) ? this.score : NO_MATCH;
			logger.fine(() -> "score " + score);
			return score;
		}

		protected abstract boolean matches(HttpServletRequest request);
	}

	class Regex extends FixedScore {

		private static Logger logger = logger(Regex.class, ALL);

		protected String regex;

		protected Pattern pattern;

		public Regex(Class<?> entity, int score, String regexTemplate) {
			super(entity, score);
			regex = regexTemplate.replace("_uri_", entity.getSimpleName().toLowerCase() + "s");
			pattern = compile(regex);
		}

		@Override
		protected boolean matches(HttpServletRequest request) {
			String path = request.getRequestURI().substring(request.getContextPath().length());
			boolean matches = pattern.matcher(path).matches();
			logger.fine(() -> "regex " + regex + " matches " + matches);
			return matches;
		}
	}
}
