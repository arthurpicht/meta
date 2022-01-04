package de.arthurpicht.meta.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesHelper {

    public static Path getWorkingDir() {
        return Paths.get(".").normalize().toAbsolutePath();
    }

    public static boolean isDirectoryNonEmpty(Path dir) throws IOException {
        if (!Files.exists(dir)) return false;
        if (!Files.isDirectory(dir)) return false;
        return Files.list(dir).findAny().isPresent();
    }

    public static boolean isExistingDirectory(Path dir) {
        if (!Files.exists(dir)) return false;
        return Files.isDirectory(dir);
    }

    public static boolean isRootDirectory(Path path) {
        if (!Files.isDirectory(path)) throw new IllegalArgumentException("Specified path [" + path + "] is not a directory.");
        Path canonicalPath = path.normalize().toAbsolutePath();
        return (canonicalPath.getParent() == null);
    }

}
