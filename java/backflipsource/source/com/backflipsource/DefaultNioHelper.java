package com.backflipsource;

import static java.nio.file.FileSystems.newFileSystem;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.newDirectoryStream;
import static java.nio.file.Files.walkFileTree;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Consumer;

public class DefaultNioHelper implements NioHelper {

	@Override
	public void extractArchive(Path archive, Path directory) {
		try (FileSystem fileSystem = newFileSystem(archive)) {
			Path start = fileSystem.getPath("/");

			FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					Path target = directory.resolve(start.relativize(file).toString());
					try {
						createDirectories(target.getParent());
						copy(file, target, REPLACE_EXISTING);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					return CONTINUE;
				}
			};

			walkFileTree(start, visitor);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void acceptDirectoryEntries(Path directory, String glob, Consumer<Path> consumer) {
		try (DirectoryStream<Path> stream = newDirectoryStream(directory, glob)) {
			stream.forEach(consumer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
