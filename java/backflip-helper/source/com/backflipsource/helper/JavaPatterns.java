package com.backflipsource.helper;

import static java.util.regex.Pattern.DOTALL;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;

public interface JavaPatterns {

	Pattern PATTERN_IMPORT = compile("import (\\S[^;]*);");
	Pattern PATTERN_INTERFACE = compile("public interface (\\S+) \\{");
	Pattern PATTERN_METHOD = compile("([^\\s\\{;][^\\{;]*)\\s(\\w+)\\((.*?)\\);", DOTALL | MULTILINE);
	Pattern PATTERN_PACKAGE = compile("package (\\S[^;]*);");
	Pattern PATTERN_PARAMETER = compile("([^\\s,<]+)(<.*?>)?\\s(?<name>[^\\s,]+)");
}
