package com.backflipsource.helper;

import java.net.URL;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface NioHelper {

	void acceptDirectoryEntries(Path directory, String glob, Consumer<Path> consumer);

	void extractArchive(Path archive, Path directory);

	void downloadFile(URL url, Path directory);
}
