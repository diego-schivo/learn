package com.backflipsource.jsp;

import java.nio.file.Path;

public interface TaglibWriter {

	void writeTaglib(Path src, String package1);
}