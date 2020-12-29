package com.backflipsource.helper;

import java.nio.file.Path;
import java.util.function.Consumer;

public interface NioHelper {

	void extractArchive(Path archive, Path directory);

	void acceptDirectoryEntries(Path directory, String glob, Consumer<Path> consumer);
}
