package com.backflipsource.helper;

import static java.util.regex.Pattern.DOTALL;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;

public interface JavaPatterns {

	Pattern PATTERN_CLASS = compile("public class (\\w+)( implements \\w+)? \\{");
	Pattern PATTERN_IMPORT = compile("import ([\\w\\.]+);");
	Pattern PATTERN_INTERFACE = compile("public interface (\\w+) \\{");
	Pattern PATTERN_METHOD = compile("(@\\w+(\\(.*?\\))?\\s+)?(public static )?(?<returnType>[^\\s\\{;][^\\{;]*)\\s(?<name>\\w+)\\((?<parameters>.*?)\\)(;| \\{.*?})", DOTALL | MULTILINE);
	Pattern PATTERN_PACKAGE = compile("package (\\S[^;]*);");
	Pattern PATTERN_PARAMETER = compile("([^\\s,<]+)(<.*?>)?\\s(?<name>\\w+)");
}
