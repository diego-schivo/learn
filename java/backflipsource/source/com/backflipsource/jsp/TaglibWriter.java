package com.backflipsource.jsp;

import java.nio.file.Path;

public interface TaglibWriter {

	void writeTaglib(Path source, String package1);
}