package com.backflipsource;

import static com.backflipsource.helper.Helper.logger;
import static com.backflipsource.helper.Helper.safeStream;
import static java.util.logging.Level.ALL;
import static java.util.regex.Pattern.compile;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public interface RequestMatcher {

	int NO_MATCH = -1;

	int match(HttpServletRequest request);

	abstract class FixedScore implements RequestMatcher {

		private static Logger logger = logger(FixedScore.class, ALL);

		protected int score;

		public FixedScore(int score) {
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

		public Regex(String regex, int score) {
			super(score);
			this.regex = regex;
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

	class Parameter extends FixedScore {

		private static Logger logger = logger(Parameter.class, ALL);

		protected String parameter;

		public Parameter(String parameter, int score) {
			super(score);
			this.parameter = parameter;
		}

		@Override
		protected boolean matches(HttpServletRequest request) {
			boolean matches = request.getParameter(parameter) != null;
			logger.fine(() -> "parameter " + parameter + " matches " + matches);
			return matches;
		}
	}

	class And implements RequestMatcher {

		private static Logger logger = logger(And.class, ALL);

		protected Iterable<RequestMatcher> matchers;

		public And(Iterable<RequestMatcher> matchers) {
			this.matchers = matchers;
		}

		@Override
		public int match(HttpServletRequest request) {
			int score = safeStream(matchers).mapToInt(matcher -> matcher.match(request)).min().orElse(NO_MATCH);
			logger.fine(() -> "score " + score);
			return score;
		}
	}
}
